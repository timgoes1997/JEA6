import { TestBed, inject } from '@angular/core/testing';

import { KweetService } from './kweet.service';

describe('KweetService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [KweetService]
    });
  });

  it('should be created', inject([KweetService], (service: KweetService) => {
    expect(service).toBeTruthy();
  }));
});
