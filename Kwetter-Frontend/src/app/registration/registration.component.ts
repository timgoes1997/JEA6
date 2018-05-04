import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {logger} from 'codelyzer/util/logger';
import {AuthService} from '../auth.service';
import {AuthRegistrationObject} from '../AuthRegistrationObject';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

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
      email: '',
      firstName: '',
      middleName: '',
      lastName: '',
      telephone: '',
    });
  }

  ngOnInit() {
  }

  OnRegister() {
    const formModel = this.loginForm;
    const registrationObject = new AuthRegistrationObject(
      formModel.controls.userName.value,
      formModel.controls.password.value,
      formModel.controls.email.value,
      formModel.controls.firstName.value,
      formModel.controls.middleName.value,
      formModel.controls.lastName.value,
      formModel.controls.telephone.value,
    );
    this.authService.register(registrationObject).subscribe(
      resp => {
          const keys = resp.headers.keys();
          console.log(resp.status);
          console.log(resp.headers.get('Authorization'));
      }
    );

    // logger.info('login called');
    // this.submitted = true;
    // this.router.navigateByUrl('/');
  }

}
