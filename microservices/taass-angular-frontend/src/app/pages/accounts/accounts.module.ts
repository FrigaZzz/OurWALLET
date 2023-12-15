import { SharedModule } from '../../shared/shared.module';
import { NgModule } from '@angular/core';
import { AccountsComponent } from './accounts.component';
import { RouterModule, Routes } from '@angular/router';
import { AccountResolverService } from 'src/app/services/account-resolver.service';
import { FooterComponent } from 'src/app/components/footer/footer.component';
import { TransactionModalComponent } from 'src/app/components/modals/transaction-modal/transaction-modal.component';
import { ClickOutsideDirective } from 'src/app/directives/click-outside.directive';
import { AccountsListComponent } from 'src/app/components/accounts-list/accounts-list.component';
import { AccountModalComponent } from 'src/app/components/modals/account-modal/account-modal.component';
import { FiltersModalComponent } from 'src/app/components/modals/filters-modal/filters-modal.component';

const routes: Routes = [
  {
    
    path:'',
    children:[
      {
        path:'',
        component:AccountsListComponent,
      },
      {
        path:':account',
        component:AccountsComponent,
        resolve:{
          account:AccountResolverService
        }
      }
    ]
  }
]

@NgModule({
  declarations: [AccountsComponent,AccountsListComponent,AccountModalComponent,FiltersModalComponent,],
  imports: [
    SharedModule,    
    RouterModule.forChild(routes)
  ],
  providers: [
    AccountResolverService,
  ],
  entryComponents: [FiltersModalComponent]


}
  
  )
export class AccountsModule { }


/*
const routes: Routes = [
  {
    
    path:'',
    component: TransactionsComponent,
    data: {
      breadcrumb: 'Accounts',
    },
    children:[
      {
        path:'',
        component:AccountsComponent,
        data: {
          breadcrumb: 'transactions',
        },
        children:[
          {
            path:':account',
            component:AccountComponent,  //holds the transaction list and some editable data about the account,
            data: {
              breadcrumb: ':account',
            }
          }
        ]
      }
    ]
  }
]*/