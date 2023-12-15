import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BudgetsComponent } from './budgets.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { RouterModule, Routes } from '@angular/router';


const routes: Routes = [
  {
    
    path:'',
    component: BudgetsComponent
  }

]


@NgModule({
  declarations: [BudgetsComponent],
  imports: [
    SharedModule,
    RouterModule.forChild(routes),
  ]
})
export class BudgetsModule { }
