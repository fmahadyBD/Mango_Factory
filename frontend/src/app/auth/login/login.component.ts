import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  loginForm: FormGroup;
  errorMessage: string | null = null;
  successMessage: string | null = null;
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]

    });
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      this.errorMessage = "Please fill valid information";
      return;
    }
    const { email, password } = this.loginForm.value;
    this.authService.login(email, password).subscribe({
      next: res => {
        this.successMessage = "Login successful";
        this.errorMessage = null;
        if (this.authService.isAdmin()) {
          this.router.navigate(['admin-dashboard']);
        } else if (this.authService.isUser()) {
          this.router.navigate(['user-dashboard']);
        }
      },
      error: err => {
        this.errorMessage = "Login failed!";
        this.successMessage = null;
      }

    })
  }

}
