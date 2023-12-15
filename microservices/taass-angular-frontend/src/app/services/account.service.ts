import { Injectable, SimpleChanges, ɵConsole } from '@angular/core';
import { BehaviorSubject ,  Observable ,  SubscriptionLike as ISubscription, observable, from, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { AccountInterface, Account } from '../types/Account';
import { map, filter } from 'rxjs/operators';
import { CategoriesService } from './categories.service';
import { CategoryInterface } from '../types/Category';
import { GroupsService } from './groups.service';
import { TransactionService } from './transaction.service';
import { TransactionInterface } from '../types/Transaction';
import { AccountsResponse } from '../types/accounts-response';
import { SimpleAccount } from '../types/simple-account';
import { User } from '../types/User';
import { AuthService } from './AuthService/auth-service.service';

const API_PATH = '/api';
const DEBUG=false

@Injectable({
  providedIn: 'root'
})
/**
 *  Dipendenza dal current Group selezionato
 *  NOTA: se poi si trova noiosa come soluzione, magari manteniamo sempre in cache i dati relativi alle transazioni dell'utente (gruppo personale)
 *        così da poterci riaccedere sempre  (senza dover ri richiedere i dati al server)
 *  Io ho pensato che se l'utente sta visualizzando i dati relativi ad un gruppo a cui è sottoscritto, quindi sarà in un path del tipo
 *  /groups/:id/accounts/ , si potrebbe dire che attualmente il gruppo da considerare è quello e quindi sono fetchati i dati relativi agli account collegati
 *  Una volta che l'utente torna alla home o comunque vuole tornare a visualizzare i suoi dati personali, Angular provvede a risettare il gruppo corretto
 *  
 *  EDIT: l'implementazione attuale prevede di mantenere sempre la lista dei conti dell'utente autenticato e la lista dei conti del gruppo che sta visualizzando
 *  (ovviamente la lista avrà solo un elemento se il conto visualizzato è "cassa comune" mentre potrà avere più elementi nel caso di gruppi famiglia,
 *   quindi uno per ogni componente come abbiamo stabilito)
 *  L'idea è quindi rilasciare e riempire questo _subscribedAccountSubject ogni volta che viene selezionato un nuovo gruppo nella sezione groups
 * @_accountSubject conti dell'utente
 * @_subscribedAccountSubject conti del gruppo selezionato
 */

export class AccountService {
  private accountIDs=0;

  private subscriptions = new Array<ISubscription>();

  // all family  accounts (list of family member accounts)
  private _familyGroupAccounts = new BehaviorSubject<Array<AccountInterface>>(new Array<AccountInterface>());

  // all user accounts ( private )
  private _accountSubject = new BehaviorSubject<Array<AccountInterface>>(new Array<AccountInterface>());
  
  // all selcted group account ( list common group member accounts)
  private _commonFundsAccount = new BehaviorSubject<Array<AccountInterface>>(new Array<AccountInterface>());


  public familyGroupAccounts$ = this._familyGroupAccounts.asObservable();
  public accounts$ = this._accountSubject.asObservable();
  public commonFundsAccount$ = this._commonFundsAccount.asObservable();

  constructor(
    private http: HttpClient,
    private groupService: GroupsService,
    private auth:AuthService
  ) 
  {

    // USER changes so we update familyGroupAccounts accounts and accounts
    this.subscriptions.push(this.groupService.currentGroup
      .subscribe(
        (curr:any) =>{

          if(curr==null|| curr==undefined|| curr.id==-1)return
          
          let userAcc=new Array<AccountInterface>()
          let familyMembersAcc=new Array<AccountInterface>()
          let commonFundsAccount=new Array<AccountInterface>()

          curr.accounts.forEach((account:SimpleAccount) => {

            if(account.user===this.auth.getCurrentUser().id)
            userAcc.push(this.buildFromSimpleAccount(account))
            else
            familyMembersAcc.push(this.buildFromSimpleAccount(account))
           })

           this._accountSubject.next(userAcc);  

           
           this._familyGroupAccounts.next(familyMembersAcc); 

           this._commonFundsAccount.next([]);  

        }
      ));
    
    // USER selects a common group so we update  commonFundsAccount
    this.subscriptions.push(this.groupService.currentSelectedGroup
          .subscribe(
            (curr:any) =>{  
              if(curr==null|| curr==undefined|| curr.id==-1)return

              if(curr.isFamilyGroup==true){
                //no need to update gui
                let userAcc=new Array<AccountInterface>()
                let familyMembersAcc=new Array<AccountInterface>()
                let commonFundsAccount=new Array<AccountInterface>()
      
                curr.accounts.forEach((account:SimpleAccount) => {
      
                  if(account.user===this.auth.getCurrentUser().id)
                  userAcc.push(this.buildFromSimpleAccount(account))
                  else
                  familyMembersAcc.push(this.buildFromSimpleAccount(account))
                 })

                 this._accountSubject.next(userAcc);  

                 this._familyGroupAccounts.next(familyMembersAcc); 

                 this._commonFundsAccount.next([]);  
      
              }
              else{
                let commonMembersAcc=[]
                curr.accounts.forEach((account:SimpleAccount) => {
                  commonMembersAcc.push(this.buildFromSimpleAccount(account))
                })
                this._commonFundsAccount.next(commonMembersAcc);  
              }
              
      
            }
          ));
  }


  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => {
        sub.unsubscribe();
    });
  }

  // we look for account of the selected group ONLY
  // When selected group changes, a new get will be triggered
  retrieveAccounts(currGroupID?:number,isInit?: boolean): Observable<Array<AccountInterface>> {
      if(DEBUG){
        const arr = new Array<AccountInterface>();
        return of(arr)
      }
      // this will output a Group containing a vector of account IDS
      else return this.http.get(API_PATH+"/group/"+currGroupID+"/accounts").pipe(
          map(
          (data:{meta:any, body: Array<AccountsResponse>}) => {
              console.log(data)
              let groupedAccounts=data.body;
              if(groupedAccounts.length==0)return []
              let group:AccountsResponse=groupedAccounts[0]
              const arr = new Array<AccountInterface>();
              group.accounts.forEach((account:SimpleAccount) => {
                  arr.push(this.buildFromSimpleAccount(account))
              });
              return arr;
          },
          err => console.log(err)
      ));
    /*} 
    else 
      return this.accounts;*/
  }



  retrieveAccount(accountID:number):Observable<AccountInterface>{
      return this.http.get(API_PATH+"/accounts/"+accountID).pipe(
        map(
        (data:any) => {
            let acc=data.body
            let account=new Account();
            account.name=acc.name
            account.id=acc.id
            // let x=new User()
            // x.id =acc.user
            account.user=acc.user           
            account.isEasyPay=acc.isEasyPay
            return account
        },
        err => console.log(err)
    ))
  }
  
  createAccount(saveAccount: AccountInterface): Observable<AccountInterface> {
    if (DEBUG) {     
        saveAccount.id=this.accountIDs++   
        const arr = this._accountSubject.value;
        arr.push(saveAccount);
        this._accountSubject.next(arr);
        console.log('Account created');
        return of(saveAccount);

    }else return this.http.post(API_PATH+`/accounts`, saveAccount).pipe(map(
          (data:any) => {
              const account = new Account(data.body);
              const arr = this._accountSubject.value;
              arr.push(account);
              this._accountSubject.next(arr);
              console.log('Account created');
              return account;
          }
      ));
  }


  deleteAccount(deleteAccount: AccountInterface): Observable<AccountInterface> {
    if (DEBUG) {     
        return null;

    }else return this.http.delete(API_PATH+`/accounts/${deleteAccount.id}`).pipe(map(
          (data:any) => {
              const arr = this._accountSubject.value;
              const index = arr.findIndex(b => b.id === deleteAccount.id);
              arr.splice(index, 1);
              this._accountSubject.next(arr);
              console.log('Account deleted');
              return data;
          }
      ));
  }

  buildFromSimpleAccount(account:SimpleAccount){
      let acc=new Account()
      acc.id=account.id
      acc.isEasyPay=account.isEasyPay
      acc.name=account.name
      // let x=new User()
      // x.id=account.user
      acc.user=account.user
      acc.goal=account.goal

      return acc;
  }

  /**
   * 
   * TO DO UPDATE e DELETE
   */


  // updateAccount(updateAccount: AccountInterface): Observable<AccountInterface> {
  //   if (DEBUG) {     
  //       const arr = this._accountSubject.value;
  //       const index = arr.findIndex(b => b.id === updateAccount.id);
  //       arr[index] = updateAccount;
  //       this._accountSubject.next(arr);
  //       console.log('Account updated');
  //       return of(updateAccount);
  //   }else return this.http.put(API_PATH+`/${updateAccount.group.id}/accounts` + `/${updateAccount.id}`, updateAccount).pipe(map(
  //         (data:any) => {
  //             const account = new Account(data.account);
  //             const arr = this._accountSubject.value;
  //             const index = arr.findIndex(b => b.id === updateAccount.id);
  //             arr[index] = account;
  //             this._accountSubject.next(arr);
  //             console.log('Account updated');
  //             return account;
  //         }
  //     ));
  // }

  // deleteAccount(deleteAccount: AccountInterface): Observable<AccountInterface> {
  //   if (DEBUG) {     
  //       const arr = this._accountSubject.value;
  //       const index = arr.findIndex(b => b.id === deleteAccount.id);
  //       arr.splice(index, 1);
  //       this._accountSubject.next(arr);
  //       console.log('Account deleted');
  //       return of(deleteAccount);

  //   }else return this.http.delete(API_PATH+`/${deleteAccount.group.id}/accounts` + `/${deleteAccount.id}`).pipe(map(
  //         (data:any) => {
  //             const arr = this._accountSubject.value;
  //             const index = arr.findIndex(b => b.id === deleteAccount.id);
  //             arr.splice(index, 1);
  //             this._accountSubject.next(arr);
  //             console.log('Account deleted');
  //             return data;
  //         }
  //     ));
  // }

  getFamilyGroupAccounts(): AccountInterface[]{
    let acc=this._familyGroupAccounts.value;
    return acc==null?[]:acc 
  }
  
  getUserAccounts(): AccountInterface[]{
    let acc=this._accountSubject.value;
    return acc==null?[]:acc 

  }
  getCommonGroupAccounts(): AccountInterface[]{
    let acc=this._commonFundsAccount.value;
    return acc==null?[]:acc 
  }

  findAccountByID(accountID):AccountInterface{
    let account=this._accountSubject.value.find(el=>el.id==accountID)
    if(account==undefined)
      account=this._commonFundsAccount.value.find(el=>el.id==accountID)
    if(account==undefined)
      account=this._familyGroupAccounts.value.find(el=>el.id==accountID)

    return account
  }

}
