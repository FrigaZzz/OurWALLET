import {  AccountInterface } from './Account'
import { TransactionInterface } from './Transaction'
import { GoalInterface } from './Goal'
import { UserInterface } from './User'
import { GroupInterface } from './Group'
import { CategoryInterface } from './Category'


export interface BudgetDataInterface
{ 
    amount:number
    spent:number    
    budget:{
        name: string,
        id: number,
        amount:number
    }
}
export class BudgetData implements BudgetDataInterface{
    amount:number
    spent:number    
    budget:{
        name: string,
        id: number,
        amount:number
    }

    constructor() {
    
    }

    

}

