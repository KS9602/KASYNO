import { Component, inject } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { LoginService } from '../services/login-service'

@Component({
  selector: 'app-login-component',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css',
})
export class LoginComponent {

  private fb = inject(FormBuilder).nonNullable;
  private loginService = inject(LoginService);
  private router = inject(Router);

  loginForm = this.fb.group({
    username: ['', Validators.required],
    password: ['', [Validators.required, Validators.minLength(8)]],
  });

  login(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }
    const form = this.loginForm.value;
    if(form.username && form.password){
      this.loginService.login(form.username, form.password).subscribe({
        next: () => this.router.navigate(['/rooms']),
        error: () => alert('Nieprawidłowa nazwa użytkownika lub hasło')
      })
    }

  }
}
