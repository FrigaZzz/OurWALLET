import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { GroupsService } from 'src/app/services/groups.service';
import { MatSort } from '@angular/material/sort';
import { SubscriptionLike as ISubscription } from 'rxjs';
import { GroupInterface, Group } from 'src/app/types/Group';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { group } from '@angular/animations';
import { MatPaginator } from '@angular/material/paginator';
import { CategoryInterface, Category } from 'src/app/types/Category';
import { ActivatedRoute, Router } from '@angular/router';
import { runInThisContext } from 'vm';
import { filter } from 'rxjs/operators';
import { CategoriesService } from 'src/app/services/categories.service';


@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {
  
private categorySub= new Array<ISubscription>();
private hoverRow: CategoryInterface;

@ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
@ViewChild(MatSort,{static:true})  sort: MatSort;

public displayedColumns = ['select','id', 'name', "super",'actions'];
public dataSource = new MatTableDataSource<CategoryInterface>();
public selection = new SelectionModel<CategoryInterface>(false, undefined);

private currentGroup:GroupInterface=null;
private categories:Array<CategoryInterface>;
private superCategories:Array<CategoryInterface>;

private isCreating=false
private invalidCategory=false

private newCategory=new Category();

constructor(
  private route: ActivatedRoute,
  private categoryService:CategoriesService,
  private router:Router
) { 
  this.categories=new Array<CategoryInterface>();
}

ngOnInit() {

  this.categoryService.categories.subscribe(
    categories=>{
      console.log("HELLO")
      this.categories=categories.slice(0)
      this.superCategories=categories.filter(c=>c.superCategory==null).slice(0)
      if(this.dataSource==null)
        this.dataSource=new MatTableDataSource(categories)
      else
       this.dataSource.data=categories
      this.selection = new SelectionModel<CategoryInterface>(true, []);
      this.ngAfterViewInit()
    }
    )
    this.categoryService.retrieveCategores(true)




}


addCategory(){
  console.log(this.newCategory)
  let categoryObj={
    id:-1,
    name:this.newCategory.name,
    superCategory:-1
  }
  if(this.newCategory.superCategory!=null)
    categoryObj.superCategory=this.newCategory.superCategory.id;


  if(this.newCategory.name=="" )
  {
      this.invalidCategory=!this.invalidCategory
  }
  else this.categoryService.createCategory(categoryObj).subscribe(
    result=>{
      console.log(result)

    }
  );
  this.newCategory=new Category()

}


deleteCategory(category){
  this.categoryService.deleteCategory(category).subscribe(
    result=>{

    }
  );

}

createCategory(){
  this.isCreating=!this.isCreating
  this.newCategory=new Category()
}


ngAfterViewInit(): void {
  this.dataSource.sort = this.sort;
  this.dataSource.paginator = this.paginator;

}

ngOnDestroy(): void {
  this.categorySub.forEach(sub=>

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
