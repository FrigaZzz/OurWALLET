import {  AccountInterface, Account } from './Account'
import { GroupInterface, Group } from './Group'
import { CategoryInterface, Category } from './Category'

export interface TransactionInterface
{
    id: number
    amount: number
    description:string
    date: Date
    groupSender?:GroupInterface
    accountReceived?: AccountInterface
    accountSender?: AccountInterface    
    category: CategoryInterface
    opt?:any
}
export class Transaction implements TransactionInterface{
    id: number
    amount: number
    date: Date
    description:string
    groupSender:GroupInterface
    accountReceived: AccountInterface
    accountSender: AccountInterface    
    category: CategoryInterface
    opt?:any

    constructor (jsonObject={id:-1,description:"",amount:null,groupSender:new Group(),accountReceived:new Account(),date:new Date(),accountSender:new Account(),category:new Category()}){
        this.id=jsonObject.id
        this.description=jsonObject.description
        this.amount=jsonObject.amount
        this.groupSender=jsonObject.groupSender
        this.accountReceived=jsonObject.accountReceived
        this.date=jsonObject.date
        this.accountSender=jsonObject.accountSender
        this.category=jsonObject.category
    }
}


