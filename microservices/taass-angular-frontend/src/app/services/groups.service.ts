import { Injectable } from '@angular/core';
import { GroupInterface, Group } from '../types/Group';
import { BehaviorSubject ,  Observable ,  SubscriptionLike as ISubscription, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { AuthService} from 'src/app/services/AuthService/auth-service.service';
import { map } from 'rxjs/operators';
import { UserInterface } from '../types/User';


const API_PATH = '/api/groups';
const DEBUG=false

@Injectable({
  providedIn: 'root'
})
/**
 * @_currentGroupSubject  gruppo personale dell'utente autenticato
 * @_currentSelectedGroupSubject gruppo selezionato dall'utente nella pagina "groups"
 * @_groupsSubject tutti i gruppi a cui l'utente è sottoscritto 
 */
export class GroupsService {
  private groupsIDs=0

  private subscriptions=new Array< ISubscription>();
  
  private _currentGroupSubject = new BehaviorSubject<GroupInterface>(new Group());
  private _currentSelectedGroupSubject = new BehaviorSubject<GroupInterface>(new Group());
  private _groupsSubject = new BehaviorSubject<Array<GroupInterface>>(new Array<GroupInterface>());


  public groups = this._groupsSubject.asObservable();
  public currentGroup = this._currentGroupSubject.asObservable();
  public currentSelectedGroup = this._currentSelectedGroupSubject.asObservable();


  groupsData:GroupInterface[]=[
    {
      id: 0,
      isFamilyGroup:true,
      name: "Personal",
      description:"first group"
    },
    {
      id: 1,
      isFamilyGroup:false,
      name: "group 2",
      description:"third group"
    },
    {
      id: 2,
      isFamilyGroup:false,
      name: "group 1",
      description:"second group"
    },
  ]
  
  constructor(
    private http: HttpClient,
    private auth: AuthService

  ) { 
    
    this.subscriptions.push( 
      this.auth.isAuthenticated.subscribe(
        isAuth=>{
          if(isAuth==true)
            this.retrieveGroups(true).subscribe()
        }
      )
    )      
    /**
     *   In realtà non so se sia necessario. L'idea è che dopo un logout siano persi tutti i dati 
     *   relativi al gruppo ecc. Vedremo poi l'effetto
    */
    // this.auth.isAuthenticated.subscribe(
    //   auth=>{
    //     if(auth==false&&this._currentGroupSubject.getValue().id>-1)
    //     {
    //       this.groups = this._groupsSubject.asObservable();
    //       this.currentGroup = this._currentGroupSubject.asObservable();
    //       this.currentSelectedGroup = this._currentSelectedGroupSubject.asObservable();
    //       this._currentGroupSubject = new BehaviorSubject<GroupInterface>(new Group());
    //       this._currentSelectedGroupSubject = new BehaviorSubject<GroupInterface>(new Group());
    //       this._groupsSubject = new BehaviorSubject<Array<GroupInterface>>(new Array<GroupInterface>());
    //     }
    //   }
       
    // )
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => {
        sub.unsubscribe();
    });
  }

  retrieveGroups(isInit?: boolean): Observable<Array<GroupInterface>> {
      let params={group:this.auth.getCurrentUser().id+""}
      const arr = new Array<GroupInterface>();
      let  famGroup:GroupInterface;
      
      if(DEBUG){
        this.groupsData.forEach(element => {
        
          element.members=[{id: 1, email: "luca@gmail.com", username: "luca"}]
          if(element.isFamilyGroup==true)
            famGroup=element;
          
          arr.push(new Group(element));
        });
        this.groups = this._groupsSubject.asObservable();
        this._groupsSubject.next(arr);

        //this.currentGroup = this._currentGroupSubject.asObservable();
        this._currentGroupSubject.next(famGroup);
        
        //this.currentSelectedGroup = this._currentSelectedGroupSubject.asObservable();
        this._currentSelectedGroupSubject.next(famGroup);

        return this.groups;
      }
      else return this.http.get(API_PATH,{params:params}).pipe(
          map(
          (data:any) => {
         
              data.body.forEach(element => {
                if(element.isFamilyGroup==true)
                    famGroup=element;
                arr.push(new Group(element));
              });
              //this.groups = this._groupsSubject.asObservable();
              this._groupsSubject.next(arr);

              //this.currentGroup = this._currentGroupSubject.asObservable();
              this._currentGroupSubject.next(famGroup);

              //this.currentSelectedGroup = this._currentSelectedGroupSubject.asObservable();
              this._currentSelectedGroupSubject.next(famGroup);
      
              return arr;
          },
          err => console.log(err)
      ));
     
  
  }

  createGroup(saveGroup: GroupInterface): Observable<GroupInterface> {
    if (DEBUG) {     
        saveGroup.id=this.groupsIDs++   
        const arr = this._groupsSubject.value;
        arr.push(saveGroup);
        this._groupsSubject.next(arr);
        console.log('Group created');
        return of(saveGroup);

    }else return this.http.post(API_PATH, saveGroup).pipe(map(
          (data:any) => {
              this.subscriptions.push(this.retrieveGroups().subscribe())
              console.log('Group created');
              return data;
          }
      ));
  }

  updateGroup(updateGroup: GroupInterface): Observable<GroupInterface> {
    if (DEBUG) {     
        const arr = this._groupsSubject.value;
        const index = arr.findIndex(b => b.id === updateGroup.id);
        arr[index] = updateGroup;
        this._groupsSubject.next(arr);
        console.log('Group updated');
        return of(updateGroup);
    }else return this.http.patch(API_PATH+"groups"+`/${updateGroup.id}`, updateGroup).pipe(map(
          (data:any) => {
              const groups = new Group(data.groups);
              const arr = this._groupsSubject.value;
              const index = arr.findIndex(b => b.id === updateGroup.id);
              arr[index] = groups;
              this._groupsSubject.next(arr);
              console.log('Group updated');
              return groups;
          }
      ));
  }

  deleteGroup(deleteGroup: GroupInterface): Observable<GroupInterface> {
    if (DEBUG) {     
        const arr = this._groupsSubject.value;
        const index = arr.findIndex(b => b.id === deleteGroup.id);
        arr.splice(index, 1);
        this._groupsSubject.next(arr);
        console.log('Group deleted');
        return of(deleteGroup);

    }else return this.http.delete(API_PATH+`/groups/${deleteGroup.id}`).pipe(map(
          (data:any) => {
              const arr = this._groupsSubject.value;
              const index = arr.findIndex(b => b.id === deleteGroup.id);
              arr.splice(index, 1);
              this._groupsSubject.next(arr);
              console.log('Group deleted');
              return data;
          }
      ));
  }
    getAllUsers(): Observable<UserInterface[]>{
    return this.http.get("/api/members").pipe(map(
          (data:any) => {
            return data.body
          })
        )
      }

    
    getGroupUsers(): Observable<UserInterface[]>{
      if(this._currentSelectedGroupSubject.value.id==-1) return;
      console.log("CERCOO")

      return this.http.get("/api/groups/"+this._currentSelectedGroupSubject.value.id+"/users").pipe(map(
           (data:any) => {
              return data.body
           })
         )
       }
   
  addMember(member: UserInterface): Observable<UserInterface> {
    let user={username:member.username,email:member.email,isPayer:member.payer}
    return this.http.post("/api/groups/"+this._currentSelectedGroupSubject.value.id+"/users",user).pipe(map(
          (data:any) => {
              
              return null;
          }
      ));
  }

  removeMember(member: UserInterface): Observable<UserInterface> {
    return this.http.delete("/api/groups/"+this._currentSelectedGroupSubject.value.id+"/users/"+member.id).pipe(map(
      (data:any) => {
       
          console.log('Category created');
          return null;
        }
  ));
  }






  getCurrentSelectedGroup(): GroupInterface {
    return this._currentSelectedGroupSubject.value;
  }

  getCurrentGroup(): GroupInterface {
    return this._currentGroupSubject.value;
  }

  getGroups(): GroupInterface[] {
    return this._groupsSubject.value;
  }

  /**
   * 
   * @param groupID 
   */
  changeCurrentSelectedGroup(group){

    let groups=this._groupsSubject.value
    let newCurr=groups.find(element => {
       return element.id==group.id
    });
    if(newCurr==null) {
      console.log("Error tryng to select non-existent group")
      return null;
    }
    else
      console.log("lo sostituiamo")
    //this.currentSelectedGroup = this._currentSelectedGroupSubject.asObservable();
    this._currentSelectedGroupSubject.next(newCurr);
  }



}
