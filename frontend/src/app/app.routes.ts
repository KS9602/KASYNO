import { Routes } from '@angular/router';
import { Home } from './home/home'
import { Board } from './board/board'
import { RegisterComponent } from './register-component/register-component'
import { LoginComponent } from './login-component/login-component'


export const routes: Routes = [
  { path: '', component: Home },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'game', component: Board }
];