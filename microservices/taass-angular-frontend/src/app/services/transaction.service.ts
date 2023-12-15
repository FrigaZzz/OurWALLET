import { Injectable } from '@angular/core';
import { TransactionInterface, Transaction } from '../types/Transaction';
import { HttpParams, HttpClient } from '@angular/common/http';
import { map, debounceTime, take } from 'rxjs/operators';
import { AccountInterface } from '../types/Account';
import { BehaviorSubject ,  Observable ,  SubscriptionLike as ISubscription, of } from 'rxjs';
import { AccountService } from './account.service';
import { BudgetService } from './budget.service';
import { CategoriesService } from './categories.service';
import { TransactionResult } from '../types/transaction-result';
import { GroupsService } from './groups.service';
import { GroupInterface } from '../types/Group';
import { CategoryInterface } from '../types/Category';

const API_PATH = '/api';
const DEBUG=false

interface TransactionResponse{
  id:number,
  description:string,
  amount:number,      
  accountSenderID:number,
  accountReceivedID:number,
  categoryID: number,
  groupSenderID:number,
  date: number
}

@Injectable({
  providedIn: 'root'
})
/**
 * Idee per l'implementazione:
 *  1. si mantiene una mappa "ID ACCOUNT" - "TRANSAZIONI" di ogni account dell'utente attuale ed una mappa anche per il gruppo selezionato
 *  2. si utilizzano gli oggetti Account salvati dentro ad AccountInterface e li si riempie ( quindi sia gli Account dell'utente sia gli Account del gruppo)
 *  3. si mantiene una sola mappa "ID ACCOUNT" - "TRANSAZIONI" che si carica in base al routing dell'utente (se visualizza un gruppo allora carichiamo quelle)
 *  4. si mantengono aggiornato solo gli oggetti Account attualemente utilizzati (dell'Account dell'utente oppure degli account del gruppo selezionato)
 *  Ovviamente 3 e 4 rendono meno pesante l'app, però 4 ci obbliga a toccare anche gli altri vettori ( cioè scaricare quello pieno) quindi 3 risulta più efficiente.
 *  Stesso ragionamento vale per 1 e 2, quindi 1 è più efficiente di 2.
 *  Inoltre se scegliamo 3, quando l'utente vuole effettuare un trasferimento dal proprio conto verso il gruppo, possiamo evitare di dover anche manipolare 
 *  ed aggiornare la mappa delle sue transazioni. Infatti quando l'utente tornerà a visualizzare la sua dashboard o i propri conti, Angular provvederà a 
 *  ricaricare le transazioni relative ai suoi conti. Sono infatti le transazioni gli oggetti "più pensati" per l'applicazione 
 *  (in base a quante sono "scaricate")
 * 
 *  NOTA: tutto questo ragionamento presuppone la possibilità di trasferire FONDI da un account personale ad un account gruppo. Quando però il gruppo visualizzato
 *  è un gruppo famiglia questo genere di trasferimento non dovrebbe essere possibile ( cioè è equivalente a ricaricare il proprio account personale siccome 
 *  il gruppo famiglia considera tutti gli account che partecipano)
 * 
 *  Di seguito implemento 3 
 */


export class TransactionService {

  /**
   * fake data gen flag
   */
  private init=true

  private transactionsIDs=0

  private transactionSub=new Array<ISubscription>();
  

  private _transactionsChangedSubject = new BehaviorSubject<any>(null);
  public transactionsChanged = this._transactionsChangedSubject.asObservable();

  private transactionsData=new Array<TransactionInterface>();

  /**
   * 
   * @param http 
   * @param accountService l'idea è che le transazioni sono legate alla rotta 
   */
  constructor(
    private http:HttpClient,
    private budgetService: BudgetService,
    private accountService: AccountService,
    private categoriesService:CategoriesService,
    private groupeService: GroupsService

    ) 
    { 

    }

  ngOnInit() {

  }



