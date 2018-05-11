import {Injectable} from '@angular/core';

import * as socketIo from 'socket.io-client';
import {Observable} from 'rxjs/Observable';
import {Event} from '../enums/event.enum';

const serverUrl = 'http://localhost:8080';

@Injectable()
export class WebSocketService {
  private socket;

  constructor() {
  }

  public initSocket(url: any): void {
    this.socket = socketIo(url);
  }

  public send(test: String) {
    this.socket.send(test);
  }

  public onEvent(event: Event): Observable<Event> {
    return new Observable<Event>(observer => {
      this.socket.on(event, () => observer.next());
    });
  }
}
