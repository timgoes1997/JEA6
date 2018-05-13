import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageRemessageComponent } from './message-remessage.component';

describe('MessageRemessageComponent', () => {
  let component: MessageRemessageComponent;
  let fixture: ComponentFixture<MessageRemessageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MessageRemessageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessageRemessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
