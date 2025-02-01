import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent {
  isLoggedIn: boolean = false;

  constructor(
    private authServce: AuthService,
    private router: Router


  ) { }

  ngOnInit() {
    this.isLoggedIn = this.authServce.isLoggedIn();
  }

  logout(): void {
    this.authServce.logout();
    this.isLoggedIn = false;
    this.router.navigate(['']);
  }


}