  /**
  * 
  * @param account        L'utente potrebbe volere solo le transazioni relative ad un conto, quindi carichiamo queste
  * @param param 
   *@param limit 
   *@param offset 
  *                       La query sul DB ovviamente prenderà tutte le transazioni del conto, e darà in output { TOT: # TOTALE RISULTATI, TRC: [ ... ] }
  *                       A seconda di come è paginata la tabella delle transazioni dobbiamo visualizzare e scaricare un tot di transazioni. 
  *                       Manteniamo di base un buffer di 50 transazioni per ogni conto.
  *                       Quando l'utente cambia paginazione verificheremo che il #transazioni è sufficiente ad essere visualizzato interamente nella pagina 
  *                       attuale della tabella, se però (#transazioniRimanenti % paginazione) !== 0 obblighiamo ad un fetch. 
  *                       Il fetch delle ulteriori transazioni avrà come offset il #transazioni_attuali e come limit (#transazioni_attuali + paginazione)
  *                       Ovviamente limit sarà MAX(TOTALE RISULTATI, (#transazioni_attuali + paginazione) ) oppure lasciamo questo compito al server 
  *                       (credo che mysql non si lamenta se LIMIT è tanto maggiore rispetto all'output)
  *                                
  */
  retrieveTransactions(filter:string="",active: string="date", direction: string="desc", pageIndex: number=0, pageSize: number=10,groupID:number=undefined,accountID:number=undefined): Observable<TransactionResult> {
    console.log("BANANEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE")
    console.log(filter)    
    
    if(groupID!=undefined && accountID!=undefined)
          return null

        const params = new HttpParams()
            .set('filters', filter.toString())
            .set('pageIndex', pageIndex.toString())
            .set('pageSize', pageSize.toString())
            .set('orderBy', active.toString())
            .set('direction', direction.toString())
       
        if(accountID!=undefined){
          //let url="/api/account/9/transactions?pageIndex=0&pageSize=10&orderBy=id&direction=asc"
          return this.http.get(API_PATH+ `/accounts/${accountID}/`+`transactions`, {params:params}).pipe(map(
              (res:{meta:any,body:{total:number,transactions:any}})=> {

                
                let transactionRes:any=res.body

                let myResult:TransactionResult={earnings:0,expanses:0,resultLenght:0,totalEarnings:0,totalExpanses:0,balance:0,transactions:[]};
                myResult.earnings=0
                myResult.expanses=0
                myResult.resultLenght=transactionRes.total
                myResult.totalEarnings=transactionRes.totalEarnings
                myResult.totalExpanses=transactionRes.totalExpanses
                myResult.balance=transactionRes.totalEarnings-transactionRes.totalExpanses

                myResult.transactions=transactionRes.transactions
                myResult.transactions.forEach(t=>t.date=new Date(t.date))
                /*new Array<Transaction>()
                console.log(d)*/
                console.log(res.body)

                return myResult
              }
            ));
      }
      else {
        const payload : TransactionResult= null//this.fakeGetTransacation(filter,active,direction,pageIndex,pageSize,groupID,accountID)
        return new BehaviorSubject(payload).pipe(debounceTime(1000))
     }
  }
  
 

  commitTransaction(transaction:TransactionInterface):Observable<TransactionInterface>{

    let saveTransaction: TransactionResponse= this.buildRequest(transaction)
  
    console.log(saveTransaction)
    return this.http.post(API_PATH+`/transactions`, saveTransaction).pipe(map(
      (data:any) => {
          let transaction:Transaction =data.body;
          this._transactionsChangedSubject.next(transaction)

          console.log(data)
          console.log('Transaction created');
          console.log( this._transactionsChangedSubject.getValue())

          return transaction;
      },err=>{
        console.log(err)
      }
   ));
   
  }

  
   updateTransaction(updateTransaction: TransactionInterface): Observable<TransactionInterface> {
    let saveTransaction: TransactionResponse= this.buildRequest(updateTransaction)
  
    console.log(saveTransaction)  
   return this.http.patch(API_PATH+`/transactions` + `/${updateTransaction.id}`, saveTransaction).pipe(map(
          (data:any) => {
              const updateTransaction = new Transaction(data.budget);
              this._transactionsChangedSubject.next(updateTransaction);
              console.log('Transaction updated');
              return updateTransaction;
          }
      ));
  }

  deleteTransaction(deleteTransaction: TransactionInterface): Observable<TransactionInterface> {
    console.log("call")
    return this.http.delete(API_PATH+"/transactions" + `/${deleteTransaction.id}`).pipe(map(
          (data:any) => {
            console.log("wee")
              this._transactionsChangedSubject.next(deleteTransaction);
              console.log('Transaction deleted');
              return data;
          }
      ));
  }



  private buildRequest(transaction:TransactionInterface): TransactionResponse{
    console.log(transaction)
    let converted: TransactionResponse= {
      id:transaction.id,
      description:transaction.description,
      amount:transaction.amount,
      accountSenderID:-1,
      accountReceivedID:-1,
      categoryID: transaction.category.id,
      groupSenderID:transaction.groupSender.id,
      date:transaction.date.getTime()
    }
    if(transaction.accountSender)
      converted.accountSenderID=transaction.accountSender.id
    if(transaction.accountReceived)
      converted.accountReceivedID=transaction.accountReceived.id

    return converted
  }

