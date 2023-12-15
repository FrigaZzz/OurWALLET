import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { GroupsService } from 'src/app/services/groups.service';
import { MatSort } from '@angular/material/sort';
import { SubscriptionLike as ISubscription } from 'rxjs';
import { GroupInterface, Group } from 'src/app/types/Group';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { group } from '@angular/animations';
import { MatPaginator } from '@angular/material/paginator';
import { UserInterface, User } from 'src/app/types/User';
import { ActivatedRoute, Router } from '@angular/router';
import { runInThisContext } from 'vm';
import { filter } from 'rxjs/operators';
import { CategoriesService } from 'src/app/services/categories.service';
import { CategoryInterface } from 'src/app/types/Category';

@Component({
  selector: 'app-members',
  templateUrl: './members.component.html',
  styleUrls: ['./members.component.css']
})
export class MembersComponent implements OnInit {

  private memberSub= new Array<ISubscription>();
  private hoverRow: UserInterface;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort,{static:true})  sort: MatSort;

  public displayedColumns = ['select','id', 'name', "payer",'actions'];
  public dataSource = new MatTableDataSource<UserInterface>();
  public selection = new SelectionModel<UserInterface>(false, undefined);
  
  private currentGroup:GroupInterface=null;

  private isCreating=false
  private invalidUser=false

  private newUser={
    user:new User(),
    payer:false
  }

  private allUsers=new Array<UserInterface>();

  constructor(
    private groupService:GroupsService,
    private route: ActivatedRoute,
    private router:Router
  ) { 

  }

  ngOnInit() {



    this.memberSub.push(this.groupService.currentSelectedGroup.pipe(
      filter(group => group!=null&&group.id!=-1))
      .subscribe(
      group=>
      {
        this.currentGroup=group;

        this.refreshTable(group);
       
      })
    )
  }


  addMember(){
    this.isCreating=!this.isCreating
    this.newUser.user.payer=this.newUser.payer
    
    console.log(this.newUser)
    if(this.newUser.user.username=="" )
    {
      alert("invalid user")
        this.invalidUser=!this.invalidUser
    }

    else this.groupService.addMember(this.newUser.user).subscribe(
      result=>{

        this.refreshTable(this.currentGroup)
        console.log("aggiiunto")



      }
    );
  }


  refreshTable(group){

    this.groupService.getGroupUsers().subscribe(d=>{
          
      console.log(d)
      let users=[...d]

      this.groupService.getAllUsers().subscribe(data=>{
        console.log(data)
        data=data.filter(d=> users.findIndex(p=>d.id==p.id)==-1)
        this.allUsers=data

        if(this.dataSource==null)
          this.dataSource=new MatTableDataSource(users)
        else 
          this.dataSource.data=users
        this.selection = new SelectionModel<UserInterface>(true, []);
        
        this.ngAfterViewInit()
      })


    })

  }

  deleteMember(user){
    console.log(user)
    this.groupService.removeMember(user).subscribe(
      result=>{
        this.refreshTable(this.currentGroup)
        console.log("eliminato")
      }
    );

  }

  createUser(){
    this.isCreating=!this.isCreating
    this.newUser={user:new User(),payer:false}
  }


  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

  }

  ngOnDestroy(): void {
    this.memberSub.forEach(sub=>

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
