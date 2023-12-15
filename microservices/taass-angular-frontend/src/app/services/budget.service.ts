import { Injectable } from '@angular/core';
import { BehaviorSubject ,  Observable ,  SubscriptionLike as ISubscription, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { BudgetInterface, Budget } from '../types/Budget';
import { map, filter } from 'rxjs/operators';
import { CategoriesService } from './categories.service';
import { CategoryInterface } from '../types/Category';
import { GroupsService } from './groups.service';
import { group } from '@angular/animations';
import { GroupInterface } from '../types/Group';

const API_PATH = '/api/groups';
const DEBUG=false
/**
 * @_budgetSubject contiene la lista dei budget dell'attuale gruppo visualizzato, abbiamo quindi la dipendenza dal gruppo
 *                 possiamo di nuovo decidere se mantenere due vettori di budget (per gruppo personale dell'utente e del gruppo selezionato)
 *                 oppure fetchare solo su richiesta la lista dei budget del gruppo desiderato.
 *                 
 * >> commento da inserire nel servizio GOAL
 * Da notare che un GOAL ha un suo account su cui sono registrate le transazioni. 
 *                      - un altro approccio al budget potrebbe benissimo equivalere al considerare sottinsiemi di transazioni che hanno il campo
 *                        BUDGET
 */
@Injectable({
  providedIn: 'root'
})
export class BudgetService {
  private budgetIDs=0;

  private budgetSub = new Array<ISubscription>();

  private _budgetSubject = new BehaviorSubject<Array<BudgetInterface>>(new Array<BudgetInterface>());
  public budgets$ = this._budgetSubject.asObservable();

 
  private categories: Array<CategoryInterface>;
  private currentGroup:GroupInterface

  constructor(
      private http: HttpClient,
      private categoriesService: CategoriesService,
      private groupService: GroupsService
  ) {

    this.budgetSub.push(this.categoriesService.categories.pipe(
      filter(categories => categories.length > 0))
      .subscribe(
        categories => {
              this.categories = categories;
          }
      ));
      this.budgetSub.push(this.groupService.currentSelectedGroup.pipe(
        filter(group => group!=null && group.id!=-1))
        .subscribe(
            group => {
                this.currentGroup=group
                this.budgetSub.push(this.retrieveBudgets(group).subscribe());
            }
        ));
     
  }

  ngOnInit() {
   
    
}

  ngOnDestroy(): void {
    this.budgetSub.forEach(sub => {
      sub.unsubscribe();
      });
  }

  retrieveBudgets(group: GroupInterface): Observable<Array<BudgetInterface>> {
          return this.http.get(API_PATH+`/${group.id}/budgets`).pipe(
              map(
              (data:{meta:any,body:any}) => {
                  const arr = new Array<BudgetInterface>();
                  data.body.forEach(element => {
                      arr.push(new Budget(element));
                  });
                  this._budgetSubject.next(arr);
                  return arr;
              },
              err => console.log(err)
          ));
  }

  createBudget(saveBudget: BudgetInterface): Observable<BudgetInterface> {
    console.log(saveBudget)
    let budget=this.buildFromBudget(saveBudget)
    
     return this.http.post(API_PATH+`/${this.currentGroup.id}/budgets`, budget).pipe(map(
          (data:any) => {
             this.budgetSub.push(this.retrieveBudgets(this.currentGroup).subscribe());

              console.log('Budget created');
              return data;
          }
      ));
  }

  updateBudget(updateBudget: BudgetInterface): Observable<BudgetInterface> {
    console.log(updateBudget)
    let budget=this.buildFromBudget(updateBudget)

   return this.http.patch(API_PATH+`/${updateBudget.groupID}/budgets` + `/${budget.categoryID}`, budget).pipe(map(
          (data:any) => {
              this.budgetSub.push(this.retrieveBudgets(this.currentGroup).subscribe());

              console.log('Budget updated');
              return data;
          }
      ));
  }

  deleteBudget(deleteBudget: BudgetInterface): Observable<BudgetInterface> {
     return this.http.delete(API_PATH+`/${deleteBudget.groupID}/budgets` + `/${deleteBudget.category.id}`).pipe(map(
          (data:any) => {
            this.budgetSub.push(this.retrieveBudgets(this.currentGroup).subscribe());
            console.log('Budget deleted');

            return data;
             
          }
      ));
  }
  private buildFromBudget(budget:BudgetInterface){
    let out={
        categoryID:budget.category.id,
        amount:budget.amount,
        groupID:budget.groupID
    }
    return out
  }

}
