import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegistrationComponent} from '../registration/registration.component';
import {LoginComponent} from '../login/login.component';
import {UserComponent} from '../user/user.component';
import {MessageDetailsComponent} from '../message-details/message-details.component';
import {TagDetailsComponent} from '../tag-details/tag-details.component';
import {LogoutComponent} from '../logout/logout.component';

const routes: Routes = [
  {path: 'registration', component: RegistrationComponent},
  {path: 'login', component: LoginComponent},
  {path: 'logout', component: LogoutComponent},
  {path: 'user/:name', component: UserComponent},
  {path: 'user/:name/:message', component: MessageDetailsComponent},
  {path: 'tag/:name', component: TagDetailsComponent}
  // {path: '', redirectTo: '/dashboard', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
