import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { BehaviorSubject ,  Observable ,  SubscriptionLike as ISubscription, of } from 'rxjs';
import { BudgetData } from 'src/app/types/BudgetData';
import { GoalData } from 'src/app/types/BudgetData copy';

const API_PATH = '/api/stats/';

@Injectable({
  providedIn: 'root'
})
export class IncomeExpenseStatsService {

  private budgetSub = new Array<ISubscription>();

  private _budgetSubject = new BehaviorSubject<Array<BudgetData>>(new Array<BudgetData>());
  public budgets$ = this._budgetSubject.asObservable();

  private _chartSubject = new BehaviorSubject<any>({});
  public chart$ = this._chartSubject.asObservable();
  
  private _goalSubject = new BehaviorSubject<Array<GoalData>>(new Array<GoalData>());
  public goals$ = this._goalSubject.asObservable();

  public chart_data = null;
  
  public budget_data = null;

  public goals_data = null;

  public pie_chart_data = null;


  constructor(private http:HttpClient) { this.retrieveChartData(true); }

  retrieveChartData(year,init?,refresh?): Observable<Object> {
    if(init){
      this.chart_data = this.http.get(API_PATH+"IncomeExpenseData/"+year);
    }
    return this.chart_data;
  }

  retrievePieChartData(groupID,init?,refresh?): Observable<Object> {
    if(init){
      this.pie_chart_data = this.http.get(API_PATH+"getPieChartData/"+groupID);
    }
    return this.pie_chart_data;
  }

  retrieveBudgettData(init?,refresh?): Observable<Object> {
    if(init||refresh){
      this.budget_data = this.http.get(API_PATH+"familyBudgetSuggestions");
    }
    return this.budget_data;
  }

  retrieveGoalData(groupID,init?,refresh?): Observable<Object> {
    if(init||refresh){
      console.log("eehhehe")
      this.goals_data = this.http.get(API_PATH+"goalSuggestions/"+groupID);
      console.log(this.goals_data)
      
    }
    return this.goals_data;
  }
}
