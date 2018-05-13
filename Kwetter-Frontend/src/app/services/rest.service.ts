import {Injectable} from '@angular/core';
import {AuthService} from './auth.service';
import {Kweet} from '../entities/Kweet';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class RestService {

  constructor(private http: HttpClient,
              private authSerivce: AuthService) {
  }

  getUriWithAuthHeaders<T>(uri: string): Observable<HttpResponse<T>> {
    const headers = this.authSerivce.getAuthHeaders();
    return this.http.get<T>(uri, {
      observe: 'response',
      headers: headers,
      withCredentials: true
    });
  }

  postUriWithAuthHeaders<T>(uri: string, body: HttpParams): Observable<HttpResponse<T>> {
    let headers = this.authSerivce.getAuthHeaders();
    headers = headers.append('Content-Type', 'application/x-www-form-urlencoded');
    return this.http.post<T>(uri, body, {
      observe: 'response',
      headers: headers,
      withCredentials: true
    });
  }

  deleteUriWithAuthHeaders<T>(uri: string): Observable<HttpResponse<T>> {
    let headers = this.authSerivce.getAuthHeaders();
    headers = headers.append('Content-Type', 'application/x-www-form-urlencoded');
    return this.http.delete<T>(uri, {
      observe: 'response',
      headers: headers,
      withCredentials: true
    });
  }
}
