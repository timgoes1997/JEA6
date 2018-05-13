import {Component, Input, OnInit} from '@angular/core';
import {Kweet} from '../entities/Kweet';
import {RestService} from '../services/rest.service';
import {HttpResponse} from '@angular/common/http';

const rel = 'likes';
const relHas = 'hasliked';
const requestType = 'GET';

@Component({
  selector: 'app-message-likes',
  templateUrl: './message-likes.component.html',
  styleUrls: ['./message-likes.component.css']
})
export class MessageLikesComponent implements OnInit {

  @Input()
  kweet: Kweet;

  likes: number;
  hasLikedUri = false;
  hasLiked: boolean;

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

    this.restService.getUriWithAuthHeaders<number>(uri).subscribe(
      likes => this.OnReceiveKweetLikes(likes)
    );
    console.log('executed delete');

    const hasUri = this.restService.getUri(this.kweet.links, relHas, requestType);
    if (!hasUri) {
      console.log('no hasUri');
      return;
    }

    this.hasLikedUri = true;
    this.restService.getUriWithAuthHeaders<boolean>(hasUri).subscribe(
      hasLiked => this.OnReceiveHasLikedKweet(hasLiked)
    );
  }

  private OnReceiveKweetLikes(http: HttpResponse<any>) {
    console.log('received kweet likes');
    if (http.status === 200) {
      this.likes = http.body.amount;
    }
  }

  private OnReceiveHasLikedKweet(http: HttpResponse<any>) {
    console.log('received has liked kweet');
    if (http.status === 200) {
      this.hasLiked = http.body;
    }
  }

  private OnUpdateLikes(likes: number) {
    this.hasLiked = !this.hasLiked;
    this.likes = likes;
  }
}
