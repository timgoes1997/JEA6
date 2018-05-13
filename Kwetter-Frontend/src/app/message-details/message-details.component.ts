import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from '../entities/User';
import {HttpResponse} from '@angular/common/http';
import {Kweet} from '../entities/Kweet';
import {KweetService} from '../services/kweet.service';
import {catchError, tap} from 'rxjs/operators';
import {AuthService} from '../services/auth.service';
import {ErrorHandlingService} from '../services/error-handling.service';

@Component({
  selector: 'app-message-details',
  templateUrl: './message-details.component.html',
  styleUrls: ['./message-details.component.css']
})
export class MessageDetailsComponent implements OnInit {

  @Input()
  kweet: Kweet;

  currentLoggedInUser: User;

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
}
