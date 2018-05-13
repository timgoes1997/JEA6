import {Component, Input, OnInit} from '@angular/core';
import {Kweet} from '../entities/Kweet';
import {RestService} from '../services/rest.service';
import {HttpResponse} from '@angular/common/http';

const rel = 'remessagescount';
const requestType = 'GET';

@Component({
  selector: 'app-message-remessage-count',
  templateUrl: './message-remessage-count.component.html',
  styleUrls: ['./message-remessage-count.component.css']
})
export class MessageRemessageCountComponent implements OnInit {

  @Input()
  kweet: Kweet;

  remessages: number;

  constructor(private restService: RestService) {

  }

  ngOnInit() {
    this.getRemessageCount();
  }

  getRemessageCount() {
    if (!this.kweet) {
      return;
    }

    const uri = this.restService.getUri(this.kweet.links, rel, requestType);
    if (!uri) {
      console.log('no request type');
      return;
    }

    this.restService.getUriWithAuthHeaders<number>(uri).subscribe(
      remessages => this.OnReceiveRemessagesCount(remessages)
    );
  }

  private OnReceiveRemessagesCount(remessages: HttpResponse<number>) {
    this.remessages = remessages.body;
  }
}
