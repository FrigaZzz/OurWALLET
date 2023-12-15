import { TestBed } from '@angular/core/testing';

import { AccountResolverService } from './account-resolver.service';

describe('AccountResolverService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AccountResolverService = TestBed.get(AccountResolverService);
    expect(service).toBeTruthy();
  });
});
