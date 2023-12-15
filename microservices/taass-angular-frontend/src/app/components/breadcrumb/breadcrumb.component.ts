import { IBreadCrumb } from '../../types/Breadcrumb';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, NavigationEnd, PRIMARY_OUTLET, RoutesRecognized } from '@angular/router';
import { filter, distinctUntilChanged } from 'rxjs/operators';
import { map, mergeMap } from 'rxjs/internal/operators';
@Component({
  selector: 'app-breadcrumb',
  templateUrl: './breadcrumb.component.html',
  styleUrls: ['./breadcrumb.component.css']
})
export class BreadcrumbComponent implements OnInit {
  public breadcrumbs: IBreadCrumb[]

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,) {
      this.breadcrumbs = this.buildBreadCrumb(this.activatedRoute.root);
  }

  ngOnInit() {
    // ... implementation of ngOnInit
    this.router.events.pipe(
      filter((event) => event instanceof NavigationEnd),
      distinctUntilChanged(),
    ).subscribe(() => {
      this.breadcrumbs = this.buildBreadCrumb(this.activatedRoute.root);
    })
  }

  /**
   * Recursively build breadcrumb according to activated route.
   * @param route
   * @param url
   * @param breadcrumbs
   */
  buildBreadCrumb(route: ActivatedRoute, url: string = '', breadcrumbs: IBreadCrumb[] = []): IBreadCrumb[] {
    // ... implementation of buildBreadCrumb
   //recupera la label associata nel router module
   let label = route.routeConfig && route.routeConfig.data ? route.routeConfig.data.breadcrumb : '';
   //recupera il path associato 
   let path = route.routeConfig && route.routeConfig.data ? route.routeConfig.path : '';
   console.log("begin")
   console.log(route.routeConfig)


   const lastRoutePart = path.split('/').pop();
   const isDynamicRoute = lastRoutePart.startsWith(':');
   console.log(lastRoutePart+"   "+isDynamicRoute)
   console.log("stop1 bread")
    if(isDynamicRoute && !!route.snapshot) {
      const paramName = lastRoutePart.split(':')[1];
      path = path.replace(lastRoutePart, route.snapshot.params[paramName]);
      /* label = route.snapshot.params[paramName]; */
    }
    const nextUrl = path ? `${url}/${path}` : url;
    
    console.log(nextUrl)
    console.log("end")

    const breadcrumb: IBreadCrumb = {
        label: label,
        url: nextUrl,
    };
    const newBreadcrumbs = breadcrumb.label ? [ ...breadcrumbs, breadcrumb ] : [ ...breadcrumbs];
    if (route.firstChild) {
        //If we are not on our current path yet,
        //there will be more children to look after, to build our breadcumb
        return this.buildBreadCrumb(route.firstChild, nextUrl, newBreadcrumbs);
    }
    else{

    }
    return newBreadcrumbs;
  }

}