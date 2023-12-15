import { AppComponent } from './app.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { NgModule, Component } from '@angular/core';
import { Routes, RouterModule} from '@angular/router';
import { AuthGuard } from './guards/auth.guard';
import { WalletComponent } from './pages/wallet/wallet.component';

const routes: Routes = [
  
  {
    path: '',
    loadChildren: './pages/wallet/wallet.module#WalletModule',
  },
  
  {
    path: 'login',
    loadChildren: './pages/login/login.module#LoginModule',
  },
  {
    path: 'register',
    loadChildren: './pages/registration/registration.module#RegistrationModule',
  },

  {
    path: '**',
    pathMatch: 'full',
    component: PageNotFoundComponent
  },
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
