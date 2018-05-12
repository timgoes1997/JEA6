import {Component, OnInit} from '@angular/core';
import {UserService} from '../services/user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {CookieService} from 'ng2-cookies';
import {User} from '../entities/User';
import {HttpResponse} from '@angular/common/http';
import {catchError, tap} from 'rxjs/operators';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {MessageService} from '../services/message.service';
import {Kweet} from '../entities/Kweet';
import {KweetService} from '../services/kweet.service';
import {AuthService} from '../services/auth.service';
import {Event} from '../enums/event.enum';
import {$WebSocket, WebSocketSendMode} from 'angular2-websocket/angular2-websocket';

const webSocketURL = 'ws://localhost:8080/Kwetter/listener/user/';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  user: User;

  currentLoggedInUser: User;

  kweets: Kweet[];

  ws: any;

  constructor(private userService: UserService,
              private authService: AuthService,
              private cookieService: CookieService,
              private messageService: MessageService,
              private kweetService: KweetService,
              private route: ActivatedRoute,
              private router: Router) {
    this.authService.loggedInUser.subscribe(value => {
      this.currentLoggedInUser = value;
    });
    this.kweetService.userCreateKweet.subscribe(value => {
      this.onReceiveUserCreatedKweet(value);
    });
    this.kweetService.userDeletedKweet.subscribe(value => {
      this.onReceiveUserKweetDelete(value);
    });
  }

  ngOnInit() {
    const name = this.route.snapshot.paramMap.get('name');
    this.getUser(name);
    this.getUserKweets(name);
    this.initSocketConnection(name);
  }

  isUserLoggedInUser(): boolean {
    if (this.user && this.currentLoggedInUser) {
      return this.user.id === this.currentLoggedInUser.id;
    } else {
      return false;
    }
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

  onReceiveUserCreatedKweet(kweet: Kweet) {
    if (kweet && this.kweets) {
      this.kweets.unshift(kweet);
    }
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

  Delete(kweet: Kweet) {
    console.log('test');
    this.kweetService.deleteKweet(kweet);
  }

  private onReceiveUserKweetDelete(kweet: Kweet) {
    if (kweet && this.kweets) {
      for (const i in this.kweets) {
        if (this.kweets[i].id === kweet.id) {
          this.kweets.splice(+i, 1);
        }
      }
    }
  }

  equalsModeratorAdminOrUser(kweet: Kweet): boolean {
    if (kweet && this.currentLoggedInUser && kweet.messager) {
      return this.currentLoggedInUser.role === 'Admin' || this.currentLoggedInUser.role === 'Moderator'
        || this.currentLoggedInUser.id === kweet.messager.id;
    }
  }

  private initSocketConnection(name: string): void {
    const URL = webSocketURL + name;
    this.ws = new $WebSocket(URL).onOpen(() => {
      console.log('Websocket is connected!');
    });

    /*
    this.ws.setSend4Mode(WebSocketSendMode.Direct);
    this.ws.send('Hello My name is Jeff and i like *');

    */
    this.ws.onMessage((msg: MessageEvent) => {
      console.log('onMessage', msg.data);
      if (!this.isUserLoggedInUser()) {
        const kweetObject = JSON.parse(msg.data);
        this.onReceiveUserCreatedKweet(kweetObject);
      }
    });

    this.ws.onClose(() => {
      console.log('Websocket has been closed');
    });
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
