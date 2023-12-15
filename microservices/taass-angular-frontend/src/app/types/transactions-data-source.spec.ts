import { TransactionDataSource } from "./TransactionDataSource";

describe('TransactionsDataSource', () => {
  it('should create an instance', () => {
    expect(new TransactionDataSource(null)).toBeTruthy();
  });
});
