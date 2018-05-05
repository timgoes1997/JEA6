import {Component, OnInit} from '@angular/core';
import {Kweet} from '../entities/Kweet';
import {KweetService} from '../kweet.service';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {MessageService} from '../message.service';
import {ActivatedRoute} from '@angular/router';
import {catchError, tap} from 'rxjs/operators';
import {HttpResponse} from '@angular/common/http';

@Component({
  selector: 'app-tag-details',
  templateUrl: './tag-details.component.html',
  styleUrls: ['./tag-details.component.css']
})
export class TagDetailsComponent implements OnInit {

  kweets: Kweet[];

  constructor(private kweetService: KweetService,
              private messageService: MessageService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    const name = this.route.snapshot.paramMap.get('name');
    this.getTagKweets(name);
  }

  getTagKweets(name: string) {
    this.kweetService.getTagKweets(name).pipe(
      tap(_ => this.log(`fetched kweets for user named ${name}`)),
      catchError(this.handleError<HttpResponse<Kweet[]>>(`getHero name=${name}`))
    ).subscribe(kweets => this.OnReceiveTagKweets(kweets));
  }

  OnReceiveTagKweets(response: HttpResponse<Kweet[]>) {
    if (response) {
      this.kweets = response.body;
      console.log('received kweet list');
    }
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: handling error request
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  private log(message: string) {
    this.messageService.add('HeroService: ' + message);
  }
}
