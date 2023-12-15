import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { GroupsService } from 'src/app/services/groups.service';
import { MatSort } from '@angular/material/sort';
import { SubscriptionLike as ISubscription } from 'rxjs';
import { GroupInterface, Group } from 'src/app/types/Group';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { group } from '@angular/animations';
import { MatPaginator } from '@angular/material/paginator';
import { BudgetInterface, Budget } from 'src/app/types/Budget';
import { ActivatedRoute, Router } from '@angular/router';
import { runInThisContext } from 'vm';
import { BudgetService } from 'src/app/services/budget.service';
import { filter } from 'rxjs/operators';
import { CategoriesService } from 'src/app/services/categories.service';
import { CategoryInterface, Category } from 'src/app/types/Category';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Component({
  selector: 'app-budgets',
  templateUrl: './budgets.component.html',
  styleUrls: ['./budgets.component.css']
})
export class BudgetsComponent implements OnInit {

  
  private budgetSub= new Array<ISubscription>();
  private hoverRow: BudgetInterface;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort,{static:true})  sort: MatSort;

  public displayedColumns = ['select','id', 'name', "amount",'actions'];
  public dataSource = new MatTableDataSource<BudgetInterface>();
  public selection = new SelectionModel<BudgetInterface>(false, undefined);
  
  private currentGroup:GroupInterface=null;
  private categories:Array<CategoryInterface>;
  private isCreating=false
  private invalidBudget=false

  private newBudget=new Budget();
  private budgets=new Array<BudgetInterface>();

  constructor(
    private _snackBar: MatSnackBar,
    private groupService:GroupsService,
    private budgetsService: BudgetService,
    private route: ActivatedRoute,
    private categoryService:CategoriesService,
    private router:Router
  ) { 
    this.categories=new Array<CategoryInterface>();
  }

  ngOnInit() {
 
    this.budgetSub.push(this.categoryService.categories.subscribe(
      categories=>{
        categories=categories//.filter(cat=>{
          //let x=this.budgets.find(b=>b.category.id==cat.id)
          //return x==undefined;          
        //})
        this.categories=categories
      }
      )
    )

    this.budgetSub.push(this.budgetsService.budgets$.subscribe(
      budgets=>{
        const budgets_list=new Array<BudgetInterface>();
        budgets.forEach(budget=> {
          let cat=this.categories//.filter(c=>c.id==budget.category.id)
          console.log(cat)
          // if(cat!=null&&cat!=undefined)
          // this.categories.splice(this.categories.indexOf(cat[0]),1)
        
          budgets_list.push(budget)
        });
        this.budgets=budgets_list
        if(this.dataSource==null)
          this.dataSource=new MatTableDataSource(budgets_list)
        else this.dataSource.data=budgets_list
        this.selection = new SelectionModel<BudgetInterface>(true, []);
        this.ngAfterViewInit()
      }
      
      )
    )


    this.budgetSub.push(this.groupService.currentGroup.pipe(
      filter(group => group!=null&&group.id!=-1))
      .subscribe(
      group=>
      {
        this.currentGroup=group;
        this.ngAfterViewInit()
      })
    )
  }


  addBudget(){
    this.isCreating=!this.isCreating

    if(this.newBudget.category==null || this.newBudget.amount==-1)
    {
        this.invalidBudget=!this.invalidBudget
        return
    }

    if(this.newBudget.groupID!=-1)
      this.budgetsService.updateBudget(this.newBudget).subscribe(data=>
        {
        this.openSnackBar("updated Budget ",this.newBudget.category.name)
        this.budgetsService.retrieveBudgets(this.currentGroup)
        this.createBudget()
      }
    )

    else
     this.budgetsService.createBudget(this.newBudget).subscribe(data=>{


        this.openSnackBar(" created Budget  ",this.newBudget.category.name)
        this.budgetsService.retrieveBudgets(this.currentGroup)
        this.createBudget()
      }
      )

    
  }

  checkData(ev){
    console.log(ev.target.value[0])
    let index=ev.target.value[0]
    let cat=this.categories[index]
    this.dataSource.data.forEach(b=>{
      if(b.category.id==cat.id)
        {
        this.newBudget.category=new Category()
        this.invalidBudget=!this.invalidBudget

        return
      }
    })
  }

  deleteBudget(budget){
    console.log(budget)
    this.budgetsService.deleteBudget(budget).subscribe(
      result=>{

        this.openSnackBar("deleted Budget ",budget.category.name)
        this.budgetsService.retrieveBudgets(this.currentGroup)
        this.createBudget()
      }
    );

  }

  createBudget(){
    this.isCreating=!this.isCreating
    this.newBudget=new Budget()
  }

  editBudget(budget){
    this.isCreating=!this.isCreating
    this.newBudget=Object.assign({},budget)
    console.log(this.newBudget)

  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

  }

  compareById(o1: CategoryInterface, o2:CategoryInterface) {
    if(o1==null||o2==null)return true
    return o1.id === o2.id
  }

  ngOnDestroy(): void {
    this.budgetSub.forEach(sub=>

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
      return this.hoverRow !== undefined && row.id === this.hoverRow.category.id;  
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

  openSnackBar(message: string, action: string) {
    let config = new MatSnackBarConfig();
    config.verticalPosition = 'bottom';
    config.horizontalPosition = 'center';
    config.duration = 2000
    this._snackBar.open(message, action , config);
  }

}
