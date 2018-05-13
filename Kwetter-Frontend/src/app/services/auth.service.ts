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
              private cookieService: CookieService,
              private router: Router) {
  }

  register(registrationObject: AuthRegistrationObject) {
    const registrationURL = `${this.authURL}/register`;
    const body = new HttpParams()
      .set('username', registrationObject.username)
      .set('password', registrationObject.password)
      .set('email', registrationObject.email)
      .set('firstName', registrationObject.firstName)
      .set('middleName', registrationObject.middleName)
      .set('lastName', registrationObject.lastName)
      .set('telephone', registrationObject.telephone);
    this.http.post(
      registrationURL,
      body, {observe: 'response', headers: headers}).subscribe(
      resp => {
        this.registerReceive(resp);
      }
    );
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
    //this.cookieService.delete(authHeaderKey);
    const cookies = document.cookie.split('; ');
    for (let c = 0; c < cookies.length; c++) {
      const d = window.location.hostname.split('.');
      while (d.length > 0) {
        const cookieBase = encodeURIComponent(cookies[c]
          .split(';')[0]
          .split('=')[0]) + '=; expires=Thu, 01-Jan-1970 00:00:01 GMT; domain=' + d.join('.') + ' ;path=';
        const p = location.pathname.split('/');
        document.cookie = cookieBase + '/';
        while (p.length > 0) {
          document.cookie = cookieBase + p.join('/');
          p.pop();
        }
        d.shift();
      }
    }
    this.loggedInUser.next(null);
  }

  isLoggedInUser(user: User): boolean {
    return user.id === this.loggedInUser.getValue().id;
  }

  retreiveAuthenticatedUser() {
    const authValue = this.getAuthToken(authHeaderKey);
    if (authValue) {
      this.getAuthenticatedUser(authHeaderKey, authValue).subscribe(
        (res: HttpResponse<any>) => {
          this.authenticatedUserReceive(res);
        });
    }
  }

  getAuthToken(authKey: string): string {
    return this.cookieService.get(authKey);
  }

  getAuthenticatedUser(authKey: string, authValue: string): Observable<any> {
    const requestURL = `${this.authURL}/authenticated`;
    const testHeaders = new HttpHeaders().append(authKey, authValue);
    return this.http.get<User>(requestURL, {
      observe: 'response',
      headers: testHeaders
    });
  }

  getAuthHeaders(): HttpHeaders {
    const authValue = this.getAuthToken(authHeaderKey);
    if (authValue) {
      return new HttpHeaders().append(authHeaderKey, authValue);
    }
    return new HttpHeaders();
  }

  private registerReceive(http: HttpResponse<any>) {
    console.log('received register response');
    // this.loginReceive(http);
  }

  private loginReceive(http: HttpResponse<any>) {
    if (http.status === 200) {
      const authKey = 'Authorization';
      const authValue = http.headers.get(authKey);
      this.cookieService.set(authKey, authValue, 3, '/', window.location.hostname);
      this.loggedInUser.next(http.body);
      console.log(authValue);
      this.router.navigateByUrl('/');
    }
  }

  private authenticatedUserReceive(http: HttpResponse<any>) {
    if (http.status === 200) {
      this.loggedInUser.next(http.body);
    } else {
      console.log('login failed');
    }
  }
}
