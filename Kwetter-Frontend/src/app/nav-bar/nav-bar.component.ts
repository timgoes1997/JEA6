import {Component, OnInit} from '@angular/core';
import {User} from '../entities/User';
import {of} from 'rxjs/observable/of';
import {Observable} from 'rxjs/Observable';
import {AuthService} from '../services/auth.service';
import {MessageService} from '../services/message.service';
import {HttpResponse} from '@angular/common/http';
import {CookieService} from 'ng2-cookies';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  loggedIn: User;

  constructor(private authService: AuthService) {
    this.authService.loggedInUser.subscribe(value => {
      this.loginNavBar(value);
    });
  }

  ngOnInit() {
    this.authService.isUserAuthenticated();
  }

  loginNavBar(user: User) {
    this.loggedIn = user;
  }

}
