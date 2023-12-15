import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { take } from 'rxjs/operators';
import { AuthService } from './AuthService/auth-service.service';


@Injectable()
export class HomeResolver implements Resolve<boolean> {
    constructor(private router: Router, private auth: AuthService) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        const obs = this.auth.isAuthenticated.pipe(take(1));
        obs.subscribe(
            isAuth => {
                if (isAuth==false) {
                    this.router.navigate(['/login']);
                    return false
                }
                else 
                    return true
            }
        );
        return obs;
    }
}
