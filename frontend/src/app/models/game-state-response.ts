import { CardResponse, CardSuit, CardType } from './card-response';
import { PlayerGameState } from './player-game-state';

export type GameStatus = 'WAITING' | 'IN_PROGRESS' | 'FINISHED';
export type AttackType = 'NONE' | 'TWO' | 'THREE' | 'FOUR' | 'KING_HEARTS' | 'KING_SPADES';

export interface GameStateResponse {
  myCards: CardResponse[];
  myCardsCount: number;
  opponents: PlayerGameState[];
  table: CardResponse[];
  tableCardsCount: number;
  deckCardsCount: number;
  status: GameStatus;
  currentPlayerId: number;
  attackType: AttackType;
  attackAmount: number;
  requestedRank: CardType | null;
  requestedSuit: CardSuit | null;
  drawnCardId: number | null;
}
