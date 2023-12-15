import { ConfigService } from './../ConfigService/config.service';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { UserInterface, User } from 'src/app/types/User';
import { BehaviorSubject ,  Observable ,  SubscriptionLike as ISubscription, of } from 'rxjs';
import { distinctUntilChanged, map } from 'rxjs/operators';
import { JwtService } from '../jwt.service';
import { TokenPayload } from 'src/app/types/token-payload';
import { AuthConfig } from 'angular-oauth2-oidc';
import { SocialUser } from "angularx-social-login";
import {
  AuthService as socialService,
  FacebookLoginProvider,
  GoogleLoginProvider
} from "angularx-social-login"
import { MatDialog } from '@angular/material/dialog';
import { DialogUsernameGoogleComponent } from 'src/app/components/modals/dialog-username-google/dialog-username-google.component';



@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authSub = new Array<ISubscription>();


  private _currentUserSubject = new BehaviorSubject<UserInterface>(new User());
  private _isAuthenticatedSubject = new BehaviorSubject<boolean>(false);

  public currentUser = this._currentUserSubject.asObservable().pipe(distinctUntilChanged());
  public isAuthenticated = this._isAuthenticatedSubject.asObservable();

  url =""
  
  private socialUser: SocialUser;


  constructor(    public dialog: MatDialog,

    private config:ConfigService,
     private http:HttpClient,
     private socialService:socialService,
     private jwtService:JwtService) 
    {
      socialService.readyState.subscribe(
        data=>
       {
         console.log(data)
       },err=>{
         console.log("errore")
       }
    )

    this.url=config.settings.apiUrl
    this.retrieveSessionFromStorage()
    
  }

    configure(){
     
    }


/**
 * 
 * @param userData 
 */
  doLogin(userData: any): Observable<any> {
    return this.http.post("/api/login", userData).pipe(map(
      (ret: { body:any, meta: any }) => {
        if (ret.meta.status == 200) {

          this.jwtService.saveToken(ret.body.jwt);
          let payload= JSON.parse(window.atob(ret.body.jwt.split('.')[1]));
          console.log(payload.iat)
          console.log(payload.exp)
          let user={id:payload.sub,email:payload.email,username:payload.name}
          this.setUser(user);
        }
        return ret
      }),
      err => {
        this.removeUser()
        return err;
      })  
  }
  
  doLogout() {
      this.removeUser()   
      this.doLogoutWithGoogle();

  }

  doSignUp(userData){
   
    return new Promise((resolve, reject) => {
      this.http.post("/api/users", userData).
        subscribe((ret: { body: any,meta: any }) => {
          if (ret.meta.status==200) {
            resolve(ret);
          }
          else
            reject(ret);
        }, err => { 
          reject(err) });
    });
  }



  /**
   *  
   */
  retrieveSessionFromStorage(){
    let token=this.jwtService.getAccessToken()
    if(!!token){
      let payload= JSON.parse(window.atob(token.split('.')[1]));
      if(!!payload.iat && !!payload.exp ){ // creare un'interfaccia per verificare che il token sia valido e corretto
        let iat=payload.iat;
        let exp=payload.exp;
        let now=Date.now()/1000
        if(now>exp){
          this.removeUser();
          return false;
        }
        let user={id:payload.sub,email:payload.email,username:payload.name}
        this.setUser(user)
        return true;
      }
    }
    this.removeUser();
    return false
  }

  /**
   * 
   * @param token 
   * @OUTPUT      remaining minutes 
   */
  checkJWTExp(token){
    let payload: TokenPayload= JSON.parse(window.atob(token.split('.')[1]));
    let iat=payload.iat;
    let exp=payload.exp;
    let now=Date.now()/1000
    return (exp-now) /60
  }



  getCurrentUser(): User {
      return this._currentUserSubject.value;
  }
  getCurrentState(): boolean {
    return this._isAuthenticatedSubject.value;
}

  removeUser() {
      this.jwtService.destroyToken();
      this._currentUserSubject.next(new User());
      this._isAuthenticatedSubject.next(false);
  }

  
  /** Usare OAUTH 2.0
   *  Forse abilitare un silent Login così da refreshare anche il jwt alla scadenza
   *  NOTA: non ho voglia di sistemare anche la question refresh token quindi:
   *        - una volta registrato / loggato un utente, useremo solo i nostri JWT
   *        - effettuo gia dopo la signInWithGoogle, una signOut per ripulire il servizio
   */


  async verifySignUp(userData,params?){
    return this.http.post("/api/oAuthUsers", userData,{params:params}).subscribe(
      (ret: any) => {
        if (ret.meta.status == 200) {
          this.socialService.signOut()
          this.jwtService.saveToken(ret.body.jwt);
          let payload= JSON.parse(window.atob(ret.body.jwt.split('.')[1]));
          console.log(payload.iat)
          console.log(payload.exp)
          let user={id:payload.sub,email:payload.email,username:payload.name}
          this.setUser(user);
        }
        console.log(ret);
        alert("BELLA")
        return "ciao"
      },
      (err:any) => {
        if (err.error.meta!=null&&err.error.meta.status==409) {  
          console.log(err)  
          err={usernameError:true,data:userData}

          if(err.data!=null&&err.data!=undefined){
            let username=err.username
            let userData=err.data

            const dialogRef = this.dialog.open(DialogUsernameGoogleComponent, {
              width: '250px',
              data: {username: ""}
            });
        
            dialogRef.afterClosed().subscribe(result => {
              if(result!=''){
                console.log('The dialog was closed'+result);
                let username = result;
                const params = new HttpParams()
                .set('username', username.toString())
                
                console.log(params)
                this.verifySignUp(userData,params);
              }
            

              }
            );
          }



        }
        return err
      })  

  }

  signWithGoogle(){
    return this.socialService.signIn(GoogleLoginProvider.PROVIDER_ID).then((userData :SocialUser)=> {
      console.log(userData)
      this.socialUser=userData
      return this.verifySignUp(userData)
      //this.apiConnection(userData);
   },err=> alert(err));
  }

  /** Bisogna far fuori sia il jwt del API server sia il token di google (tramite il plugin)
   *  
   */
  doLogoutWithGoogle(){
    this.socialService.signOut();
  }

  private setUser(user: User) {
    this._currentUserSubject.next(user);
    this._isAuthenticatedSubject.next(true);
  }

  
  
}
