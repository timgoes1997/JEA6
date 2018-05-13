import {Component, Input, OnInit} from '@angular/core';
import {Kweet} from '../entities/Kweet';

@Component({
  selector: 'app-message-list',
  templateUrl: './message-list.component.html',
  styleUrls: ['./message-list.component.css']
})
export class MessageListComponent implements OnInit {

  @Input()
  kweets: Kweet[];

  constructor() { }

  ngOnInit() {
  }

  OnReceiveUserKweetDelete(kweet: Kweet) {
    if (kweet && this.kweets) {
      for (const i in this.kweets) {
        if (this.kweets[i].id === kweet.id) {
          this.kweets.splice(+i, 1);
        }
      }
    }
  }
}
