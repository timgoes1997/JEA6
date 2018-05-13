import {Component, OnInit} from '@angular/core';
import {User} from '../entities/User';
import {AuthService} from '../services/auth.service';

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
    this.authService.retreiveAuthenticatedUser();
  }

  loginNavBar(user: User) {
    this.loggedIn = user;
  }

  reload() {
    location.reload(true);
  }
}
