import { TransactionInterface } from './Transaction';

export interface TransactionResult {
    resultLenght:number,
    expanses:number,
    earnings:number,
    totalExpanses:number,
    totalEarnings:number,
    balance:number,
    transactions:TransactionInterface[]
}