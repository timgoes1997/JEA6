import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {UserService} from '../services/user.service';
import {CookieService} from 'ng2-cookies';
import {MessageService} from '../services/message.service';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from '../entities/User';
import {HttpResponse} from '@angular/common/http';
import {Kweet} from '../entities/Kweet';
import {KweetService} from '../services/kweet.service';
import {catchError, tap} from 'rxjs/operators';
import {AuthService} from '../services/auth.service';

@Component({
  selector: 'app-message-details',
  templateUrl: './message-details.component.html',
  styleUrls: ['./message-details.component.css']
})
export class MessageDetailsComponent implements OnInit {

  kweet: Kweet;

  currentLoggedInUser: User;

  constructor(private kweetService: KweetService,
              private cookieService: CookieService,
              private messageService: MessageService,
              private authService: AuthService,
              private route: ActivatedRoute,
              private router: Router) {
    this.kweetService.userDeletedKweet.subscribe(value => {
      this.onReceiveDeleteKweet(value);
    });
    this.authService.loggedInUser.subscribe(value => {
      this.onReceiveUser(value);
    });
  }

  ngOnInit() {
    const name = this.route.snapshot.paramMap.get('name');
    const message = +this.route.snapshot.paramMap.get('message');

    this.kweetService.getKweet(name, message).pipe(
      tap(_ => this.log(`fetched kweet ${message} from user ${name}`)),
      catchError(this.handleError<HttpResponse<Kweet>>(`retrieving kweet ${message} from ${name}`))
    ).subscribe(kweet => this.OnReceive(kweet));
  }

  OnReceive(response: HttpResponse<Kweet>) {
    if (response) {
      this.kweet = response.body;
      console.log('received kweet');
    } else {
      const name = this.route.snapshot.paramMap.get('name');
      this.router.navigateByUrl(`user/${name}`);
      console.log('user tried to visit non existing user');
    }
  }

  onReceiveUser(user: User) {
    this.currentLoggedInUser = user;
  }

  onReceiveDeleteKweet(kweet: Kweet) {
    if (kweet && this.kweet && this.kweet.id === kweet.id) {
      const name = this.route.snapshot.paramMap.get('name');
      this.router.navigateByUrl(`user/${name}`);
    }
  }

  Delete() {
    console.log('test');
    this.kweetService.deleteKweet(this.kweet);
  }

  equalsModeratorAdminOrUser(): boolean {
    if (this.kweet && this.currentLoggedInUser && this.kweet.messager) {
      return this.currentLoggedInUser.role === 'Admin' || this.currentLoggedInUser.role === 'Moderator'
        || this.currentLoggedInUser.id === this.kweet.messager.id;
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

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  private log(message: string) {
    this.messageService.add('HeroService: ' + message);
  }
}