    /**HELPERS per testare senza SERVER */
  /***************************************************************************** */

  // populateTransactions(){
  //   let acounts=this.accountService.getSubscribedAccounts();
  //   if(this.init==true){
  //     this.init=false
  //     for (let i = 1; i <= 1500; i++) {
  //       let account=acounts[this.getRandomArrayIndex(acounts.length)]
  //       this.transactionsData.push(this.createNewTransaction(this.transactionsIDs++,account));
  //     }
  //   }
  // }

  // fakeGetTransacation(filter,active,direction,pageIndex,pageSize,groupID=undefined,accountID=undefined):TransactionResult{
  //   let filtertrc;
  //   let earnings=0
  //   let expanses=0
  //   let totalEarnings=0
  //   let totalExpanses=0

  //   if(groupID!=undefined){
  //     const accountsID:number[]=this.accountService.getSubscribedAccounts().filter(account=>account.user==groupID).map(account=>account.id)
  //     filtertrc=(trc:TransactionInterface)=>{
  //       if(accountsID.includes(trc.account.id)||accountsID.includes(trc.transfer_target_account.id))
  //       {
  //         totalExpanses+=trc.amount<0?trc.amount:0
  //         totalEarnings+=trc.amount>=0?trc.amount:0
  //         return true
  //       }
  //       return false
  //     }
  //   }
  //   if(accountID!=undefined)
  //     filtertrc=(trc:TransactionInterface)=>{
  //       try{
  //         if(trc.account.id==accountID||trc.transfer_target_account.id==accountID)
  //         {
  //           totalExpanses+=trc.amount<0?trc.amount:0
  //           totalEarnings+=trc.amount>=0?trc.amount:0
  //           return true
  //         }
  //       }
  //       catch(e){
  //        console.log(trc) 

  //       }
       
  //       return false
  //     }

  //   active = active || 'date';
  //   const cmp = (a, b) => (a[active] < b[active] ? -1 : 1);
  //   const rev = (a, b) => cmp(b, a);
    
   
  //   let transactions = [...this.transactionsData]
  //     .filter(filtertrc)
  //     .filter(trc=>{
  //         if(this.filterCheck(trc,filter)==true){
  //           expanses+=trc.amount<0?trc.amount:0
  //           earnings+=trc.amount>=0?trc.amount:0
  //           return true;
  //         }
  //         return false  
  //      })
  //     .sort(direction === 'desc' ?  cmp:rev)

  //   let resultLenght=transactions.length

  //   let [l, r] = [(pageIndex - 1) * pageSize, pageIndex * pageSize];
  //   transactions=transactions.slice(l,r)

  //   let payload:TransactionResult={resultLenght:resultLenght,earnings:earnings, expanses:expanses,totalEarnings:totalEarnings, totalExpanses:totalExpanses,transactions:transactions};
  //   console.log(payload)
  //   return payload
  // }

  // nestedFilterCheck(search, data, key) {
  //     if (typeof data[key] === 'object') {
  //       for (const k in data[key]) {
  //         if (data[key][k] !== null) {
  //           search = this.nestedFilterCheck(search, data[key], k);
  //         }
  //       }
  //     } else {
  //       search += data[key];
  //     }
  //     return search;
  //   }


  //  filterCheck(data, filter: string){
  //         const accumulator = (currentTerm, key) => {
  //           return this.nestedFilterCheck(currentTerm, data, key);
  //         };
  //         const dataStr = Object.keys(data).reduce(accumulator, '').toLowerCase();
  //         // Transform the filter by converting it to lowercase and removing whitespace.
  //         const transformedFilter = filter.trim().toLowerCase();
  //         return dataStr.indexOf(transformedFilter) !== -1;
  //   }

  // createNewTransaction(id: number,account): TransactionInterface {
  //   let categories=this.categoriesService.getCategories();
  //   const category =categories[this.getRandomArrayIndex(categories.length)]
  //   let date=new Date()
  //   date.setMonth(new Date(Date.now()).getMonth()-Math.random() * (15))
    
  //   const accounts=this.accountService.getSubscribedAccounts().filter(acc=>acc.id!=account.id)
  //   return {
  //     id: id,
  //     category: category,
  //     date: date.getTime(),
  //     amount: Math.round(Math.random() * (100)-Math.random()*150),
  //     description:"",
  //     transfer_target_account:account,
  //     account: accounts[this.getRandomArrayIndex(accounts.length)],
  //   };
  // }

  // private getRandomArrayIndex(length: number): number {
  //   return Math.round(Math.random() * (length - 1));
  // }


}
 


