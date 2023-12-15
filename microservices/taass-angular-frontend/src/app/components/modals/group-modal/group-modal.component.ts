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
  selector: 'app-group-modal',
  templateUrl: './group-modal.component.html',
  styleUrls: ['./group-modal.component.css']
})
export class GroupModalComponent implements OnInit,OnChanges {
  @Input() newGroup: GroupInterface;
  private groupSubs= new Array<ISubscription>();

  constructor(
    private groupService: GroupsService,
    ){
     
  }
  ngOnInit() {

  }
  ngOnChanges(changes: SimpleChanges) {
    if (changes) {
      this.newGroup = Object.assign({},changes.newGroup.currentValue)
    }
  }

  abortNewGroup(){
  }

  fun(){
  }
  
  addGroup(){
    if(this.newGroup.id!=-1) 
      return this.editGroup()
    let data=Object.assign({},this.newGroup)
    this.groupService.createGroup( data).subscribe(
      result  =>{
        console.log(result)
      }
    )
  }

  editGroup(){
    // let data=Object.assign({},this.newTransaction)
    // this.transactionService.updateTransaction( data).subscribe(
    //   res=> console.log(res)
    // );
  }



}
