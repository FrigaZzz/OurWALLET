import { Injectable } from '@angular/core';
import { BehaviorSubject ,  Observable ,  SubscriptionLike as ISubscription, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { GoalInterface, Goal } from '../types/Goal';
import { map, filter } from 'rxjs/operators';
import { CategoriesService } from './categories.service';
import { CategoryInterface } from '../types/Category';
import { group } from '@angular/animations';
import { GroupInterface } from '../types/Group';
import { GroupsService } from './groups.service';

const API_PATH = '/api/groups';
const DEBUG=false
/**
 * @_goalSubject contiene la lista dei goal dell'attuale gruppo visualizzato, abbiamo quindi la dipendenza dal gruppo
 *                 possiamo di nuovo decidere se mantenere due vettori di goal (per gruppo personale dell'utente e del gruppo selezionato)
 *                 oppure fetchare solo su richiesta la lista dei goal del gruppo desiderato.
 *                 
 * >> commento da inserire nel servizio GOAL
 * Da notare che un GOAL ha un suo account su cui sono registrate le transazioni. 
 *                      - un altro approccio al goal potrebbe benissimo equivalere al considerare sottinsiemi di transazioni che hanno il campo
 *                        BUDGET
 */
@Injectable({
  providedIn: 'root'
})
export class GoalService {
  private goalIDs=0;

  private goalSub = new Array<ISubscription>();

  private _goalSubject = new BehaviorSubject<Array<GoalInterface>>(new Array<GoalInterface>());
  public goals$ = this._goalSubject.asObservable();
  private currentGroup : GroupInterface;

 
  constructor(
      private http: HttpClient,
      private groupService: GroupsService
  ) {

      this.goalSub.push(this.groupService.currentGroup.pipe(
        filter(group => group!=null))
        .subscribe(
            group => {
                if(group.id==-1)return
                this.currentGroup=group;
                this.goalSub.push(this.retrieveGoals(group).subscribe(  
                  arr=>this._goalSubject.next(arr)
                ));
            }
        ));
    
  }

  ngOnInit() {
   
    
}

  ngOnDestroy(): void {
    this.goalSub.forEach(sub => {
      sub.unsubscribe();
      });
  }

  retrieveGoals(group: GroupInterface): Observable<Array<GoalInterface>> {
          return this.http.get(API_PATH+`/${group.id}/goals`).pipe(
              map(
              (data:{meta:any,body:any}) => {
                  console.log(data)
                  const arr = new Array<GoalInterface>();
                  data.body.forEach(element => {
                    let g=new Goal(element)
                    element.deadLine= new Date(element.deadLine)
                    element.startDate= new Date(element.startDate) 
                    // completed, on going, failed 
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
                     //element.deadLine>new Date().getTime() || element.amountRequested>=element.amountReached             
                    arr.push(element);
                  });
            

                  return arr;
              },
              err => console.log(err)
          ));

  }

  createGoal(saveGoal: GoalInterface): Observable<GoalInterface> {

    let commitGoal=this.buildInputGoal(saveGoal)

    console.log(commitGoal)
     return this.http.post(API_PATH+`/${this.currentGroup.id}/goals`, commitGoal).pipe(map(
          (data:any) => {
             
            this.goalSub.push(this.retrieveGoals(this.currentGroup).subscribe(
                (goals: Array<GoalInterface>)=>{
                        this._goalSubject.next(goals);
                }
            ));              return data;
          }
      ));
  }

  updateGoal(updateGoal: GoalInterface): Observable<GoalInterface> {
    let saveGoal=this.buildInputGoal(updateGoal)

    return this.http.patch(API_PATH+`/${this.currentGroup.id}/goals` + `/${saveGoal.goalID}`, saveGoal).pipe(map(
          (data:any) => {
            this.goalSub.push(this.retrieveGoals(this.currentGroup).subscribe(
                (goals: Array<GoalInterface>)=>{
                        this._goalSubject.next(goals);
                }
            ));            return data;
          }
      ));
  }

  deleteGoal(deleteGoal: GoalInterface): Observable<GoalInterface> {
    return this.http.delete(API_PATH+`/${this.currentGroup.id}/goals` + `/${deleteGoal.id}`).pipe(map(
          (data:any) => {
            this.goalSub.push(this.retrieveGoals(this.currentGroup).subscribe(
                (goals: Array<GoalInterface>)=>{
                        this._goalSubject.next(goals);
                }
            ));            
            return data;
          }
      ));
  }

  buildInputGoal(saveGoal:GoalInterface){
    let goal={
      name: saveGoal.name,
      deadLine: saveGoal.deadLine.getTime(),
      startDate: saveGoal.startDate.getTime(),
      amount:saveGoal.amountRequested,
      groupID:saveGoal.group,
      goalID:saveGoal.id,
      description:saveGoal.description
    }
    return goal;
  }
  
}
