import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { AdminDashboardComponent } from './admin/admin-dashboard/admin-dashboard.component';
import { UserDashboardComponent } from './user/user-dashboard/user-dashboard.component';
import { GetAllUsersComponent } from './admin/get-all-users/get-all-users.component';
import { HomeComponent } from './home/home.component';



const routes: Routes = [
  {path:'',component:HomeComponent},
  
  {path:'login',component:LoginComponent},
  {path:'register',component:RegisterComponent},

  {path:'admin-dashboard',component:AdminDashboardComponent},

  {path:'user-dashboard',component:UserDashboardComponent},
  {path:'get-all-users',component:GetAllUsersComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
