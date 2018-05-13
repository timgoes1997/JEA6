import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Kweet} from '../entities/Kweet';
import {HttpParams, HttpResponse} from '@angular/common/http';
import {RestService} from '../services/rest.service';

const rel = 'removelike';
const requestType = 'DELETE';

@Component({
  selector: 'app-message-likes-dislike',
  templateUrl: './message-likes-dislike.component.html',
  styleUrls: ['./message-likes-dislike.component.css']
})
export class MessageLikesDislikeComponent implements OnInit {

  @Input()
  kweet: Kweet;

  @Output()
  onReceiveLike: EventEmitter<number> = new EventEmitter<number>();

  constructor(private restService: RestService) {
  }

  ngOnInit() {
  }

  OnDisLike() {
    if (!this.kweet) {
      return;
    }

    const uri = this.restService.getUri(this.kweet.links, rel, requestType);
    if (!uri) {
      console.log('no request type');
      return;
    }

    this.restService.deleteUriWithAuthHeaders<number>(uri).subscribe(
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
