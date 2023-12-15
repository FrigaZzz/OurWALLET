import { SharedModule } from 'src/app/shared/shared.module';
import { NgModule } from '@angular/core';
import { GroupsComponent } from './groups.component';
import { RouterModule, Routes } from '@angular/router';
import { GroupModalComponent } from 'src/app/components/modals/group-modal/group-modal.component';
import { TransactionModalComponent } from 'src/app/components/modals/transaction-modal/transaction-modal.component';



const routes: Routes = [
  {
    
    path:'',
    component: GroupsComponent
  },
  

]

@NgModule({
  declarations: [GroupsComponent, GroupModalComponent  ],
  imports: [
    SharedModule,
    RouterModule.forChild(routes)

  ]
})

export class GroupsModule { }

