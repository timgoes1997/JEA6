import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {AuthService} from './auth.service';
import {Kweet} from './entities/Kweet';

@Injectable()
export class KweetService {
  private userURL = 'http://localhost:8080/Kwetter/api/message';

  constructor(private http: HttpClient,
              private authService: AuthService) {
  }

  getKweet(name: string, id: number): Observable<any> {
    const requestURL = `${this.userURL}/user/${name}/${id}`;
    return this.http.get<Kweet>(requestURL, {observe: 'response'});
  }
}
