import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Kweet} from '../entities/Kweet';
import {RestService} from '../services/rest.service';
import {HttpParams, HttpResponse} from '@angular/common/http';

const rel = 'addLike';
const requestType = 'POST';

@Component({
  selector: 'app-message-likes-like',
  templateUrl: './message-likes-like.component.html',
  styleUrls: ['./message-likes-like.component.css']
})
export class MessageLikesLikeComponent implements OnInit {

  @Input()
  kweet: Kweet;

  @Output()
  onReceiveLike: EventEmitter<number> = new EventEmitter<number>();

  constructor(private restService: RestService) {
  }

  ngOnInit() {
  }

  OnLike() {
    if (!this.kweet) {
      return;
    }

    const uri = this.restService.getUri(this.kweet.links, rel, requestType);
    if (!uri) {
      console.log('no request type');
      return;
    }

    this.restService.postUriWithAuthHeaders<number>(uri, new HttpParams()).subscribe(
      likes => this.OnReceiveLike(likes)
    );
  }


  private OnReceiveLike(http: HttpResponse<any>) {
    console.log('received like');
    if (http.status === 200) {
      this.onReceiveLike.emit(http.body.amount);
    }
  }
}
