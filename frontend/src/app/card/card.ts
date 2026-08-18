import { Component, input } from '@angular/core';
import { CardModel } from '../models/card-model';

@Component({
  selector: 'app-card',
  standalone: true,
  templateUrl: './card.html',
  styleUrl: './card.css'
})
export class Card {

  card = input.required<CardModel>();

}