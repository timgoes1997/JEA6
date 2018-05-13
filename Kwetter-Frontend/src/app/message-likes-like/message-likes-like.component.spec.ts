import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageLikesLikeComponent } from './message-likes-like.component';

describe('MessageLikesLikeComponent', () => {
  let component: MessageLikesLikeComponent;
  let fixture: ComponentFixture<MessageLikesLikeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MessageLikesLikeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessageLikesLikeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
