import {Component, Input, OnInit} from '@angular/core';
import {Kweet} from '../entities/Kweet';
import {RestService} from '../services/rest.service';
import {HttpResponse} from '@angular/common/http';

const rel = 'repliescount';
const requestType = 'GET';

@Component({
  selector: 'app-message-reply-count',
  templateUrl: './message-reply-count.component.html',
  styleUrls: ['./message-reply-count.component.css']
})
export class MessageReplyCountComponent implements OnInit {

  @Input()
  kweet: Kweet;

  replies: number;

  constructor(private restService: RestService) {
  }

  ngOnInit() {
    this.getReplyCount();
  }

  getReplyCount() {
    if (!this.kweet) {
      return;
    }

    const uri = this.restService.getUri(this.kweet.links, rel, requestType);
    if (!uri) {
      console.log('no request type');
      return;
    }

    this.restService.getUriWithAuthHeaders<number>(uri).subscribe(
      replies => this.OnReceiveRepliesCount(replies)
    );
  }

  private OnReceiveRepliesCount(replies: HttpResponse<number>) {
    this.replies = replies.body;
  }
}
