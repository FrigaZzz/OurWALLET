import { RequestSettingsService } from './services/RequestSettingService/request-settings.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { ChartsModule } from 'ng2-charts';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { WeatherService } from './services/WeatherService/weather.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FakedataService } from './services/fakedata.service';
import { JwtInterceptor } from './services/jwt-interceptor.service';
import { JwtService } from './services/jwt.service';
import { AccountService } from './services/account.service';
import { Router } from '@angular/router';
import { ClickOutsideDirective } from './directives/click-outside.directive';
import { OAuthModule } from 'angular-oauth2-oidc';

// google e facebook auth
import { SocialLoginModule, AuthServiceConfig } from "angularx-social-login";
import { GoogleLoginProvider, FacebookLoginProvider } from "angularx-social-login";
import { DialogUsernameGoogleComponent } from './components/modals/dialog-username-google/dialog-username-google.component';
import { SharedModule } from './shared/shared.module';

const config = new AuthServiceConfig(
  [
 
    {
      id: GoogleLoginProvider.PROVIDER_ID,
      provider: new GoogleLoginProvider("86393286031-5dqcksufteitfnms8pjt2d561p8flvu8.apps.googleusercontent.com")
    }
  ]
)

export function provideConfig(){
  return config;
}

export function app_Init( settingsHttpService: RequestSettingsService) {
  return () => settingsHttpService.initializeApp();
}

@NgModule({
  imports: [
    SocialLoginModule,
    OAuthModule.forRoot(),
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ChartsModule,
    BrowserAnimationsModule,
    SharedModule
    
  ], 
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    ClickOutsideDirective,
    DialogUsernameGoogleComponent,
  ],
  providers: [
    { provide: APP_INITIALIZER, useFactory: app_Init, deps: [RequestSettingsService], multi: true },
    WeatherService, 
    FakedataService,
    JwtService,
    {
      provide: AuthServiceConfig,
      useFactory: provideConfig
    },
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true,  }
  ]
  ,
  entryComponents:[DialogUsernameGoogleComponent],

  bootstrap: [AppComponent]
})
export class AppModule {
  constructor(
    private readonly router: Router,
  ) {
    // router.events
    //   .subscribe( ev=>console.log(ev))
  }
 }
