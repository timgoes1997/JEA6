import {Component, OnInit} from '@angular/core';
import {CookieService} from 'ng2-cookies';
import {Router} from '@angular/router';

const authKey = 'Authorization';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  constructor(private cookieService: CookieService,
              private router: Router) {
  }

  ngOnInit() {
    this.OnLogOut();
  }

  OnLogOut() {
    this.cookieService.delete(authKey);
    this.router.navigateByUrl('/');
  }
}
