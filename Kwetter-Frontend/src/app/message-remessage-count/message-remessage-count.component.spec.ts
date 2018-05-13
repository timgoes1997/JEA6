import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageRemessageCountComponent } from './message-remessage-count.component';

describe('MessageRemessageCountComponent', () => {
  let component: MessageRemessageCountComponent;
  let fixture: ComponentFixture<MessageRemessageCountComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MessageRemessageCountComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessageRemessageCountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
