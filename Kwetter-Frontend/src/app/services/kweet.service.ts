import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {AuthService} from './auth.service';
import {Kweet} from '../entities/Kweet';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {User} from '../entities/User';

const authHeaderKey = 'Authorization';

@Injectable()
export class KweetService {
  private messageURL = 'http://localhost:8080/Kwetter/api/message';
  private tagURL = 'http://localhost:8080/Kwetter/api/tag';

  public userCreateKweet: BehaviorSubject<Kweet> = new BehaviorSubject<Kweet>(null);
  public userDeletedKweet: BehaviorSubject<Kweet> = new BehaviorSubject<Kweet>(null);

  constructor(private http: HttpClient,
              private authService: AuthService) {
  }

  getKweet(name: string, id: number): Observable<any> {
    const requestURL = `${this.messageURL}/user/${name}/${id}`;
    return this.http.get<Kweet>(requestURL, {observe: 'response', headers: this.authService.getAuthHeaders()});
  }

  getKweets(name: string): Observable<any> {
    const requestURL = `${this.messageURL}/user/${name}/messages`;
    return this.http.get<Kweet[]>(requestURL, {observe: 'response', headers: this.authService.getAuthHeaders()});
  }

  getTagKweets(tagName: string): Observable<any> {
    const requestURL = `${this.tagURL}/${tagName}`;
    return this.http.get<Kweet[]>(requestURL, {observe: 'response', headers: this.authService.getAuthHeaders()});
  }

  deleteKweet(kweet: Kweet): boolean {
    const requestURL = `${this.messageURL}/${kweet.id}/remove`;
    let headers = this.authService.getAuthHeaders();
    headers = headers.append('Content-Type', 'application/x-www-form-urlencoded');
    if (!headers.has(authHeaderKey)) {
      return false;
    }
    this.http.delete<Kweet>(requestURL, {
      observe: 'response',
      headers: headers,
      withCredentials: true
    }).subscribe(value => this.kweetDelete(value));
    return true;

  }

  private kweetDelete(http: HttpResponse<any>) {
    console.log('received kweet delete');
    if (http.status === 200) {
      this.userDeletedKweet.next(http.body);
      console.log('updated current user kweet');
    }
  }


  createKweet(text: string): boolean {
    const requestURL = `${this.messageURL}/create`;
    let headers = this.authService.getAuthHeaders();
    headers = headers.append('Content-Type', 'application/x-www-form-urlencoded');
    if (!headers.has(authHeaderKey)) {
      return false;
    }
    const body = new HttpParams()
      .set('message', text)
      .set('messageType', 'Public');
    this.http.post<Kweet>(requestURL, body, {
      observe: 'response',
      headers: headers,
      withCredentials: true
    }).subscribe(value => this.kweetReceive(value));
  }

  private kweetReceive(http: HttpResponse<any>) {
    console.log('received kweet response');
    if (http.status === 200) {
      this.userCreateKweet.next(http.body);
      console.log('updated current user kweet');
    }
  }
  // createKweetHTML
}
