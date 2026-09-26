import { CardSuit, CardType } from './card-response';

export interface PlayCardRequest {
  cardIds: number[];
  requestedRank: CardType | null;
  requestedSuit: CardSuit | null;
}
