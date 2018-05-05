import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing/app-routing.module';
import {RegistrationComponent} from './registration/registration.component';
import {AuthService} from './auth.service';
import {MessageService} from './message.service';
import {HttpClientModule} from '@angular/common/http';
import {LoginComponent} from './login/login.component';
import {CookieService} from 'ng2-cookies';
import {UserComponent} from './user/user.component';
import {UserService} from './user.service';
import { MessageDetailsComponent } from './message-details/message-details.component';
import {KweetService} from './kweet.service';
import { TagDetailsComponent } from './tag-details/tag-details.component';


@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    LoginComponent,
    UserComponent,
    MessageDetailsComponent,
    TagDetailsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [
    AuthService,
    MessageService,
    CookieService,
    UserService,
    KweetService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
