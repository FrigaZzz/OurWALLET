import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { GroupsService } from 'src/app/services/groups.service';
import { MatSort } from '@angular/material/sort';
import { SubscriptionLike as ISubscription } from 'rxjs';
import { GroupInterface, Group } from 'src/app/types/Group';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { group } from '@angular/animations';
import { MatPaginator } from '@angular/material/paginator';
import { AccountService } from 'src/app/services/account.service';
import { AccountInterface, Account } from 'src/app/types/Account';
import { ActivatedRoute, Router } from '@angular/router';
import { runInThisContext } from 'vm';

@Component({
  selector: 'app-accounts-list',
  templateUrl: './accounts-list.component.html',
  styleUrls: ['./accounts-list.component.css']
})
export class AccountsListComponent implements OnInit {

  private accountsSub= new Array<ISubscription>();
  private hoverRow: AccountInterface;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort,{static:true})  sort: MatSort;
  public displayedColumns = ['select','id', 'name', 'isEasyPay','actions'];
  public dataSource = new MatTableDataSource<AccountInterface>();
  public selection = new SelectionModel<AccountInterface>(false, undefined);
  
  private currentGroup:GroupInterface=null;

  private newAccount=new Account();
  private accounts=new Array<AccountInterface>()

  private type='user';

  constructor(
    private groupService:GroupsService,
    private accountService: AccountService,
    private route: ActivatedRoute,
    private router:Router
  ) { 

  }

  ngOnInit() {
 
    this.accountsSub.push(this.route.queryParams.subscribe(
      params=>{
        console.log(params)
        if(params['type']){
          switch(params['type']){
            case 'family': {
              this.type='family';         
              this.accounts=this.accountService.getFamilyGroupAccounts()
              break;
            }
            case 'funds': {
              this.type='funds';
              this.accounts=this.accountService.getCommonGroupAccounts()
              break;
            }
            case 'user': {
              this.type='user';
              this.accounts=this.accountService.getUserAccounts()
              break;
            }
            default:
              this.router.navigate(["/groups",{queryParams:{type:"user"}}])
          }
        }
        else{
          this.router.navigate(["/groups",{queryParams:{type:"user"}}])
        }
        const groups_list=new Array<GroupInterface>();
        if(this.dataSource==null)
          this.dataSource=new MatTableDataSource(this.accounts)
        else this.dataSource.data=this.accounts
        this.selection = new SelectionModel<AccountInterface>(true, []);
        this.ngAfterViewInit()
      }
      
      )
    )

    this.accountsSub.push(this.groupService.currentSelectedGroup.subscribe(
      group=>
      {
        switch(this.type){
          case 'family': {
            this.type='family';
            this.accounts=this.accountService.getFamilyGroupAccounts()
            break;
          }
          case 'funds': {
            this.type='funds';
            this.accounts=this.accountService.getCommonGroupAccounts()
            break;
          }
          case 'user': {
            this.type='user';
            this.accounts=this.accountService.getUserAccounts()
            break;
          }
        }

        const groups_list=new Array<GroupInterface>();
        if(this.dataSource==null)
          this.dataSource=new MatTableDataSource(this.accounts)
        else this.dataSource.data=this.accounts
        this.selection = new SelectionModel<AccountInterface>(true, []);
        this.ngAfterViewInit()
      })
    )

    this.accountsSub.push(this.groupService.currentGroup.subscribe(
      group=>
      {
        const groups_list=new Array<GroupInterface>();
        this.currentGroup=group;
        this.ngAfterViewInit()
      })
    )
  }


  deleteAccount(account){
    this.accountService.deleteAccount(account).subscribe(
      result=>{
        this.accounts=this.accounts.filter(acc=> acc.id!=account.id)
        this.dataSource.data=this.accounts
        console.log("eliminato")
      }
    );

  }

  createAccount(){
    this.newAccount=new Account()
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

  }

  ngOnDestroy(): void {
    this.accountsSub.forEach(sub=>
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

}
