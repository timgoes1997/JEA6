import {Component, OnInit} from '@angular/core';
import {UserService} from '../services/user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from '../entities/User';
import {HttpResponse} from '@angular/common/http';
import {catchError, tap} from 'rxjs/operators';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {MessageService} from '../services/message.service';
import {Kweet} from '../entities/Kweet';
import {KweetService} from '../services/kweet.service';
import {AuthService} from '../services/auth.service';
import {$WebSocket, WebSocketSendMode} from 'angular2-websocket/angular2-websocket';
import {ErrorHandlingService} from '../services/error-handling.service';

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
              private errorHandlingService: ErrorHandlingService,
              private kweetService: KweetService,
              private route: ActivatedRoute,
              private router: Router) {
    this.authService.loggedInUser.subscribe(value => {
      this.onReceiveUser(value);
    });
    this.kweetService.userCreateKweet.subscribe(value => {
      this.onReceiveUserCreatedKweet(value);
    });
  }

  ngOnInit() {
    const name = this.route.snapshot.paramMap.get('name');
    this.getUser(name);
    this.getUserKweets(name);
    this.initSocketConnection(name);
  }

  onReceiveUser(user: User) {
    this.currentLoggedInUser = user;
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
      tap(_ => this.errorHandlingService.log(`fetched user named ${name}`)),
      catchError(this.errorHandlingService.handleError<HttpResponse<User>>(`getHero name=${name}`))
    ).subscribe(user => this.OnReceiveUser(user));
  }

  getUserKweets(name: string) {
    this.kweetService.getKweets(name).pipe(
      tap(_ => this.errorHandlingService.log(`fetched kweets for user named ${name}`)),
      catchError(this.errorHandlingService.handleError<HttpResponse<Kweet[]>>(`getHero name=${name}`))
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

  OnReceiveUserKweetDelete(kweet: Kweet) {
    if (kweet && this.kweets) {
      for (const i in this.kweets) {
        if (this.kweets[i].id === kweet.id) {
          this.kweets.splice(+i, 1);
        }
      }
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
}
