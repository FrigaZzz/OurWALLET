import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { GroupsService } from 'src/app/services/groups.service';
import { MatSort } from '@angular/material/sort';
import { SubscriptionLike as ISubscription } from 'rxjs';
import { GroupInterface, Group } from 'src/app/types/Group';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { group } from '@angular/animations';
import { MatPaginator } from '@angular/material/paginator';
import { Transaction } from 'src/app/types/Transaction';

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
export class GroupsComponent implements OnInit {
  private groupSub= new Array<ISubscription>();
  private hoverRow: GroupInterface;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort,{static:true})  sort: MatSort;
  public displayedColumns = ['select','id', 'name', 'description', 'isFamilyGroup','actions'];
  public dataSource = new MatTableDataSource<GroupInterface>();
  public selection = new SelectionModel<GroupInterface>(false, undefined);
 
  private currentGroup:GroupInterface=null;

  private newGroup=new Group();

  constructor(
    private groupService:GroupsService
  ) { 
    console.log("sfa")

  }

  ngOnInit() {
    this.groupSub.push(this.groupService.groups.subscribe(
      groups=>
      {
        //const groups_list=new Array<GroupInterface>();
        if(this.dataSource==null)
          this.dataSource=new MatTableDataSource(groups)
        else this.dataSource.data=groups
        this.selection = new SelectionModel<GroupInterface>(true, []);
        this.ngAfterViewInit()
      })
    )
    this.groupSub.push(this.groupService.currentGroup.subscribe(
      group=>
      {
        //const groups_list=new Array<GroupInterface>();
        this.currentGroup=group;
        this.ngAfterViewInit()
      })
    )
  }

  createGroup(){
    this.newGroup=new Group()
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

  }

  ngOnDestroy(): void {
    this.groupSub.forEach(sub=>
        sub.unsubscribe()
      )
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

  changeSelectedGroup(selectedGroup:GroupInterface){
    this.groupService.changeCurrentSelectedGroup(selectedGroup)
    alert("changed group")
  }


 
}
