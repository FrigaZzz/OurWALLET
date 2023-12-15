import { Injectable, Output, EventEmitter } from '@angular/core';
import { TransactionInterface } from '../types/Transaction';

@Injectable()
export class FakedataService {
  @Output() transactionsChanged = new EventEmitter<boolean>(true);

  id=100;

  private transactions=[]

  private  categories = [
    'Food',
    'Gas',
    'Tech',
    'Hotel',
    'Cinema',
    'Opera',
    'Plane',
    'Train',
    'Uber',
    'Smoke',
    'Transfer',
    'Deposit'
  ];
  private  accounts = [
   'VISA',
   'PAYPAL',
  ];

  constructor() {}

  // createNewTransaction(id: number): TransactionInterface {
  //   const category =
  //     this.categories[this.getRandomArrayIndex(this.categories.length)]

  //   return {
  //     id: id,
  //     category: category,
  //     date: new Date(),
  //     amount: Math.round(Math.random() * (100)-Math.random()*150),
  //     account: this.accounts[this.getRandomArrayIndex(this.accounts.length)]
  //   };
  // }

  // create100Transactions(): TransactionInterface[] {
  //   const transactions: TransactionInterface[] = [];
  //   for (let i = 1; i <= 100; i++) {
  //     transactions.push(this.createNewTransaction(i));
  //   }
  //   console.log(transactions)

  //   this.transactions=transactions
  //   return transactions;
  // }

  // getCategories(){
  //   return this.categories;
  // }

  // addTransaction(transaction: TransactionInterface){
  //   transaction.id=++this.id
  //   this.transactions.push(transaction)
  //   this.transactionsChanged.emit(true);
  // }

  // getTransactions(){
  //   return this.transactions;
  // }
  // private getRandomArrayIndex(length: number): number {
  //   return Math.round(Math.random() * (length - 1));
  // }
}
