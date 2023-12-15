import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { GroupsService } from 'src/app/services/groups.service';
import { MatSort } from '@angular/material/sort';
import { SubscriptionLike as ISubscription } from 'rxjs';
import { GroupInterface, Group } from 'src/app/types/Group';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { group } from '@angular/animations';
import { MatPaginator } from '@angular/material/paginator';
import { GoalInterface, Goal } from 'src/app/types/Goal';
import { ActivatedRoute, Router } from '@angular/router';
import { runInThisContext } from 'vm';
import { GoalService } from 'src/app/services/goal.service';
import { filter } from 'rxjs/operators';


@Component({
  selector: 'app-goals',
  templateUrl: './goals.component.html',
  styleUrls: ['./goals.component.css']
})
export class GoalsComponent implements OnInit {

  private goalSub= new Array<ISubscription>();
  private hoverRow: GoalInterface;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort,{static:true})  sort: MatSort;

  public displayedColumns = ['select','id', 'name','description','status', 'startDate','deadLine','amountRequested','amountReached','actions'];
  public dataSource = new MatTableDataSource<GoalInterface>();
  public selection = new SelectionModel<GoalInterface>(false, undefined);
  
  private currentGroup:GroupInterface=null;

  private newGoal=new Goal();

  
  constructor(
    private groupService:GroupsService,
    private goalsService: GoalService,
    private route: ActivatedRoute,
    private router:Router
  ) { 
  }

  ngOnInit() {
 
    this.goalSub.push(this.goalsService.goals$.subscribe(
      goals=>{
        console.log("eehhehe")
        console.log(goals)
        console.log("eehhehe")

        const goals_list=goals.slice(0)
   

        if(this.dataSource==null)
          this.dataSource=new MatTableDataSource(goals_list)
        else this.dataSource.data=goals_list
        this.selection = new SelectionModel<GoalInterface>(true, []);
        this.ngAfterViewInit()
      }
      
      )
    )

   
  }


  deleteGoal(account){
    this.goalsService.deleteGoal(account).subscribe(
      result=>{
      
        console.log("eliminato")
      }
    );

  }

  editGoal(goal){
    this.newGoal=Object.assign({},goal)

  }

  createGoal(){
    this.newGoal=new Goal()
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

  }

  ngOnDestroy(): void {
    this.goalSub.forEach(sub=>

        sub.unsubscribe()
      )
      console.log("DISTRUTTO GOAL")
  }

  onMouseEnter(row): void {
      this.hoverRow = row;
  }

  onMouseLeave(row): void {
      this.hoverRow = undefined;
  }

  isRowHover(row): boolean {
      return this.hoverRow !== undefined && row.id === this.hoverRow.id;  
  }

  changeCurrentSelection():void{

  }

    /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected()
      ? this.selection.clear()
      : this.dataSource.data.forEach(row => this.selection.select(row));
  }

}
