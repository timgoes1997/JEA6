import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {logger} from 'codelyzer/util/logger';
import {AuthService} from '../services/auth.service';
import {AuthRegistrationObject} from '../entities/AuthRegistrationObject';
import {HttpResponse} from '@angular/common/http';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  submitted = false;

  registrationForm: FormGroup;

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private router: Router) {
    this.createForm();
  }

  createForm() {
    this.registrationForm = this.fb.group({
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
    const formModel = this.registrationForm;
    const registrationObject = new AuthRegistrationObject(
      formModel.controls.userName.value,
      formModel.controls.password.value,
      formModel.controls.email.value,
      formModel.controls.firstName.value,
      formModel.controls.middleName.value,
      formModel.controls.lastName.value,
      formModel.controls.telephone.value,
    );
    this.authService.register(registrationObject);
    this.submitted = true;
    this.router.navigateByUrl('/');
  }
}
