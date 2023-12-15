import { AuthService} from '../../services/AuthService/auth-service.service';
import { User } from '../../types/User';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { GoogleLoginProvider } from 'angularx-social-login';
import { MatDialog } from '@angular/material/dialog';
import { DialogUsernameGoogleComponent } from 'src/app/components/modals/dialog-username-google/dialog-username-google.component';
import { HttpParams } from '@angular/common/http';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  @ViewChild("f",{static:true}) registerForm: HTMLFormElement;
  success:boolean;
  errors:string;
  user:User=new User()
  pass:string=""

  constructor(
    private auth: AuthService, 
    private router: Router) {
   

    this.user=new User()
    this.pass=""
    this.errors=null
    this.success=false
    this.auth.isAuthenticated.subscribe(
      (signed: boolean) => {
        if(signed)
          this.router.navigate(['/']);
      }
    );
  }
  ngOnInit() {

  }

  /**
   *  CAMBIARE IL TIPO DI JSON IN RICEZIONE
   *  MOSTRA MSG ERRORE (username esistente, email già usata)
   */
  signUp(){
    this.success=false
    this.errors=null
    this.auth.doSignUp(this.user).then((res: { body: any,meta: any }) => {
      this.success = true;
      this.registerForm.reset()

    }, err => {
      this.errors= "Errors with response"
      if (!!err.error.meta&&err.error.meta.status!=200) {        
        let errs:Array<String>=err.error.meta.errors
        this.errors+=": " +errs.join(", ")
      }

      //or show where the error is: Autofocus!
      //this.registerForm.reset();
     
    })

  }
  googleSignUp(){
    this.auth.signWithGoogle().then( (data)=>
                                  
                              {
                                
                                console.log("loggato")
                                console.log(data)
                                
                              }    
                              )
                              .catch( (err:any)=>{
                                console.log("MMH")
                                console.log(err)
                              

                              })

  }


}
