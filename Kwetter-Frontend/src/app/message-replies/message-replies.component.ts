import {Component, Input, OnInit} from '@angular/core';
import {Kweet} from '../entities/Kweet';

@Component({
  selector: 'app-message-replies',
  templateUrl: './message-replies.component.html',
  styleUrls: ['./message-replies.component.css']
})
export class MessageRepliesComponent implements OnInit {

  @Input()
  kweets: Kweet[];

  constructor() { }

  ngOnInit() {
  }

}
