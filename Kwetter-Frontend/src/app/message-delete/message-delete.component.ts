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

    const uri = this.hasDeleteURI();
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

  /*
  private receiveDeleteKweet(kweet: Kweet) {
    if (kweet && this.kweet && this.kweet.id === kweet.id) {
      const name = this.route.snapshot.paramMap.get('name');
      this.router.navigateByUrl(`user/${name}`);
      location.reload();
    }
  }*/

  hasUri(): boolean {
    return this.hasDeleteURI() != null;
  }

  hasDeleteURI(): string {
    if (!this.kweet) { return null; }

    for (const link of  this.kweet.links) {
      if (link.rel === rel && link.requestType === requestType) {
        return link.link;
      }
    }
    return null;
  }
}
