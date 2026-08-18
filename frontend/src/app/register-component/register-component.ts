import { Component, inject } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { RegisterService } from '../services/register-service'

@Component({
  selector: 'app-register-component',
  imports: [ReactiveFormsModule],
  templateUrl: './register-component.html',
  styleUrl: './register-component.css',
})
export class RegisterComponent {

  private fb = inject(FormBuilder).nonNullable;
  private registerService = inject(RegisterService);

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
    console.log(this.registerForm.value);
    const form = this.registerForm.value;
    if(form.username && form.password && form.email){
      this.registerService.register(form.username, form.password, form.email).subscribe({})
    }

  }
}