import { Component, OnDestroy, OnInit, computed, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription, switchMap } from 'rxjs';

import { Card } from '../card/card';
import { CardResponse, CardSuit, CardType } from '../models/card-response';
import { GameStateResponse } from '../models/game-state-response';
import { GameService } from '../services/game-service';
import { GameSocketService } from '../services/game-socket-service';
import { AuthService } from '../services/auth-service';

@Component({
  selector: 'app-board',
  standalone: true,
  imports: [Card],
  templateUrl: './board.html',
  styleUrl: './board.css'
})
export class Board implements OnInit, OnDestroy {

  private route = inject(ActivatedRoute);
  private gameService = inject(GameService);
  private gameSocketService = inject(GameSocketService);
  private authService = inject(AuthService);

  private subscriptions: Subscription[] = [];

  gameId = Number(this.route.snapshot.paramMap.get('gameId'));
  myPlayerId = signal<number | null>(null);
  state = signal<GameStateResponse | null>(null);
  selectedCardIds = signal<number[]>([]);
  chosenRank = signal<CardType | null>(null);
  chosenSuit = signal<CardSuit | null>(null);
  targetPlayerId = signal<number | null>(null);

  nonFunctionalRanks: CardType[] = ['FIVE', 'SIX', 'SEVEN', 'EIGHT', 'NINE', 'TEN'];
  suits: CardSuit[] = ['HEARTS', 'DIAMONDS', 'CLUBS', 'SPADES'];

  isMyTurn = computed(() => this.state()?.currentPlayerId === this.myPlayerId());

  lastSelectedCard = computed<CardResponse | null>(() => {
    const ids = this.selectedCardIds();
    if (!ids.length) {
      return null;
    }
    const lastId = ids[ids.length - 1];
    return this.state()?.myCards.find(c => c.id === lastId) ?? null;
  });

  ngOnInit(): void {
    this.authService.whoAmI().subscribe(me => {
      this.myPlayerId.set(me.id);
      this.loadState();
      this.connectSocket();
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
    this.gameSocketService.disconnect();
  }

  private loadState(): void {
    this.gameService.getGameState(this.gameId).subscribe(state => this.state.set(state));
  }

  private connectSocket(): void {
    const topic = `/topic/games/${this.gameId}/${this.myPlayerId()}`;
    const sub = this.gameSocketService.connect().pipe(
      switchMap(() => this.gameSocketService.subscribe<GameStateResponse>(topic))
    ).subscribe(state => this.state.set(state));
    this.subscriptions.push(sub);
  }

  toggleCard(card: CardResponse): void {
    const ids = this.selectedCardIds();
    if (ids.includes(card.id)) {
      this.selectedCardIds.set(ids.filter(id => id !== card.id));
    } else {
      this.selectedCardIds.set([...ids, card.id]);
    }
  }

  playSelected(): void {
    const ids = this.selectedCardIds();
    if (!ids.length) {
      return;
    }
    this.gameService.playCards(this.gameId, ids, this.chosenRank(), this.chosenSuit()).subscribe({
      next: state => {
        this.state.set(state);
        this.selectedCardIds.set([]);
        this.chosenRank.set(null);
        this.chosenSuit.set(null);
      },
      error: e => alert(e?.error?.message ?? 'Nie można zagrać tych kart')
    });
  }

  draw(): void {
    this.gameService.draw(this.gameId).subscribe({
      next: state => this.state.set(state),
      error: e => alert(e?.error?.message ?? 'Błąd dobierania kart')
    });
  }

  pass(): void {
    this.gameService.pass(this.gameId).subscribe({
      next: state => this.state.set(state),
      error: e => alert(e?.error?.message ?? 'Nie ma czego pasować')
    });
  }

  callMakao(): void {
    this.gameService.callMakao(this.gameId).subscribe({
      next: state => this.state.set(state),
      error: e => alert(e?.error?.message ?? 'Nie możesz teraz powiedzieć makao')
    });
  }

  stopMakao(): void {
    const targetId = this.targetPlayerId();
    if (!targetId) {
      return;
    }
    this.gameService.stopMakao(this.gameId, targetId).subscribe({
      next: state => this.state.set(state),
      error: e => alert(e?.error?.message ?? 'Błąd stop makao')
    });
  }
}
