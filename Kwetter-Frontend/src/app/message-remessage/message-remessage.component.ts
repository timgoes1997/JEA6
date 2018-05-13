import {Component, Input, OnInit} from '@angular/core';
import {Kweet} from '../entities/Kweet';
import {FormBuilder, FormGroup} from '@angular/forms';
import {RestService} from '../services/rest.service';
import {HttpParams, HttpResponse} from '@angular/common/http';
import {Router} from '@angular/router';

const rel = 'remessage';
const requestType = 'POST';

@Component({
  selector: 'app-message-remessage',
  templateUrl: './message-remessage.component.html',
  styleUrls: ['./message-remessage.component.css']
})
export class MessageRemessageComponent implements OnInit {

  @Input()
  kweet: Kweet;

  uri: string;

  kweetForm: FormGroup;

  constructor(private fb: FormBuilder,
              private restService: RestService,
              private router: Router) {
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

  private kweetReceive(http: HttpResponse<Kweet>) {
    console.log('received kweet response');
    if (http.status === 200) {
      this.router.navigateByUrl(`user/${http.body.messager.username}`);
      console.log('updated current user kweet');
    }
  }

  hasUri(): boolean {
    return this.restService.hasUri(this.kweet.links, rel, requestType);
  }
}
