import { SharedModule } from 'src/app/shared/shared.module';
import { NgModule } from '@angular/core';
import { GoalsComponent } from './goals.component';
import { Routes, RouterModule } from '@angular/router';
import { GoalModalComponent } from 'src/app/components/modals/goal-modal/goal-modal.component';


const routes: Routes = [
  {
    
    path:'',
    component: GoalsComponent
  }

]


@NgModule({
  declarations: [GoalsComponent,GoalModalComponent],
  imports: [
    SharedModule,
    RouterModule.forChild(routes),

  ]
})
export class GoalsModule { }
