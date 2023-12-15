import { GroupInterface } from './Group';
import { AccountInterface } from './Account';

export interface UserInterface
{
    id?:number
    email?: String,
    username: String,
    password?: String,
    payer?:boolean,
    familyGroup?:GroupInterface
    accounts?:Array<AccountInterface>
    commonFundGroups?:Array<GroupInterface>

}
export class User implements UserInterface{
    
    id?:number
    email?: String
    username: String
    password?: String
    payer?:boolean
    familyGroup?:GroupInterface
    accounts?:Array<AccountInterface>
    commonFundGroups?:Array<GroupInterface>

    constructor (jsonObject={id:0,email:"",username:"",password:null,payer:false,familyGroup:null,accounts:null,commonFundGroups:null}){
        this.username=jsonObject.username;
        this.email = jsonObject.email
        this.password=jsonObject.password
        this.payer=jsonObject.payer
        this.familyGroup=jsonObject.familyGroup
        this.accounts=jsonObject.accounts
        this.commonFundGroups=jsonObject.commonFundGroups

    }
}


