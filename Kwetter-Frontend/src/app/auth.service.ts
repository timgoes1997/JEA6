import {Injectable} from '@angular/core';

import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import {MessageService} from './message.service';
import {AuthRegistrationObject} from './AuthRegistrationObject';
import {catchError, map, tap} from 'rxjs/operators';


const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/x-www-form-urlencoded'
  }), observe: 'response'
}


// const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' }), observe: 'response' };

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
    return this.http.post(
      registrationURL,
      body, httpOptions)/**.pipe(
      tap(_ => this.log(_.toString())),
      catchError(this.handleError<any>('register'))
    )*/;
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
