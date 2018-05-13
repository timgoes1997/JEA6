import {Component, OnInit} from '@angular/core';
import {Kweet} from '../entities/Kweet';
import {KweetService} from '../services/kweet.service';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {MessageService} from '../services/message.service';
import {ActivatedRoute} from '@angular/router';
import {catchError, tap} from 'rxjs/operators';
import {HttpResponse} from '@angular/common/http';
import {ErrorHandlingService} from '../services/error-handling.service';

@Component({
  selector: 'app-tag-details',
  templateUrl: './tag-details.component.html',
  styleUrls: ['./tag-details.component.css']
})
export class TagDetailsComponent implements OnInit {

  kweets: Kweet[];

  name: string;

  constructor(public kweetService: KweetService,
              private errorHandlingService: ErrorHandlingService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.name = this.route.snapshot.paramMap.get('name');
    this.getTagKweets(this.name);
  }

  getTagKweets(name: string) {
    this.kweetService.getTagKweets(name).pipe(
      tap(_ => this.errorHandlingService.log(`fetched kweets for user named ${name}`)),
      catchError(this.errorHandlingService.handleError<HttpResponse<Kweet[]>>(`Erro on tag name=${name}`))
    ).subscribe(kweets => this.OnReceiveTagKweets(kweets));
  }

  OnReceiveTagKweets(response: HttpResponse<Kweet[]>) {
    if (response) {
      this.kweets = response.body;
      console.log('received kweet list');
    }
  }
}
