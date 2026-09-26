export type RoomStatus = 'WAITING' | 'IN_GAME' | 'FINISHED';

export interface RoomResponse {
  id: number;
  name: string;
  playersCount: number;
  maxPlayers: number;
  status: RoomStatus;
}
