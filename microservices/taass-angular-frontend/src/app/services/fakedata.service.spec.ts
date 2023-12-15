import { TestBed } from '@angular/core/testing';

import { FakedataService } from './fakedata.service';

describe('FakedataService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FakedataService = TestBed.get(FakedataService);
    expect(service).toBeTruthy();
  });
});
