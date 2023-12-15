import { TestBed } from '@angular/core/testing';

import { RequestSettingsService } from './request-settings.service';

describe('RequestSettingsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RequestSettingsService = TestBed.get(RequestSettingsService);
    expect(service).toBeTruthy();
  });
});
