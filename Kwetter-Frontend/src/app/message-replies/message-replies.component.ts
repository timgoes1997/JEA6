import {Component, Input, OnInit} from '@angular/core';
import {Kweet} from '../entities/Kweet';
import {RestService} from '../services/rest.service';
import {HttpResponse} from '@angular/common/http';
import {ErrorHandlingService} from '../services/error-handling.service';

const rel = 'replies';
const requestType = 'GET';

@Component({
  selector: 'app-message-replies',
  templateUrl: './message-replies.component.html',
  styleUrls: ['./message-replies.component.css']
})
export class MessageRepliesComponent implements OnInit {

  @Input()
  kweet: Kweet;

  kweets: Kweet[];

  constructor(private restService: RestService) {
  }

  ngOnInit() {
    if (!this.kweet) {
      return;
    }

    const uri = this.restService.getUri(this.kweet.links, rel, requestType);
    if (!uri) {
      console.log('no request type');
      return;
    }

    this.restService.getUriWithAuthHeaders<Kweet[]>(uri).subscribe(
      kweets => this.OnReceiveUserKweets(kweets)
    );
    console.log('executed delete');
  }

  OnReceiveUserKweets(response: HttpResponse<Kweet[]>) {
    if (response) {
      this.kweets = response.body;
      console.log('received kweet list');
    } else {
      console.log(`couldn't receive replies`);
    }
  }

  OnReceiveUserCreatedReply(kweet: Kweet) {
    if (kweet && this.kweets) {
      this.kweets.unshift(kweet);
    }
  }
}
