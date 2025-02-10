import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { RegisterComponent } from './auth/register/register.component';
import { LoginComponent } from './auth/login/login.component';
import { AdminDashboardComponent } from './admin/admin-dashboard/admin-dashboard.component';
import { UserDashboardComponent } from './user/user-dashboard/user-dashboard.component';
import { GetAllUsersComponent } from './admin/get-all-users/get-all-users.component';
import { HomeComponent } from './home/home.component';
import { NavbarComponent } from './navbar/navbar.component';
import { DashboardComponent } from './admin/dashboard/dashboard.component';
import { ViewAboutComponent } from './about/view-about/view-about.component';
import { ContentManageAboutComponent } from './about/content-manage-about/content-manage-about.component';
import { ViewContactComponent } from './contact/view-contact/view-contact.component';
import { ManageContactComponent } from './contact/manage-contact/manage-contact.component';

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    AdminDashboardComponent,
    UserDashboardComponent,
    GetAllUsersComponent,
    HomeComponent,
    NavbarComponent,
    DashboardComponent,
    ViewAboutComponent,
    ContentManageAboutComponent,
    ViewContactComponent,
    ManageContactComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    provideClientHydration(),
    provideHttpClient(
      withFetch()
    )
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
