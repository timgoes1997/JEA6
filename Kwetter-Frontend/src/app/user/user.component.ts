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
import {Kweet} from '../entities/Kweet';
import {KweetService} from '../kweet.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  user: User;

  kweets: Kweet[];

  constructor(private userService: UserService,
              private cookieService: CookieService,
              private messageService: MessageService,
              private kweetService: KweetService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit() {
    const name = this.route.snapshot.paramMap.get('name');
    this.getUser(name);
    this.getUserKweets(name);
  }

  getUser(name: string) {
    this.userService.getUser(name).pipe(
      tap(_ => this.log(`fetched user named ${name}`)),
      catchError(this.handleError<HttpResponse<User>>(`getHero name=${name}`))
    ).subscribe(user => this.OnReceiveUser(user));
  }

  getUserKweets(name: string) {
    this.kweetService.getKweets(name).pipe(
      tap(_ => this.log(`fetched kweets for user named ${name}`)),
      catchError(this.handleError<HttpResponse<Kweet[]>>(`getHero name=${name}`))
    ).subscribe(kweets => this.OnReceiveUserKweets(kweets));
  }

  OnReceiveUser(response: HttpResponse<User>) {
    if (response) {
      this.user = response.body;
      console.log('received user');
    } else {
      this.router.navigateByUrl('/');
      console.log('user tried to visit non existing user');
    }
  }

  OnReceiveUserKweets(response: HttpResponse<Kweet[]>) {
    if (response) {
      this.kweets = response.body;
      console.log('received kweet list');
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
