import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { CreateRoomRequest } from '../models/create-room-request';
import { JoinRoomRequest } from '../models/join-room-request';
import { RoomResponse } from '../models/room-response';
import { GameResponse } from '../models/game-response';

@Injectable({
  providedIn: 'root',
})
export class RoomService {
  private http = inject(HttpClient);

  getRooms(): Observable<RoomResponse[]> {
    return this.http.get<RoomResponse[]>('/api/makao/rooms')
      .pipe(
        catchError(e => { console.error('Get rooms error: ', e); return throwError(() => e); })
      );
  }

  getRoom(roomId: number): Observable<RoomResponse> {
    return this.http.get<RoomResponse>(`/api/makao/rooms/${roomId}`)
      .pipe(
        catchError(e => { console.error('Get room error: ', e); return throwError(() => e); })
      );
  }

  createRoom(name: string, password: string, maxPlayers: number): Observable<RoomResponse> {
    const body: CreateRoomRequest = { name, password, maxPlayers };
    return this.http.post<RoomResponse>('/api/makao/rooms', body)
      .pipe(
        tap(r => console.log('Create room response: ', r)),
        catchError(e => { console.error('Create room error: ', e); return throwError(() => e); })
      );
  }

  joinRoom(roomId: number, password: string): Observable<RoomResponse> {
    const body: JoinRoomRequest = { password };
    return this.http.post<RoomResponse>(`/api/makao/rooms/${roomId}/join`, body)
      .pipe(
        tap(r => console.log('Join room response: ', r)),
        catchError(e => { console.error('Join room error: ', e); return throwError(() => e); })
      );
  }

  leaveRoom(roomId: number): Observable<void> {
    return this.http.delete<void>(`/api/makao/rooms/${roomId}/players/me`)
      .pipe(
        catchError(e => { console.error('Leave room error: ', e); return throwError(() => e); })
      );
  }

  startGame(roomId: number): Observable<GameResponse> {
    return this.http.post<GameResponse>(`/api/makao/rooms/${roomId}/start`, {})
      .pipe(
        tap(r => console.log('Start game response: ', r)),
        catchError(e => { console.error('Start game error: ', e); return throwError(() => e); })
      );
  }
}
