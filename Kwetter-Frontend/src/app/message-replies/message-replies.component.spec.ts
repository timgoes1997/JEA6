import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageRepliesComponent } from './message-replies.component';

describe('MessageRepliesComponent', () => {
  let component: MessageRepliesComponent;
  let fixture: ComponentFixture<MessageRepliesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MessageRepliesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessageRepliesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
