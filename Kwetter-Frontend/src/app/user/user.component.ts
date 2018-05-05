import { Component, OnInit } from '@angular/core';
import {UserService} from '../user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {CookieService} from 'ng2-cookies';
import {User} from '../entities/User';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  user: User;

  constructor(private userService: UserService,
              private cookieService: CookieService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit() {
    const name = this.route.snapshot.paramMap.get('name');
    this.userService.getUser(name).subscribe(user => this.OnReceive(user));
  }

  OnReceive(user: User) {
    this.user = user;
    console.log('received user');
  }

}
