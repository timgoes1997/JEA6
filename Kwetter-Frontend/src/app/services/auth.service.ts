import {Injectable} from '@angular/core';

import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import {MessageService} from './message.service';
import {AuthRegistrationObject} from '../entities/AuthRegistrationObject';
import {catchError, map, tap} from 'rxjs/operators';
import {User} from '../entities/User';
import {CookieService} from 'ng2-cookies';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Router} from '@angular/router';

const headers = new HttpHeaders({
  'Content-Type': 'application/x-www-form-urlencoded'
});
const authHeaderKey = 'Authorization';

@Injectable()
export class AuthService {
  private authURL = 'http://localhost:8080/Kwetter/api/user';

  public loggedInUser: BehaviorSubject<User> = new BehaviorSubject<User>(null);

  constructor(private http: HttpClient,
              private messageService: MessageService,
              private cookieService: CookieService,
              private router: Router) {
  }

  register(registrationObject: AuthRegistrationObject): Observable<HttpResponse<any>> {
    const registrationURL = `${this.authURL}/register`;
    const body = new HttpParams()
      .set('username', registrationObject.username)
      .set('password', registrationObject.password)
      .set('email', registrationObject.email)
      .set('firstName', registrationObject.firstName)
      .set('middleName', registrationObject.middleName)
      .set('lastName', registrationObject.lastName)
      .set('telephone', registrationObject.telephone);
    return this.http.post(
      registrationURL,
      body, {observe: 'response', headers: headers});
    /**.pipe(
     tap(_ => this.log(_.toString())),
     catchError(this.handleError<any>('register'))
     )*/
  }

  login(username: string,
        password: string) {
    const loginURL = `${this.authURL}/login`;
    const body = new HttpParams()
      .set('username', username)
      .set('password', password);
    this.http.post(loginURL, body, {observe: 'response', headers: headers, withCredentials: true}).subscribe(
      (res: HttpResponse<any>) => {
        this.loginReceive(res);
      });
  }

  logout() {
    this.loggedInUser.next(null);
  }

  isUserAuthenticated() {
    const authValue = this.cookieService.get(authHeaderKey);
    if (authValue) {
      this.getAuthenticatedUser(authHeaderKey, authValue).subscribe(
        (res: HttpResponse<any>) => {
          this.logoutReceive(res);
        });
    }
  }

  getAuthenticatedUser(authKey: string, authValue: string): Observable<any> {
    const requestURL = `${this.authURL}/authenticated`;
    const testHeaders = new HttpHeaders().append(authKey, authValue);
    return this.http.get<User>(requestURL, {
      observe: 'response',
      headers: testHeaders
    });
  }

  private loginReceive(http: HttpResponse<any>) {
    if (http.status === 200) {
      const authKey = 'Authorization';
      const authValue = http.headers.get(authKey);
      this.cookieService.set(authKey, authValue);
      this.loggedInUser.next(http.body);
      console.log(authValue);
      this.router.navigateByUrl('/');
    }
  }

  private logoutReceive(http: HttpResponse<any>) {
    if (http.status === 200) {
      this.loggedInUser.next(http.body);
      this.router.navigateByUrl('/');
    } else {
      console.log('login failed');
    }
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  private log(message: string) {
    this.messageService.add('HeroService: ' + message);
  }

}
