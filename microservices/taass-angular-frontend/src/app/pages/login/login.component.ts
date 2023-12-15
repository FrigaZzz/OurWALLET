import { User } from './../../types/User';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService} from './../../services/AuthService/auth-service.service';


import { FacebookLoginProvider, GoogleLoginProvider, AuthService as SocialService } from "angularx-social-login";
import { OAuthService } from 'angular-oauth2-oidc';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {
  @ViewChild("form",{static:true}) loginForm: HTMLFormElement;
  user:User=new User()
  incorrectLogin = false;


  constructor(
    private oauthService: OAuthService,
    private auth: AuthService, 
    //private social:SocialService,
    private router: Router, 
    ) {


    this.user=new User()
    this.auth.isAuthenticated.subscribe(
      (signed: boolean) => {
        if(signed==true)
          this.router.navigate(['/']).then(out=> console.log(out)).catch(err=>console.log(err));
      }
    );
  }

  ngOnInit() {
  }
  
  loginWithGoogle(){
    this.auth.signWithGoogle()
  }

  login() {
    if (this.user.username === "" || this.user.password === "") {
      //alert("username and password empty");
      this.incorrectLogin=!this.incorrectLogin
      return;
    }
    this.auth.doLogin(this.user).subscribe((res: { body: any,meta: any }) => {
      if (res.meta.status==401||res.meta.status==400) {
        this.loginForm.reset();
        this.incorrectLogin = true;
        setTimeout(() => {
          this.incorrectLogin = false;
        }, 3000)
      }      
    }, err => {
      this.loginForm.reset();
      this.incorrectLogin = true;
      setTimeout(() => {
        this.incorrectLogin = false;
      }, 3000)
    })
  }
  

}
