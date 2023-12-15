import { SharedModule } from 'src/app/shared/shared.module';
import {  Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { DashboardComponent } from './dashboard.component';
import { ChartsModule } from 'ng2-charts';
import { IncomeExpenseStatsService } from 'src/app/services/StatsService/income-expense-stats.service';

const routes: Routes = [
  {
    
    path:'',
    component: DashboardComponent
  }

]

@NgModule({
  declarations: [DashboardComponent,],
  imports: [
    SharedModule,
    RouterModule.forChild(routes),
    ChartsModule
  ],
  providers: [
    IncomeExpenseStatsService
  ]
})
export class DashboardModule { }
