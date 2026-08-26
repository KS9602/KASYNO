import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, tap, throwError  } from 'rxjs';
import { CreateGameRequest } from '../models/create-game-request'
import { CreateGameResponse } from '../models/create-game-response'
import { StartRoundRequest } from '../models/start-round-request'
import { StartRoundResponse } from '../models/start-round-response'
import { DrawCardsRequest } from '../models/draw-cards-request'
import { CardModel } from '../models/card-model'

@Injectable({
  providedIn: 'root',
})
export class BoardService {
  private http = inject(HttpClient);


  startGame(players: string[]): Observable<CreateGameResponse>{
      const body: CreateGameRequest = {
      players: players
    };
    return this.http.post<CreateGameResponse>('/api/poker/start-game', body)
      .pipe(
        tap(r => console.log("Start game response: ", r)),
        catchError(e => {console.error("Start game Errror: ", e)
          return throwError(() => e);
        })
      )
  }

  startRound(gameId: number): Observable<StartRoundResponse>{
    const body: StartRoundRequest = {
      gameId: gameId
    };
    return this.http.post<StartRoundResponse>('/api/poker/start-round', body)
      .pipe(
        tap(r => console.log("Start round response: ", r)),
        catchError(e => {console.error("Start round error: ", e)
          return throwError(() => e);
        })
      )
  }

  draw(gameId: number, roundId: number, amount: number): Observable<CardModel[]> {
    const body: DrawCardsRequest = {
      gameId: gameId,
      roundId: roundId,
      amount: amount
    }
          return this.http.post<CardModel[]>('/api/poker/draw', body)
      .pipe(
        tap(r => console.log("Draw response: ", r)),
        catchError(e => {console.error("Draw error: ", e)
          return throwError(() => e);
        })
      )
  }


}