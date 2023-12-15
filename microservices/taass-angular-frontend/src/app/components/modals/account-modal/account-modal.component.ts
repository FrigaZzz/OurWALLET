import { Component, OnInit, OnChanges, Input, SimpleChanges } from '@angular/core';
import { AccountInterface } from 'src/app/types/Account';
import { CategoryInterface } from 'src/app/types/Category';
import { TransactionInterface, Transaction } from 'src/app/types/Transaction';
import { AccountService } from 'src/app/services/account.service';
import { TransactionService } from 'src/app/services/transaction.service';
import { CategoriesService } from 'src/app/services/categories.service';
import { BehaviorSubject ,  Observable ,  SubscriptionLike as ISubscription, merge, fromEvent, of } from 'rxjs';
import { GroupInterface } from 'src/app/types/Group';
import { GroupsService } from 'src/app/services/groups.service';
@Component({
  selector: 'app-account-modal',
  templateUrl: './account-modal.component.html',
  styleUrls: ['./account-modal.component.css']
})
export class AccountModalComponent implements OnInit,OnChanges {
  @Input() newAccount: AccountInterface;
  private groupSubs= new Array<ISubscription>();

  constructor(
    private accountService: AccountService,
    ){
     
  }
  ngOnInit() {

  }
  ngOnChanges(changes: SimpleChanges) {
    if (changes) {
      this.newAccount = Object.assign({},changes.newAccount.currentValue)
    }
  }

  abortNewAccount(){
  }

  fun(){
  }
  
  addAccount(){
    if(this.newAccount.id!=-1) 
      return this.editAccount()
    let data=Object.assign({},this.newAccount)
    this.accountService.createAccount( data).subscribe(
      result  =>{
        console.log(result)
        
      }
    )
  }

  editAccount(){
    // let data=Object.assign({},this.newTransaction)
    // this.transactionService.updateTransaction( data).subscribe(
    //   res=> console.log(res)
    // );
  }



}
