import { AuthConfig } from 'angular-oauth2-oidc';

export const authConfig: AuthConfig = {

  // Url of the Identity Provider
  issuer: 'https://accounts.google.com',

  // URL of the SPA to redirect the user to after login
  redirectUri: window.location.origin + '/index.html',

  // The SPA's id. The SPA is registered with this id at the auth-server
  clientId: '86393286031-5dqcksufteitfnms8pjt2d561p8flvu8.apps.googleusercontent.com',

  // set the scope for the permissions the client should request
  // The first three are defined by OIDC. The 4th is a usecase-specific one
  scope: 'openid profile email ',
  strictDiscoveryDocumentValidation: false,
  showDebugInformation: true,

  sessionChecksEnabled: true

}