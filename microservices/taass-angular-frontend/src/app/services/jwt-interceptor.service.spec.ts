import { TestBed } from '@angular/core/testing';

import { JwtInterceptor } from './jwt-interceptor.service';

describe('JwtInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: JwtInterceptor = TestBed.get(JwtInterceptor);
    expect(service).toBeTruthy();
  });
});

/**
 *     constructor(
        private router: Router,
        private jwtService: JwtService,
        private auth: AuthService,
        private http: HttpClient
    ) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<any> {
        let _request = this.setHeaders(request);
        if(request!=null){

            return next.handle(_request)
        }
        else{

            return this.refreshToken().pipe(
                switchMap(() => {
                    _request = this.setHeaders(request);
                    return next.handle(_request);
                }),
                catchError(() => {
                    this.logout();
                    return observableEmpty();
                }),);
        }        
    }

    private setHeaders(request) {
        const headers = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        };
        const accessToken = this.jwtService.getAccessToken();
        if (accessToken!=undefined&&this.auth.checkJWTExp(accessToken)>5000) {
            headers['Authorization'] = `Bearer ${accessToken}`;
            return request.clone({setHeaders: headers});
        }
        else return null
    }

    private refreshToken(): Observable<any> {
        return this.http.post('/api/login/refresh', { refresh: this.jwtService.getAccessToken() }).pipe(
            map(data => this.jwtService.saveToken(data)),
            shareReplay(),);
    }

    private logout() {
        this.auth.removeUser();
        this.router.navigateByUrl('/');
        window.location.reload();
    }
 */