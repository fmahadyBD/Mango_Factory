import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import { error } from 'node:console';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  registrationForm: FormGroup;
  errorMessage: string | null = null;
  successMessage: string | null = null;
  image: File | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {

    this.registrationForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      phone: ['', Validators.required],
      gender: ['', Validators.required],
      address: ['', Validators.required],
      dob: ['', Validators.required]
    }, 
    {
      validators: this.passwordMatcherValidator 
    });

  }

  passwordMatcherValidator(formGroup: FormGroup): { [key: string]: boolean } | null {
    const password = formGroup.get('password')?.value;
    const confirmedPassword = formGroup.get('confirmPassword')?.value;
    return password === confirmedPassword ? null : { mismatch: true };
  }

  // File selected method
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input?.files && input.files[0]) {
      this.image = input.files[0];
    }
  }

  onSubmit() {
    if (this.registrationForm.invalid) {
      console.log("Check the inputs");
      this.errorMessage = 'Check inputs, there may be some issues.';
      return;
    }

    if (!this.image) {
      this.errorMessage = 'Please select an image';
      return;
    }

    const { name, email, password, phone, address, dob, gender,image } = this.registrationForm.value;

    this.authService.register(
      { name, email, password, phone, address, dob, gender, image: '' },
      this.image
    ).subscribe({
      next: () => {
        this.successMessage = 'Registration complete. Check your email to activate your account.';
        this.router.navigate(['login']);
      },
      error: () => {
        console.log(error);
        this.errorMessage = "Registration failed,";
      }
    });
  }
}
