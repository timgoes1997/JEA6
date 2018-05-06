import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing/app-routing.module';
import {RegistrationComponent} from './registration/registration.component';
import {AuthService} from './services/auth.service';
import {MessageService} from './services/message.service';
import {HttpClientModule} from '@angular/common/http';
import {LoginComponent} from './login/login.component';
import {CookieService} from 'ng2-cookies';
import {UserComponent} from './user/user.component';
import {UserService} from './services/user.service';
import { MessageDetailsComponent } from './message-details/message-details.component';
import {KweetService} from './services/kweet.service';
import { TagDetailsComponent } from './tag-details/tag-details.component';
import { KweetTypePipe } from './pipes/kweet-type.pipe';
import { KweetMessagePipe } from './pipes/kweet-message.pipe';


@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    LoginComponent,
    UserComponent,
    MessageDetailsComponent,
    TagDetailsComponent,
    KweetTypePipe,
    KweetMessagePipe
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
