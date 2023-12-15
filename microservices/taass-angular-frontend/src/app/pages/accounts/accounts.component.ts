import { Component, OnInit, ViewChild, AfterViewInit, ElementRef, NgZone } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { TransactionInterface, Transaction } from 'src/app/types/Transaction';
import { FakedataService } from 'src/app/services/fakedata.service';
import { FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Route } from '@angular/compiler/src/core';
import { AccountService } from 'src/app/services/account.service';
import { TransactionService } from 'src/app/services/transaction.service';
import { AccountInterface, Account } from 'src/app/types/Account';
import { CategoriesService } from 'src/app/services/categories.service';
import { CategoryInterface } from 'src/app/types/Category';
import { BehaviorSubject ,  Observable ,  SubscriptionLike as ISubscription, merge, fromEvent, of, zip } from 'rxjs';
import { startWith, switchMap, tap, debounceTime, distinctUntilChanged, take } from 'rxjs/operators';
import { TransactionDataSource } from 'src/app/types/TransactionDataSource';
import { AuthService } from 'src/app/services/AuthService/auth-service.service';
import { GroupsService } from 'src/app/services/groups.service';
import { GroupInterface } from 'src/app/types/Group';
import { async } from '@angular/core/testing';
import { MatDialogConfig, MatDialog } from '@angular/material/dialog';
import { FiltersModalComponent } from 'src/app/components/modals/filters-modal/filters-modal.component';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit {
  private accountSubs= new Array<ISubscription>();

  account:AccountInterface;
  dataSource: TransactionDataSource;
  displayedColumns = ['select', 'id','description', 'category', 'accountSender','accountReceived','date', 'amount','actions'];
  
  @ViewChild(MatPaginator, { static: false }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: false }) sort: MatSort;
  @ViewChild("input", { static: false }) input: ElementRef;
  selection : SelectionModel<TransactionInterface>//= new SelectionModel<TransactionInterface>(false, null);
  @ViewChild('ref',{static:false}) ref: any;
  /**
   * Modal inputs
   */
  categories:CategoryInterface[];
  group:GroupInterface;
  newTransaction: TransactionInterface=new Transaction();
  filterDate:string="";
  filterAmount:string="";

  filters={
    minAmount:null,
    maxAmount:null,
    toDate:null,
    fromDate:null
  }


  constructor(
    private _snackBar: MatSnackBar,

  
    private route:ActivatedRoute,
    private transactionService:TransactionService,
    private categoryService:CategoriesService,
    private authService:AuthService,
    private accountService:AccountService,

    private groupsService:GroupsService,
    private dialog: MatDialog,
    private zone:NgZone,
    private router:Router


    ){
      let date=new Date(Date.now());
      
      let d=date.getDay()
      let m=date.getMonth()
      let y=date.getFullYear()

      let days=(1+new Date(Date.now()).getUTCDate())//(date.getTime()-new Date(y,m).getTime())/(24*60*60*1000)
     
      this.filters.fromDate=new Date(y,m-1,days)
      this.filters.toDate=new Date(y,m,days)

      this.filterDate="date>"+this.filters.fromDate.getTime()+" AND date<"+this.filters.toDate.getTime()


      this.accountSubs.push(categoryService.categories.subscribe(
        categories=>this.categories=categories
        )
      )

      this.accountSubs.push(route.params.subscribe(
        params=>{
          this.ngOnInit()
          }
        )
      )

    }


  ngOnInit() {
       

        this.account = this.route.snapshot.data["account"];
        this.group = this.route.snapshot.data["group"];
        this.dataSource = new TransactionDataSource(this.transactionService);
        this.selection = new SelectionModel<TransactionInterface>(true, [],true);

       console.log(this.account)
        if(this.account!=undefined)
          this.dataSource.loadTransactions(this.filterFromInput(),'date','asc',0,25,undefined,this.account.id);
        if(this.group!=undefined)
          this.dataSource.loadTransactions(this.filterFromInput(),'date','asc',0,25,this.group.id,undefined);
  }

  /**
   * Linka il paginator e il datasource
   * Qui mi assicuro che  paginator sia già caricato
   * tap comprende i click dell'utente su page size o sulle colonne della tabella
   * Il merge dei due observable trigghera il load di nuovi dati quando si cambia pagina o si ordina
   * Quando l'utente scrive, l'evento keyup permette la ricerca lato server
   */
  ngAfterViewInit() {
    this.accountSubs.push(fromEvent(this.input.nativeElement,'keyup')
      .pipe(
          debounceTime(250),      // limitiamo le richieste 
          distinctUntilChanged(), // elimina duplicati (ad esempio se due richieste consecutive sono simili e hanno stesso output)
          tap(() => 
          {
            this.loadTransactionsPage()
            this.paginator.pageIndex=0
            this.selection.clear()
          })
      )
      .subscribe() 
    )
    // dopo il sort viene resettata la paginazione quindi si va alla prima pagina
    this.accountSubs.push(
      this.sort.sortChange.subscribe(() => {
        this.paginator.pageIndex = 0
      })   
    )
    this.accountSubs.push(merge(this.paginator.page,this.sort.sortChange)
        .pipe(
            tap(() => {
              this.loadTransactionsPage()
              }
            )
        )
      .subscribe() 
    )
  }

  ngOnDestroy(): void {
    this.accountSubs.forEach(sub=>
        sub.unsubscribe()
      )
  }

  loadTransactionsPage() {

    let groupID=undefined
    let accountID=undefined
    if(!!this.account)
      accountID=this.account.id
    if(!!this.group)
      groupID=this.group.id
    
    this.selection.clear()
    this.dataSource.loadTransactions(
        this.filterFromInput(),
        this.sort.active,
        this.sort.direction,
        this.paginator.pageIndex,
        this.paginator.pageSize,
        groupID,
        accountID
        );
  }

  isEdit(row){
   if(this.selection.selected.length==1 &&row.id==this.selection.selected[0].id){
    return false
   }
   return true
  }

  compareById(o1: AccountInterface, o2:AccountInterface) {
    if(o1==null||o2==null)return false
    return o1.id === o2.id
  }
  toDate(d){
    return new Date(d)
  }

  filterFromInput(){
    let value=""
    if(this.input!=undefined&&this.input!=null)
      value=this.input.nativeElement.value
    let searchfilter=""
    if(value!=''){
      searchfilter= "( description:*"+value+"* OR "+
      "accountNAME:*"+value+"* OR "+
      "transferTargetAccountNAME:*"+value+"* OR "+
      //"amount:*"+value+"* OR "+
      "categoryNAME:*"+value+"* )";
    }
    let completeFilter=""
    if(searchfilter!="")
      completeFilter+=searchfilter+" AND "
    if(this.filterDate!="")
      completeFilter+=this.filterDate+" AND "
    if(this.filterAmount!="")
      completeFilter+=this.filterAmount+" AND "
    completeFilter=completeFilter.substr(0,completeFilter.length-4);
    console.log(completeFilter)
    return completeFilter
  }
    /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    let numRows = this.paginator.pageSize;
    if(this.paginator.pageSize>this.dataSource.data().length)
      numRows=this.dataSource.data().length
    return numSelected==numRows
  }

  update(event){
    if(event.a==1){
      this.loadTransactionsPage()
      this.openSnackBar(" Updated  ",event.b)
    }
    if(event.a==2){
      this.loadTransactionsPage()
      this.openSnackBar(" Added  ",event.b)

    }
    if(event.a==3){
      this.loadTransactionsPage()
      this.openSnackBar(" Aborted: overbudget for ",event.b,'error')

    }
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected()
      ? (
        this.selection.clear()
        )
      : (
        this.dataSource.data().forEach(
              row => 
                {
                    this.selection.select(row)
                }
          )
      )
  }

  print(){
    console.log(this.selection.selected.length)

  }

  addTransaction(){
   this.newTransaction=new Transaction()
  }

  deleteTransactions(){

    zip(...this.selection.selected.map(sel=>  this.transactionService.deleteTransaction(sel)))
    .subscribe(data => {
      console.log("deleted all")
      this.loadTransactionsPage()

      this.openSnackBar(" Deleted  ","Transactions",'success')

  
    }, err => {
      console.log(err)
    })
   
  }
  editTransaction(){
    this.newTransaction=this.selection.selected[0];

  }

  previousTable(){
    console.log("AIA")
    if(this.account.user==-1)
      this.router.navigate(["/goals"])
    else
      this.router.navigate(["/accounts"],{ queryParams:{"type":"user"}});
  }

  openDialog(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.data = this.filters
    const dialogRef = this.dialog.open(FiltersModalComponent, dialogConfig);

    
    dialogRef.afterClosed().subscribe(
       (out:any) => {
            console.log(out)
            if(out==null || out==undefined){
              return
            }
            let data=out;
            console.log("Dialog output:", data)
            this.filters=data;
            this.filterDate=""
            this.filterAmount=""
            //date
            if(data.fromDate!=null)
              this.filterDate+="date>"+new Date(data.fromDate).getTime()
            if(data.toDate!=null)
              this.filterDate=(this.filterDate=="")?"date<"+new Date(data.toDate).getTime():"( "+this.filterDate+" AND date<"+new Date(data.toDate).getTime()+" )"
            //amount
              if(data.maxAmount!=null)
              this.filterAmount="amount<"+data.maxAmount
            if(data.minAmount!=null)
              this.filterAmount=(this.filterAmount=="")?"amount>"+data.minAmount:"( "+this.filterAmount+" AND amount>"+data.minAmount+" )"
            this.loadTransactionsPage();
          },err=>{
            console.log(err)
          }
    );    
  }
  openSnackBar(message: string, action: string,color?) {
    let config = new MatSnackBarConfig();
    config.verticalPosition = 'bottom';
    config.horizontalPosition = 'center';
    config.duration = 2000
    config.panelClass= ['mat-toolbar', "mat-primary"]
    if(color)
      config.panelClass= ['mat-toolbar', "mat-warn"]
    this._snackBar.open(message, action , config);
  }
}
 