import { SimpleAccount } from './simple-account';

export interface AccountsResponse {
    id:number,
     isFamilyGroup:boolean,
     name:string,
     accounts:Array<SimpleAccount>
}
