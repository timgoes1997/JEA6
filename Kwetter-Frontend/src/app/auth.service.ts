import {Injectable} from '@angular/core';

import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import {MessageService} from './message.service';
import {AuthRegistrationObject} from './AuthRegistrationObject';
import {catchError, map, tap} from 'rxjs/operators';

const headers = new HttpHeaders({
  'Content-Type': 'application/x-www-form-urlencoded'
});

@Injectable()
export class AuthService {
  private authURL = 'http://localhost:8080/Kwetter/api/user';

  constructor(private http: HttpClient,
              private messageService: MessageService) {
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
    /** const options = { observe: 'response', headers: headers}; dunno why but can't give this as
     * param need to make the object itself otherwise compile error...*/
    return this.http.post(
      registrationURL,
      body, {observe: 'response', headers: headers})/**.pipe(
     tap(_ => this.log(_.toString())),
     catchError(this.handleError<any>('register'))
     )*/;
  }

  login(username: string,
        password: string): Observable<HttpResponse<any>> {
    const loginURL = `${this.authURL}/login`;
    const body = new HttpParams()
      .set('username', username)
      .set('password', password);
    return this.http.post(loginURL, body, {observe: 'response', headers: headers, withCredentials: true});
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
