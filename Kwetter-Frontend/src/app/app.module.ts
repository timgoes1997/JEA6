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
import {MessageDetailsComponent} from './message-details/message-details.component';
import {KweetService} from './services/kweet.service';
import {TagDetailsComponent} from './tag-details/tag-details.component';
import {KweetTypePipe} from './pipes/kweet-type.pipe';
import {KweetMessagePipe} from './pipes/kweet-message.pipe';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { LogoutComponent } from './logout/logout.component';
import { MessageCreateComponent } from './message-create/message-create.component';
import {WebSocketService} from './services/web-socket.service';
import {ErrorHandlingService} from './services/error-handling.service';
import { MessageDeleteComponent } from './message-delete/message-delete.component';
import {RestService} from './services/rest.service';
import { MessageRepliesComponent } from './message-replies/message-replies.component';
import { MessageListComponent } from './message-list/message-list.component';
import { MessageReplyComponent } from './message-reply/message-reply.component';


@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    LoginComponent,
    UserComponent,
    MessageDetailsComponent,
    TagDetailsComponent,
    KweetTypePipe,
    KweetMessagePipe,
    NavBarComponent,
    LogoutComponent,
    MessageCreateComponent,
    MessageDeleteComponent,
    MessageRepliesComponent,
    MessageListComponent,
    MessageReplyComponent
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
    KweetService,
    WebSocketService,
    ErrorHandlingService,
    RestService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {

}
