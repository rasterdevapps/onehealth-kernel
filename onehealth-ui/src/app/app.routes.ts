import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { HisViewComponent } from './components/his-view/his-view.component';
import { EmrViewComponent } from './components/emr-view/emr-view.component';
import { LisViewComponent } from './components/lis-view/lis-view.component';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'his', component: HisViewComponent },
  { path: 'emr', component: EmrViewComponent },
  { path: 'lis', component: LisViewComponent }
];
