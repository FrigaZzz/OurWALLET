import { AuthService } from './../services/AuthService/auth-service.service';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { take, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router){}
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      // return this.auth.isAuthenticated.pipe(take(1), map(bool => !bool));
      return this.auth.isAuthenticated.pipe(take(1));
      // if(!this.auth.isLoggedIn()){
      //   console.log("no token")
      //   return true;
      // }
      // console.log("TOKEN valid")
      // this.router.navigate(['/dashboard']);
      // return false;
  }
  
}
