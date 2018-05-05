import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import {MessageService} from './message.service';
import {AuthService} from './auth.service';
import {Observable} from 'rxjs/Observable';
import {User} from './entities/User';
import {of} from 'rxjs/observable/of';
import {catchError, tap} from 'rxjs/operators';

const headers = new HttpHeaders({
  'Content-Type': 'application/x-www-form-urlencoded'
});

@Injectable()
export class UserService {
  private userURL = 'http://localhost:8080/Kwetter/api/user';

  constructor(private http: HttpClient,
              private messageService: MessageService,
              private authService: AuthService) {
  }

  getUser(name: string): Observable<User> {
    const requestURL = `${this.userURL}/${name}`;
    return this.http.get<User>(requestURL).pipe(
      tap(_ => this.log(`fetched hero name=${name}`)),
      catchError(this.handleError<User>(`getHero name=${name}`))
    );
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
