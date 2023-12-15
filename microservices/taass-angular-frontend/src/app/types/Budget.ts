import {  AccountInterface } from './Account'
import { TransactionInterface } from './Transaction'
import { GoalInterface } from './Goal'
import { UserInterface } from './User'
import { GroupInterface } from './Group'
import { CategoryInterface } from './Category'

export enum BudgetFrequencyEnum {
    WEEKLY = 'Weekly',
    MONTHLY = 'Monthly'
}
export type BudgetFrequency = BudgetFrequencyEnum.WEEKLY | BudgetFrequencyEnum.MONTHLY;

export interface BudgetInterface
{ 
    amount:number
    groupID:number    
    category: CategoryInterface
}
export class Budget implements BudgetInterface{
    amount:number
    groupID:number    
    category: CategoryInterface

    constructor(json: BudgetInterface={amount:0,groupID:-1,category:null}) {
       this.amount=json.amount
       this.groupID=json.groupID
       this.category=json.category
    }

    

}

