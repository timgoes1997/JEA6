import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageReplyCountComponent } from './message-reply-count.component';

describe('MessageReplyCountComponent', () => {
  let component: MessageReplyCountComponent;
  let fixture: ComponentFixture<MessageReplyCountComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MessageReplyCountComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessageReplyCountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
