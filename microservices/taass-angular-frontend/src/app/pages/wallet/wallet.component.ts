import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/AuthService/auth-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-wallet',
  templateUrl: './wallet.component.html',
  styleUrls: ['./wallet.component.css']
})
export class WalletComponent implements OnInit {


  constructor(private auth: AuthService,private router:Router) { 
    this.auth.isAuthenticated.subscribe(
      (isAuthenticated: boolean) => {
        if(isAuthenticated==false)
          this.router.navigate(['/login']);
      }
    );
    //this.groupService.
  }

  ngOnInit() {

  }

}
