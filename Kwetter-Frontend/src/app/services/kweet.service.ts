import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {AuthService} from './auth.service';
import {Kweet} from '../entities/Kweet';

@Injectable()
export class KweetService {
  private userURL = 'http://localhost:8080/Kwetter/api/message';
  private tagURL = 'http://localhost:8080/Kwetter/api/tag';

  constructor(private http: HttpClient,
              private authService: AuthService) {
  }

  getKweet(name: string, id: number): Observable<any> {
    const requestURL = `${this.userURL}/user/${name}/${id}`;
    return this.http.get<Kweet>(requestURL, {observe: 'response'});
  }

  getKweets(name: string): Observable<any> {
    const requestURL = `${this.userURL}/user/${name}/messages`;
    return this.http.get<Kweet[]>(requestURL, {observe: 'response'});
  }

  getTagKweets(tagName: string): Observable<any> {
    const requestURL = `${this.tagURL}/${tagName}`;
    return this.http.get<Kweet[]>(requestURL, {observe: 'response'});
  }

  getKweetType(kweet: Kweet): string {
    switch (kweet.discriminator) {
      case 1:
        return 'Message';
      case 2:
        return 'Reply';
      case 3:
        return 'Remessage';
      default:
        return 'Unknown';
    }
  }

  // createKweetHTML
}
