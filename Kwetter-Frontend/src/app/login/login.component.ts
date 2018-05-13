import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  submitted = false;

  loginForm: FormGroup;

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private router: Router) {
    this.createForm();
  }

  createForm() {
    this.loginForm = this.fb.group({
      userName: '',
      password: '',
    });
  }

  ngOnInit() {
  }

  OnLogin() {
    const formModel = this.loginForm;
    const username = formModel.controls.userName.value;
    const password = formModel.controls.password.value;
    this.authService.login(username, password);
  }

  btnRegisterClick() {
    this.router.navigateByUrl('/registration');
  }
}
