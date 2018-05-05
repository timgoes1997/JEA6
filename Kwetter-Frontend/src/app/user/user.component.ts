import {Component, OnInit} from '@angular/core';
import {UserService} from '../user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {CookieService} from 'ng2-cookies';
import {User} from '../entities/User';
import {HttpResponse} from '@angular/common/http';
import {catchError, tap} from 'rxjs/operators';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {MessageService} from '../message.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  user: User;

  constructor(private userService: UserService,
              private cookieService: CookieService,
              private messageService: MessageService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit() {
    const name = this.route.snapshot.paramMap.get('name');
    this.userService.getUser(name).pipe(
      tap(_ => this.log(`fetched user named ${name}`)),
      catchError(this.handleError<HttpResponse<User>>(`getHero name=${name}`))
    ).subscribe(user => this.OnReceive(user));
  }

  OnReceive(response: HttpResponse<User>) {
    if (response) {
      this.user = response.body;
      console.log('received user');
    } else {
      this.router.navigateByUrl('/');
      console.log('user tried to visit non existing user');
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
