import {Component, OnInit} from '@angular/core';
import {User} from '../entities/User';
import {of} from 'rxjs/observable/of';
import {Observable} from 'rxjs/Observable';
import {AuthService} from '../services/auth.service';
import {MessageService} from '../services/message.service';
import {HttpResponse} from '@angular/common/http';
import {CookieService} from 'ng2-cookies';

const authKey = 'Authorization';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  loggedIn: User;

  constructor(private authService: AuthService,
              private messageService: MessageService,
              private cookieService: CookieService) {
  }

  ngOnInit() {
    this.OnAuth();
  }

  OnAuth() {
    const authValue = this.cookieService.get(authKey);
    if (authValue) {
      this.authService.isUserAuthenticated(authKey, authValue).subscribe(
        (res: HttpResponse<any>) => {
          this.OnReceive(res);
        });
    }
  }

  private OnReceive(http: HttpResponse<any>) {
    if (http.status === 200) {
      this.loggedIn = http.body;
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

      // TODO: handling error request
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
