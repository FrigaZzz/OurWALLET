import { AccountInterface } from './Account';
import { GroupInterface } from './Group';

export interface GoalInterface
{
    id: number
    name: string
    deadLine: Date
    startDate: Date
    amountRequested:number
    amountReached?:number
    account: number    
    group:number
    description:string
    status?:string
}
export class Goal implements GoalInterface{
    id: number
    name: string
    deadLine:Date
    startDate: Date
    amount:number
    account: number   
    amountReached?:number
    amountRequested: number    
    group:number
    description:string
    status?:string

    constructor (jsonObj:GoalInterface={
        id: -1,
        name: "",
        deadLine:new Date(),
        startDate:new Date(),
        amountRequested:100,
        amountReached:0,
        account: -1  ,  
        group:-1,
        description:"",
    }){
        this.id=jsonObj.id
        this.name=jsonObj.name
        this.deadLine=jsonObj.deadLine
        this.startDate=jsonObj.startDate

        this.amountRequested=jsonObj.amountRequested
        this.amountReached=jsonObj.amountReached

        this.account=jsonObj.account
        this.group=jsonObj.group
        this.description=jsonObj.description
        }
}



