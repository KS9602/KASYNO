export type CardSuit = 'HEARTS' | 'DIAMONDS' | 'CLUBS' | 'SPADES';

export type CardType =
  | 'TWO' | 'THREE' | 'FOUR' | 'FIVE' | 'SIX' | 'SEVEN' | 'EIGHT' | 'NINE' | 'TEN'
  | 'JACK' | 'QUEEN' | 'KING' | 'ACE';

export interface CardResponse {
  id: number;
  suit: CardSuit;
  type: CardType;
}
