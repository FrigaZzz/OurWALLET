import { GoalInterface } from './Goal';
import { UserInterface } from './User';
import { TransactionInterface } from './Transaction';
import { GroupInterface, Group } from './Group';

export interface AccountInterface
{
    id: number,
    isEasyPay:boolean,    

    name: String,
    user:number,
    goal:number



}
export class Account implements AccountInterface{
    id: number
    isEasyPay:boolean    

    name: String
    user:number
    goal:number

    constructor (jsonObj={name:"",id:-1,goal:null,isEasyPay:false,user:null}){
        this.name=jsonObj.name
        this.isEasyPay=jsonObj.isEasyPay
        this.id=jsonObj.id
        this.goal=jsonObj.goal
        this.user=jsonObj.user
    }

}


