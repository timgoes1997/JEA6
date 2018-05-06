import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {KweetService} from '../services/kweet.service';

@Component({
  selector: 'app-message-create',
  templateUrl: './message-create.component.html',
  styleUrls: ['./message-create.component.css']
})
export class MessageCreateComponent implements OnInit {

  kweetForm: FormGroup;

  constructor(private fb: FormBuilder,
              private kweetService: KweetService) {
    this.createForm();
  }

  createForm() {
    this.kweetForm = this.fb.group({
      text: ''
    });
  }

  ngOnInit() {
  }

  OnKweet() {
    const text = this.kweetForm.controls.text.value;
    this.kweetService.createKweet(text);
  }
}
