import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

import { throwError as observableThrowError, empty as observableEmpty,  Observable } from 'rxjs';
import { shareReplay, map, switchMap, catchError } from 'rxjs/operators';

import { JwtService } from './jwt.service';
import { AuthService } from './AuthService/auth-service.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

    constructor(
        private router: Router,
        private jwtService: JwtService,
        private auth: AuthService,
        private http: HttpClient
    ) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<any> {
        let _request=this.setHeaders(request)
        const accessToken = this.jwtService.getAccessToken();
        if (accessToken!=undefined&&!request.url.includes('refresh'))
        {   
            //se mancano 10 minuti all exp allora refresh

            if(this.auth.checkJWTExp(accessToken)<5)
            {
                console.log("Refresh token")
                return this.refreshToken().pipe(
                    switchMap(() => {
                        let _request=this.setHeaders(request)
                        console.log("ce reprovo")

                        return next.handle(_request);
                    }),
                    catchError((err) => {
                        console.log(err)
                        this.logout();
                        return observableEmpty();
                    }),);
            }
            else{
                
                console.log("ancora buono"+this.auth.checkJWTExp(accessToken))
            }
        }
        return next.handle(_request)

    }


    private setHeaders(request) {
        const headers = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        };
        const accessToken = this.jwtService.getAccessToken();
        if (accessToken) {
            headers['Authorization'] = `Bearer ${accessToken}`
        }
        return request.clone({setHeaders: headers})
    }

    private refreshToken(): Observable<any> {
        return this.http.get('/api/refresh').pipe(
            map( (data:any) => {
                this.jwtService.saveToken(data.body.jwt)
            }),
            shareReplay());
    }

    private logout() {
        this.auth.removeUser();
        this.router.navigateByUrl('/');
        window.location.reload();
    }

}

