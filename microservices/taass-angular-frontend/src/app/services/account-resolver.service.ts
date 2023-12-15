
import { Injectable } from '@angular/core';

import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AccountInterface } from '../types/Account';
import { Observable, of } from 'rxjs';
import { AccountService } from './account.service';
import { GroupsService } from './groups.service';

@Injectable()
export class AccountResolverService implements Resolve<Observable<any>> {
  constructor(private accountService:AccountService, private groupService:GroupsService) {

  }
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
    if(route.params['account']){
      return this.accountService.retrieveAccount(route.params['account']);
    }
   
  }
}