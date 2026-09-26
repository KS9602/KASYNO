import { Component, inject } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { RegisterService } from '../services/register-service'

@Component({
  selector: 'app-register-component',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register-component.html',
  styleUrl: './register-component.css',
})
export class RegisterComponent {

  private fb = inject(FormBuilder).nonNullable;
  private registerService = inject(RegisterService);
  private router = inject(Router);

  registerForm = this.fb.group({
    username: ['', Validators.required],
    password: ['', [Validators.required, Validators.minLength(8)]],
    password2: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]]
  });

  register(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }
    const form = this.registerForm.value;
    if(form.username && form.password && form.email){
      this.registerService.register(form.username, form.password, form.email).subscribe({
        next: () => this.router.navigate(['/login']),
        error: () => alert('Nie udało się zarejestrować')
      })
    }

  }
}
