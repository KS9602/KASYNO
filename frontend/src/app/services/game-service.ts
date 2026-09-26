import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { GameStateResponse } from '../models/game-state-response';
import { PlayCardRequest } from '../models/play-card-request';
import { CardSuit, CardType } from '../models/card-response';

@Injectable({
  providedIn: 'root',
})
export class GameService {
  private http = inject(HttpClient);

  getGameState(gameId: number): Observable<GameStateResponse> {
    return this.http.get<GameStateResponse>(`/api/makao/games/${gameId}/state`)
      .pipe(
        catchError(e => { console.error('Get game state error: ', e); return throwError(() => e); })
      );
  }

  playCards(
    gameId: number,
    cardIds: number[],
    requestedRank: CardType | null = null,
    requestedSuit: CardSuit | null = null
  ): Observable<GameStateResponse> {
    const body: PlayCardRequest = { cardIds, requestedRank, requestedSuit };
    return this.http.post<GameStateResponse>(`/api/makao/games/${gameId}/actions/play`, body)
      .pipe(
        tap(r => console.log('Play cards response: ', r)),
        catchError(e => { console.error('Play cards error: ', e); return throwError(() => e); })
      );
  }

  draw(gameId: number): Observable<GameStateResponse> {
    return this.http.post<GameStateResponse>(`/api/makao/games/${gameId}/actions/draw`, {})
      .pipe(
        tap(r => console.log('Draw response: ', r)),
        catchError(e => { console.error('Draw error: ', e); return throwError(() => e); })
      );
  }

  pass(gameId: number): Observable<GameStateResponse> {
    return this.http.post<GameStateResponse>(`/api/makao/games/${gameId}/actions/pass`, {})
      .pipe(
        catchError(e => { console.error('Pass error: ', e); return throwError(() => e); })
      );
  }

  callMakao(gameId: number): Observable<GameStateResponse> {
    return this.http.post<GameStateResponse>(`/api/makao/games/${gameId}/actions/makao`, {})
      .pipe(
        catchError(e => { console.error('Makao error: ', e); return throwError(() => e); })
      );
  }

  stopMakao(gameId: number, targetPlayerId: number): Observable<GameStateResponse> {
    return this.http.post<GameStateResponse>(
      `/api/makao/games/${gameId}/actions/stop-makao?targetPlayerId=${targetPlayerId}`, {}
    ).pipe(
      catchError(e => { console.error('Stop makao error: ', e); return throwError(() => e); })
    );
  }
}
