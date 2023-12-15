import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/AuthService/auth-service.service';
import { Router } from '@angular/router';
import { GroupsService } from 'src/app/services/groups.service';
import { GroupInterface } from 'src/app/types/Group';
import { filter } from 'rxjs/operators';
import { SideBarService } from 'src/app/services/side-bar.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  groups:GroupInterface[]=[]
  constructor(
    private auth: AuthService,
    private groupService:GroupsService,
    private sidebarService:SideBarService,
    private router:Router
    ) { 
      this.groupService.groups.pipe(filter(groups=> groups.length>0))
        .subscribe(
          groups=>{
           this.groups=groups
        }
      )

  }

  move(){
    this.router.navigate(["/goals"])
  }

  toggle(){
    this.sidebarService.toggleSideNav();
  }
  ngOnInit() {

  }

  logout(){
    console.log("logout")
    this.auth.doLogout()
  }

  changeSelectedGroup(selectedGroup:GroupInterface){
    this.groupService.changeCurrentSelectedGroup(selectedGroup)
  }
  openSideBar(){
    
  }
}
