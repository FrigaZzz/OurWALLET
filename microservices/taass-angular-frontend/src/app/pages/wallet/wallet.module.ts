import { FooterComponent } from './../../components/footer/footer.component';
import { SidebarComponent } from './../../components/sidebar/sidebar.component';
import { NavbarComponent } from './../../components/navbar/navbar.component';
import { WalletComponent } from './wallet.component';
import { SharedModule } from '../../shared/shared.module';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeResolver } from 'src/app/services/home-resolver.service';
import { AccountService } from 'src/app/services/account.service';
import { CategoriesComponent } from 'src/app/components/categories/categories.component';
import { TransactionModalComponent } from 'src/app/components/modals/transaction-modal/transaction-modal.component';
//sfs



const routes: Routes = [
  {
    path: '',
    component: WalletComponent,
    resolve: {
      isAuthenticated: HomeResolver
    },
    children:[
      {
        path:'',
        redirectTo:"dashboard"
      },
      {
        path:'dashboard',
        loadChildren: '../dashboard/dashboard.module#DashboardModule' 
      },
      { 
        path: 'goals',
        loadChildren: '../goals/goals.module#GoalsModule' 
      },
      { 
        path: 'budgets',
        loadChildren: '../budgets/budgets.module#BudgetsModule' 
      },
      { 
        path: 'profile', 
        loadChildren: '../profile/profile.module#ProfileModule' },
      { 
        path: 'accounts',
        loadChildren: '../accounts/accounts.module#AccountsModule' 
      },
      { 
        path: 'groups',
        loadChildren: '../groups/groups.module#GroupsModule' 
      },
      { 
        path: 'members',
        loadChildren: '../members/members.module#MembersModule' 
      },
      { 
        path: 'categories',
        component: CategoriesComponent,
      }
     
    ]
  }
]

@NgModule({
  declarations: [WalletComponent,NavbarComponent,SidebarComponent,FooterComponent,CategoriesComponent],
  imports: [
    SharedModule,
    RouterModule.forChild(routes),
    //sf
  ],
  providers: [
    HomeResolver,
  ],
})
export class WalletModule { }
