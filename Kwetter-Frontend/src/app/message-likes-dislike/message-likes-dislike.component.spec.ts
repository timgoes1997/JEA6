import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageLikesDislikeComponent } from './message-likes-dislike.component';

describe('MessageLikesDislikeComponent', () => {
  let component: MessageLikesDislikeComponent;
  let fixture: ComponentFixture<MessageLikesDislikeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MessageLikesDislikeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessageLikesDislikeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
