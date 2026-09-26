import { GameStatus } from './game-state-response';

export interface GameResponse {
  id: number;
  roomId: number;
  gameNumber: number;
  status: GameStatus;
  currentPlayerId: number;
}
