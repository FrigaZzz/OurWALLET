import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MembersComponent } from './members.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { RouterModule, Routes } from '@angular/router';


const routes: Routes = [
  {
    
    path:'',
    component: MembersComponent
  },
  

]

@NgModule({
  declarations: [MembersComponent,  ],
  imports: [
    SharedModule,
    RouterModule.forChild(routes)

  ]
})

export class MembersModule { }

