import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Kweet} from '../entities/Kweet';
import {RestService} from '../services/rest.service';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpParams, HttpResponse} from '@angular/common/http';

const rel = 'reply';
const requestType = 'POST';

@Component({
  selector: 'app-message-reply',
  templateUrl: './message-reply.component.html',
  styleUrls: ['./message-reply.component.css']
})
export class MessageReplyComponent implements OnInit {

  @Input()
  kweet: Kweet;

  @Output()
  onUserReply: EventEmitter<Kweet> = new EventEmitter<Kweet>();

  uri: string;

  kweetForm: FormGroup;

  constructor(private fb: FormBuilder,
              private restService: RestService) {
    this.createForm();
  }

  createForm() {
    this.kweetForm = this.fb.group({
      text: ''
    });
  }

  ngOnInit() {
    if (!this.kweet) {
      return;
    }

    this.uri = this.restService.getUri(this.kweet.links, rel, requestType);
    if (!this.uri) {
      console.log('no request type');
      return;
    }
  }

  OnKweet() {
    const text = this.kweetForm.controls.text.value;
    const body = new HttpParams()
      .set('text', text);
    this.restService.postUriWithAuthHeaders<Kweet>(this.uri, body).subscribe(
      kweet => this.kweetReceive(kweet)
    );
  }

  private kweetReceive(http: HttpResponse<any>) {
    console.log('received kweet response');
    if (http.status === 200) {
      this.onUserReply.emit(http.body);
      console.log('updated current user kweet');
    }
  }

  hasUri(): boolean {
    return this.restService.hasUri(this.kweet.links, rel, requestType);
  }

}
