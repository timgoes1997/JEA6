import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageLikesComponent } from './message-likes.component';

describe('MessageLikesComponent', () => {
  let component: MessageLikesComponent;
  let fixture: ComponentFixture<MessageLikesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MessageLikesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessageLikesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
