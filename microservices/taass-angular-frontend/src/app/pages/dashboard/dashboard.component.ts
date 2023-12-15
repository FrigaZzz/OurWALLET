import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js';
import { WeatherService } from 'src/app/services/WeatherService/weather.service';
import { IncomeExpenseStatsService } from 'src/app/services/StatsService/income-expense-stats.service';
import { BudgetData } from 'src/app/types/BudgetData';
import { GoalData } from 'src/app/types/BudgetData copy';
import { GroupsService } from 'src/app/services/groups.service';
import { Transaction } from 'src/app/types/Transaction';
import { GoalInterface } from 'src/app/types/Goal';
import { AccountService } from 'src/app/services/account.service';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})


export class DashboardComponent implements OnInit {

  public barChartOptions = {
    scaleShowVerticalLines: false,
    responsive: true
  };

  public barChartLabels = [];
  public barChartType = 'bar';
  public barChartLegend = true;

  public barChartData = [];

  public pieChartLabels = null;
  public pieChartData = null;
  public pieChartType = 'pie';

  public budgetsData:BudgetData[]=[]
  public goalsData:GoalData[]=[]
  newTransaction=new Transaction()
  group;
  barChartYear=new Date().getFullYear()

  test=100

  constructor(public barChartService: IncomeExpenseStatsService,
              private _snackBar:MatSnackBar,
              private accountService:AccountService,
              private groupService:GroupsService
    ) {
   
    this.retrieveChartData()

    this.retrieveBudgettData()


    
    this.groupService.currentSelectedGroup.subscribe(group=>{
      if(group!=null && group.id!=-1){
        this.group=group
        this.retrieveGoalData(group)
        this.retrievePieChartData(group)

      }

    })
   
  }

  ngOnInit() {
    
  }

  retrieveChartData(){

    this.barChartService.retrieveChartData(this.barChartYear,true).subscribe((data:any) => {
      this.barChartLabels = data.body.labels;
      this.barChartData = data.body.dataset;
      console.log(this.barChartData)
    });
  }

  retrievePieChartData(group){
    this.barChartService.retrievePieChartData(group.id,true).subscribe((data:any) => {
      this.pieChartLabels = data.body.labels;
      this.pieChartData = data.body.dataset;
      console.log(this.barChartData)
    });
  }
  retrieveBudgettData(){
  this.barChartService.retrieveBudgettData(true).subscribe((data:any) => {
    this.budgetsData = data.body;
  });
  }

  retrieveGoalData(group){

    this.barChartService.retrieveGoalData(group.id,true).subscribe((data:any) => {
      this.goalsData = data.body;
      this.goalsData=this.goalsData.map( (data:any) => {
        let element=data.goal
        if(element.deadLine<=new Date().getTime())
           if( element.amountReached>=element.amountRequested)
             element.status="Completed"
          else
            element.status="Failed"
           else
            if( element.amountReached>=element.amountRequested)
               element.status="Completed"
            else
              element.status="Active"
          data.goal=element
          return data
      });
      console.log(this.goalsData)
    });
  
  }
  getActualPercentage(data:BudgetData){

    let percentage=(data.spent/data.budget.amount)*100;
    percentage=Math.round(percentage)
    return percentage
  }

  getActualGoalPercentage(data:GoalData){

    let percentage=(data.spent/data.goal.amountRequested)*100;
    percentage=Math.round(percentage)
    return percentage
  }

   
  
  addTransaction(goalData:{amount:number;spent:number,goal:GoalInterface}){
    this.newTransaction=new Transaction()
    this.newTransaction.amount=goalData.amount
    this.newTransaction.opt=goalData.goal.account

   
   }

   update(event){
    if(event.a==1){
      this.retrieveGoalData(this.group)
      this.openSnackBar(" Updated  ",event.b)

    }
    if(event.a==2){
      
      this.retrieveGoalData(this.group)
      this.openSnackBar(" Added  ",event.b)

    }
    if(event.a==3){
      this.retrieveGoalData(this.group)
      this.openSnackBar(" Aborted: overbudget for ",event.b,'error')

    }
  }
  openSnackBar(message: string, action: string,color?) {
    let config = new MatSnackBarConfig();
    config.verticalPosition = 'bottom';
    config.horizontalPosition = 'center';
    config.duration = 2000
    config.panelClass= ['mat-toolbar', "mat-primary"]
    if(color)
      config.panelClass= ['mat-toolbar', "mat-warn"]
    this._snackBar.open(message, action , config);
  }
  

  getGoalsPercentage(){
    let percentage=0
    let total=0
    let amount=0
    this.goalsData.forEach(data=>
      {
          if(data.goal.status!='Failed'){
            total+=data.goal.amountRequested
            amount+=data.goal.amountReached
          }
        
      }
    )
    if(total==0)return 0
    percentage=(amount/total)*100;
    percentage=Math.round(percentage)
    return percentage

  }

  getCurrentYearEarnings(){
    let total=0
    this.barChartData[0].data.forEach(data=>{
      total+=data
    })
    return total
  }

  getCurrentMonthEarnings(){
    let date=new Date(Date.now());

    let m=date.getMonth()

    return this.barChartData[0].data[m]


  }
}
