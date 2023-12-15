import { Component, OnInit, OnChanges, Input, SimpleChanges } from '@angular/core';
import { AccountInterface } from 'src/app/types/Account';
import { CategoryInterface } from 'src/app/types/Category';
import { AccountService } from 'src/app/services/account.service';
import { CategoriesService } from 'src/app/services/categories.service';
import { BehaviorSubject ,  Observable ,  SubscriptionLike as ISubscription, merge, fromEvent, of } from 'rxjs';
import { GroupsService } from 'src/app/services/groups.service';
import { GoalInterface } from 'src/app/types/Goal';
import { GoalService } from 'src/app/services/goal.service';

@Component({
  selector: 'app-goal-modal',
  templateUrl: './goal-modal.component.html',
  styleUrls: ['./goal-modal.component.css']
})
export class GoalModalComponent implements OnInit,OnChanges {
  @Input() newGoal: GoalInterface;
  private accountSubs= new Array<ISubscription>();

  startDate:Date
  deadLine:Date

  constructor(
    private goalService:GoalService,
    ){
     }

  ngOnInit() {

  }
  ngOnChanges(changes: SimpleChanges) {
    if (changes) {
      this.newGoal = Object.assign({},changes.newGoal.currentValue)   
      console.log(this.newGoal)  
    }
  }

  abortNewGoal(){
    return
  }

  fun(){
    console.log(this.newGoal)
  }
  
  addGoal(){
    //check dei campi
    //this.newGoal.group=this.groupService.getCurrentSelectedGroup()
   
    if(this.newGoal.id!=-1) 
      return this.editGoal()
    let data=Object.assign({},this.newGoal)
    this.goalService.createGoal( data).subscribe(
      result  =>{
        console.log(result)
      }
    )
  }

  editGoal(){
    let data=Object.assign({},this.newGoal)
    this.goalService.updateGoal( data).subscribe(
      res=> console.log(res)
    );
  }


}
