import { Component, computed, input, output } from '@angular/core';
import { CardResponse, CardSuit, CardType } from '../models/card-response';

const SUIT_SYMBOLS: Record<CardSuit, string> = {
  HEARTS: '♥',
  DIAMONDS: '♦',
  CLUBS: '♣',
  SPADES: '♠',
};

const RANK_LABELS: Record<CardType, string> = {
  TWO: '2', THREE: '3', FOUR: '4', FIVE: '5', SIX: '6', SEVEN: '7',
  EIGHT: '8', NINE: '9', TEN: '10', JACK: 'J', QUEEN: 'Q', KING: 'K', ACE: 'A',
};

@Component({
  selector: 'app-card',
  standalone: true,
  templateUrl: './card.html',
  styleUrl: './card.css'
})
export class Card {

  card = input.required<CardResponse>();
  selected = input(false);

  cardClick = output<CardResponse>();

  suitSymbol = computed(() => SUIT_SYMBOLS[this.card().suit]);
  rankLabel = computed(() => RANK_LABELS[this.card().type]);
  isRed = computed(() => this.card().suit === 'HEARTS' || this.card().suit === 'DIAMONDS');

  onClick(): void {
    this.cardClick.emit(this.card());
  }
}
