import {  AccountInterface } from './Account'
import { TransactionInterface } from './Transaction'
import { GoalInterface } from './Goal'
import { UserInterface } from './User'
import { GroupInterface } from './Group'
import { CategoryInterface } from './Category'


export interface GoalDataInterface
{ 
    amount:number
    spent:number    
    goal:GoalInterface
}
export class GoalData implements GoalDataInterface{
    amount:number
    spent:number    
    goal:GoalInterface

    constructor() {
    
    }

    

}

