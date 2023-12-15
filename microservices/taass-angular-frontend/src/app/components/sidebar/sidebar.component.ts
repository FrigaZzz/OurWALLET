import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { AccountService } from 'src/app/services/account.service';
import { SubscriptionLike as ISubscription, Observable } from 'rxjs';
import { GroupsService } from 'src/app/services/groups.service';
import { element } from 'protractor';
import { GroupInterface } from 'src/app/types/Group';
import { AccountInterface } from 'src/app/types/Account';
import { SideBarService } from 'src/app/services/side-bar.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  private groupSub= new Array<ISubscription>();  

  /**
   * Listo in modo separato gli account (distinguo gruppo da account utente)
   */
  public userAccounts$: Observable<Array<AccountInterface>>;
  public familyAccounts$: Observable<Array<AccountInterface>>;
  public commonGroupAccounts$: Observable<Array<AccountInterface>>;

  private userAccounts=[]
  private familyAccounts=[]
  private commonGroupAccounts=[]

  private currentGroup:GroupInterface;

  constructor(
    private accountService:AccountService, 
    private groupService:GroupsService,
    public sidebarService: SideBarService
    ) { }
  

  ngOnInit() {
  
    this.userAccounts$=this.accountService.accounts$
    this.familyAccounts$=this.accountService.familyGroupAccounts$
    this.commonGroupAccounts$=this.accountService.commonFundsAccount$



    this.groupSub.push(this.groupService.currentSelectedGroup.subscribe(
      group=>{
        console.log("GROUP")
        this.currentGroup=group;
      
      }
    ))
    
    this.groupSub.push(this.accountService.accounts$.subscribe())
    this.groupSub.push(this.accountService.familyGroupAccounts$.subscribe())
    this.groupSub.push(this.accountService.commonFundsAccount$.subscribe())
  }
  ngOnDestroy(): void {
    this.groupSub.forEach(sub=>
        sub.unsubscribe()
      )
  }

  print(){
    this.userAccounts$.subscribe(
      v=> console.log("PRINTO"+ v.length)
    )
  }

  }
/**
 * 
 * 
 *  private groupSub= new Array<ISubscription>();
  private hoverRow: GroupInterface;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort,{static:true})  sort: MatSort;
  public displayedColumns = ['select','id', 'name', 'description', 'isFamilyGroup','actions'];
  public dataSource = new MatTableDataSource<GroupInterface>();
  public selection = new SelectionModel<GroupInterface>(false, undefined);
  
  private currentGroup:GroupInterface=null;


  constructor(
    private groupService:GroupsService
  ) { }

  ngOnInit() {
    this.groupSub.push(this.groupService.groups.subscribe(
      groups=>
      {
        //const groups_list=new Array<GroupInterface>();
        this.dataSource=new MatTableDataSource(groups)
        this.selection = new SelectionModel<GroupInterface>(true, []);
        this.ngAfterViewInit()
      })
    )
 */