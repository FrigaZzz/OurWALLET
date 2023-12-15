import { Component, OnInit, OnChanges, Input, SimpleChanges, EventEmitter, Output } from '@angular/core';
import { AccountInterface } from 'src/app/types/Account';
import { CategoryInterface } from 'src/app/types/Category';
import { TransactionInterface, Transaction } from 'src/app/types/Transaction';
import { AccountService } from 'src/app/services/account.service';
import { TransactionService } from 'src/app/services/transaction.service';
import { CategoriesService } from 'src/app/services/categories.service';
import { BehaviorSubject ,  Observable ,  SubscriptionLike as ISubscription, merge, fromEvent, of, zip } from 'rxjs';
import { GroupsService } from 'src/app/services/groups.service';
import { GoalService } from 'src/app/services/goal.service';
import { SimpleAccount } from 'src/app/types/simple-account';

@Component({
  selector: 'app-transaction-modal',
  templateUrl: './transaction-modal.component.html',
  styleUrls: ['./transaction-modal.component.css']
})
export class TransactionModalComponent implements OnInit,OnChanges {
  @Input() newTransaction: TransactionInterface;

  @Output() change: EventEmitter<{a:number,b:String}> = new EventEmitter<{a:number,b:String}>();

  private accountSubs= new Array<ISubscription>();



  userAccounts:AccountInterface[]=new Array<AccountInterface>();
  allAccounts:AccountInterface[]=new Array<AccountInterface>();

  categories:CategoryInterface[]=new Array<CategoryInterface>();
  invalidAmount: boolean;
  
  account:number=0
  target_account:number=0
  category:string=""
  amount:number=0
  date:Date=new Date()
  isTransfer=false
  isDeposit=false


  constructor(
    private accountService: AccountService,
    private transactionService:TransactionService,
    private categoryService:CategoriesService,
    private groupService:GroupsService,
    private goalService: GoalService
    ){

      // devono anche essere presenti gli account dei goal, in modo da poter riusare questo stesso componente
      this.accountSubs.push(this.accountService.accounts$.subscribe(
        accounts=>{
          this.userAccounts=accounts.slice(0) 
          this.allAccounts=this.accountService.getUserAccounts().slice(0)
          this.userAccounts.forEach(
              acc=> this.allAccounts.push(acc)
          )
          this.userAccounts=this.allAccounts.slice(0) 

          this.goalService.goals$.subscribe(
            goals=>{
              zip(...goals.map(goal=> this.accountService.retrieveAccount(goal.account)))
                  .subscribe(data => {
                   
                    data.forEach(d => {
                      if(this.userAccounts.findIndex(a=>a.id==d.id)==-1){
                        this.userAccounts.push(d)
                        this.allAccounts.push(d)
                      }
                     
                    })
                  }, err => {
                    console.log(err)
                  });
              
                  //goals.forEach( g=>{
               // this.userAccounts.push(g.account)
              //})

            }
          )
          }
        )
      )
      this.accountSubs.push(this.accountService.familyGroupAccounts$.subscribe(
        accounts=>{
          this.allAccounts=accounts.slice(0)
          this.userAccounts= this.accountService.getUserAccounts().slice(0)
          this.userAccounts.forEach(
            acc=> this.allAccounts.push(acc)
          )

          this.userAccounts=this.allAccounts.slice(0) 

          this.goalService.goals$.subscribe(
            goals=>{
              zip(...goals.map(goal=> this.accountService.retrieveAccount(goal.account)))
                  .subscribe(data => {
                
                    data.forEach(d => {
                      if(this.userAccounts.findIndex(a=>a.id==d.id)==-1){
                        this.userAccounts.push(d)
                        this.allAccounts.push(d)
                      }
                     
                    })
                  }, err => {
                    console.log(err)
                  });
          
                })
        })
      )

      this.accountSubs.push(this.accountService.commonFundsAccount$.subscribe(
        accounts=>{
          this.allAccounts=accounts.slice(0)
          this.userAccounts= this.accountService.getUserAccounts().slice(0)
          this.userAccounts.forEach(
            acc=> this.allAccounts.push(acc)
          )

          this.userAccounts=this.allAccounts.slice(0) 

          this.goalService.goals$.subscribe(
            goals=>{
              zip(...goals.map(goal=> this.accountService.retrieveAccount(goal.account)))
                  .subscribe(data => {
                
                    data.forEach(d => {
                      if(this.userAccounts.findIndex(a=>a.id==d.id)==-1){
                        this.userAccounts.push(d)
                        this.allAccounts.push(d)
                      }
                     
                    })
                  }, err => {
                    console.log(err)
                  });
          
                })
        })
      )
      this.accountSubs.push(this.categoryService.categories.subscribe(
        categories=>this.categories=categories
        )
      )
  }
  ngOnInit() {

  }
  ngOnChanges(changes: SimpleChanges) {
    if (changes) {
      this.isTransfer=false
      this.isDeposit=false

      let transaction:TransactionInterface=Object.assign({},changes.newTransaction.currentValue)
      this.newTransaction = transaction
      this.newTransaction.amount = Math.abs(transaction.amount)

      if(this.newTransaction.opt!=null){
        console.log("eh")
        this.accountService.retrieveAccount(this.newTransaction.opt).subscribe((data:any)=>{

          let account=this.accountService.buildFromSimpleAccount(data)

          this.newTransaction.accountReceived=account
          this.isTransfer=true
        })
      }

      if(this.newTransaction.accountReceived!=null&&this.newTransaction.accountReceived.id!=-1)
          this.isTransfer=true
    }
  }

  abortNewTransaction(){
  }
  
  compareById(o1: AccountInterface, o2:AccountInterface) {
    if(o1==null||o2==null)return true
    return o1.id === o2.id
  }

  fun(){
    console.log(this.newTransaction)
  }
  
  addTransaction(){
     if(!this.checkTransfer()) return;

    
    if(this.isTransfer||!this.isDeposit){
      this.newTransaction.amount=Math.abs( this.newTransaction.amount)*-1
    }
    let data=Object.assign({},this.newTransaction)
    if(this.newTransaction.id!=-1) 
      return this.editTransaction(data)

    data.groupSender=this.groupService.getCurrentSelectedGroup()
    this.transactionService.commitTransaction( data).subscribe(
      result  =>{
        console.log(result)
        this.change.emit({a:2,b:data.category.name})
      },err=>this.change.emit({a:3,b:data.category.name})

    )
  }

  verify(value){
    var regexp =/^\d+(\.\d+)?$/
    
    if(!regexp.test(value))
    {
      this.newTransaction.amount=null
      this.invalidAmount=true
      return false
    }

    this.invalidAmount=false
    return true
  }

  editTransaction(data){
    this.transactionService.updateTransaction( data).subscribe(
      res=> {
        console.log(res)
        this.change.emit({a:1,b:data.category.name})
      },err=>{
        this.change.emit({a:3,b:data.category.name})

      }
    );
  }

  
  checkTransfer(){
    console.log(this.newTransaction)
    if(this.newTransaction.category.name=="Transfer" && 
        (this.newTransaction.accountReceived==this.newTransaction.accountSender||this.newTransaction.accountReceived==null||this.newTransaction.accountSender==null))
    {
      //this.newTransaction.accountReceived=this.newTransaction.accountSender
      alert("Only Transfers are allowed between 2 different accounts ")
      this.newTransaction.category=null
      console.log("sfsa")
      return false
    }
    return true
  }
  checkTransactionData(){
  }
  checkCategorySelection(value){
 
  }
  checkDeposit(){
  
  }
}
