import { Component } from '@angular/core';
import { Board } from '../board/board';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [Board],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {}