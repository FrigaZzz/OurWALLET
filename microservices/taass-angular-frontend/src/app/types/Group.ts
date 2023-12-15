import {  AccountInterface } from './Account'
import { TransactionInterface } from './Transaction'
import { GoalInterface } from './Goal'
import { UserInterface } from './User'
import { BudgetInterface } from './Budget'

export interface GroupInterface
{
    id: number
    isFamilyGroup:boolean
    name: String
    description:String
    transactions?:Array<TransactionInterface>
    goals?:Array<GoalInterface>
    budget?:Array<BudgetInterface>
    members?:Array<UserInterface>
    accounts?: Array<AccountInterface>    
   
}
export class Group implements GroupInterface{
    id: number
    isFamilyGroup:boolean
    transactions?:Array<TransactionInterface>
    goals?:Array<GoalInterface>
    budget?:Array<BudgetInterface>
    members?:Array<UserInterface>
    accounts?: Array<AccountInterface>        
    name: String
    description:String


    constructor (jsonObj:GroupInterface={id:-1,isFamilyGroup:false,name:"",description:"",transactions:[],goals:[],budget:[],members:[],accounts:[]}){
        this.id=jsonObj.id 
        this.isFamilyGroup=jsonObj.isFamilyGroup 
        this.transactions=jsonObj.transactions
        this.goals=jsonObj.goals
        this.budget=jsonObj.budget
        this.members=jsonObj.members
        this.accounts=jsonObj.accounts
        this.name=jsonObj.name
        this.description=jsonObj.description

    }
}


