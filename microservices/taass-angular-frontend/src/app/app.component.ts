import { Router } from '@angular/router';
import { AuthService } from './services/AuthService/auth-service.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'taass-angular-frontend';

  constructor(private auth: AuthService, private router: Router) {
    auth.configure();
   
  }
}
