import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {User} from '../entities/User';

const headers = new HttpHeaders({
  'Content-Type': 'application/x-www-form-urlencoded'
});

@Injectable()
export class UserService {
  private userURL = 'http://localhost:8080/Kwetter/api/user';

  constructor(private http: HttpClient) {
  }

  getUser(name: string): Observable<any> {
    const requestURL = `${this.userURL}/${name}`;
    return this.http.get<User>(requestURL, {observe: 'response'});
  }
}
