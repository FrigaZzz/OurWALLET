import { SharedModule } from '../../shared/shared.module';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { LoginComponent } from './login.component';
import { AuthGuard } from 'src/app/guards/auth.guard';


const routes: Routes = [
  {
    
    path:'',
    component: LoginComponent,
  }

]

@NgModule({
  declarations: [LoginComponent],
  imports: [
    RouterModule.forChild(routes),
    SharedModule
    ],
  providers: [
    AuthGuard
  ],
  
})
export class LoginModule { }
