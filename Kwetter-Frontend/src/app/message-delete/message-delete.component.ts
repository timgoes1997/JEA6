import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {RestService} from '../services/rest.service';
import {Kweet} from '../entities/Kweet';
import {Link} from '../entities/Link';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';

const rel = 'remove';
const requestType = 'DELETE';

@Component({
  selector: 'app-message-delete',
  templateUrl: './message-delete.component.html',
  styleUrls: ['./message-delete.component.css']
})
export class MessageDeleteComponent implements OnInit {

  @Input()
  kweet: Kweet;

  @Output()
  onMessageDeleted: EventEmitter<Kweet> = new EventEmitter<Kweet>();

  constructor(private restService: RestService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit() {
  }

  Delete() {
    if (!this.kweet) {
      return;
    }

    const uri = this.restService.getUri(this.kweet.links, rel, requestType);
    if (!uri) {
      console.log('no request type');
      return;
    }

    this.restService.deleteUriWithAuthHeaders(uri).subscribe(
      kweet => this.receiveDeleteResponse(kweet)
    );
    console.log('executed delete');
  }

  private receiveDeleteResponse(http: HttpResponse<any>) {
    console.log('received kweet delete');
    if (http.status === 200) {
      this.onMessageDeleted.emit(http.body);
      console.log('updated current user kweet');
    }
  }

  hasUri(): boolean {
    return this.restService.hasUri(this.kweet.links, rel, requestType);
  }
}
