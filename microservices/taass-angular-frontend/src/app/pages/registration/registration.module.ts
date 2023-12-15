import { SharedModule } from '../../shared/shared.module';
import { Routes, RouterModule } from '@angular/router';
import { RegistrationComponent } from './registration.component';
import { NgModule } from '@angular/core';


const routes: Routes = [
  {
    
    path:'',
    component: RegistrationComponent
  }

]

@NgModule({
  declarations: [RegistrationComponent],
  imports: [
    SharedModule,
    RouterModule.forChild(routes)

  ]
})
export class RegistrationModule { }
