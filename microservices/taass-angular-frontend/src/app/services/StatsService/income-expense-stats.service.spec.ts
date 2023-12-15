import { TestBed } from '@angular/core/testing';

import { IncomeExpenseStatsService } from './income-expense-stats.service';

describe('IncomeExpenseStatsServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: IncomeExpenseStatsService = TestBed.get(IncomeExpenseStatsService);
    expect(service).toBeTruthy();
  });
});
