import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from '../entities/User';
import {HttpResponse} from '@angular/common/http';
import {Kweet} from '../entities/Kweet';
import {KweetService} from '../services/kweet.service';
import {catchError, tap} from 'rxjs/operators';
import {AuthService} from '../services/auth.service';
import {ErrorHandlingService} from '../services/error-handling.service';
import {MessageRepliesComponent} from '../message-replies/message-replies.component';
import {MessageReplyCountComponent} from '../message-reply-count/message-reply-count.component';
import {MessageRemessageComponent} from '../message-remessage/message-remessage.component';
import {MessageRemessageCountComponent} from '../message-remessage-count/message-remessage-count.component';

@Component({
  selector: 'app-message-details',
  templateUrl: './message-details.component.html',
  styleUrls: ['./message-details.component.css']
})
export class MessageDetailsComponent implements OnInit {

  @Input()
  kweet: Kweet;

  @Output()
  onMessageDeleted: EventEmitter<Kweet> = new EventEmitter<Kweet>();

  @ViewChild(MessageRepliesComponent)
  private messageRepliesComponent: MessageRepliesComponent;

  @ViewChild(MessageReplyCountComponent)
  private messageReplyCountComponent: MessageReplyCountComponent;

  @ViewChild(MessageRemessageCountComponent)
  private messageRemessageCountComponent: MessageRemessageCountComponent;

  currentLoggedInUser: User;

  kweetUsingInput = true;

  constructor(private kweetService: KweetService,
              private errorHandlingService: ErrorHandlingService,
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
    if (!this.kweet) {
      this.kweetUsingInput = false;
      const name = this.route.snapshot.paramMap.get('name');
      const message = +this.route.snapshot.paramMap.get('message');

      this.kweetService.getKweet(name, message).pipe(
        tap(_ => this.errorHandlingService.log(`fetched kweet ${message} from user ${name}`)),
        catchError(this.errorHandlingService.handleError<HttpResponse<Kweet>>(`retrieving kweet ${message} from ${name}`))
      ).subscribe(kweet => this.OnReceive(kweet));
    }
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
    if (this.kweetUsingInput && kweet) {
      this.onMessageDeleted.emit(kweet);
    }
    if (kweet && this.kweet && this.kweet.id === kweet.id) {
      const name = this.route.snapshot.paramMap.get('name');
      this.router.navigateByUrl(`user/${name}`);
    }
  }

  OnUserCreatedReply(kweet: Kweet) {
    if (this.messageRepliesComponent) {
      this.messageRepliesComponent.OnReceiveUserCreatedReply(kweet);
      this.messageReplyCountComponent.getReplyCount();
      this.messageRemessageCountComponent.getRemessageCount();
    }
  }

  Reload() {
    location.reload();
  }

}
