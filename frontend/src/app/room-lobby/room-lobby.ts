import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, switchMap } from 'rxjs';

import { RoomResponse } from '../models/room-response';
import { GameResponse } from '../models/game-response';
import { RoomService } from '../services/room-service';
import { GameSocketService } from '../services/game-socket-service';

@Component({
  selector: 'app-room-lobby',
  standalone: true,
  imports: [],
  templateUrl: './room-lobby.html',
  styleUrl: './room-lobby.css'
})
export class RoomLobby implements OnInit, OnDestroy {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private roomService = inject(RoomService);
  private gameSocketService = inject(GameSocketService);

  private subscriptions: Subscription[] = [];

  roomId = Number(this.route.snapshot.paramMap.get('roomId'));
  room = signal<RoomResponse | null>(null);

  ngOnInit(): void {
    this.roomService.getRoom(this.roomId).subscribe(room => this.room.set(room));

    const connected = this.gameSocketService.connect();

    const roomSub = connected.pipe(
      switchMap(() => this.gameSocketService.subscribe<RoomResponse>(`/topic/rooms/${this.roomId}`))
    ).subscribe(room => this.room.set(room));

    const startedSub = connected.pipe(
      switchMap(() => this.gameSocketService.subscribe<GameResponse>(`/topic/rooms/${this.roomId}/started`))
    ).subscribe(game => this.router.navigate(['/game', game.id]));

    this.subscriptions.push(roomSub, startedSub);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
    this.gameSocketService.disconnect();
  }

  startGame(): void {
    this.roomService.startGame(this.roomId).subscribe({
      error: e => alert(e?.error?.message ?? 'Nie udało się rozpocząć gry')
    });
  }

  leaveRoom(): void {
    this.roomService.leaveRoom(this.roomId).subscribe({
      next: () => this.router.navigate(['/rooms']),
      error: e => alert(e?.error?.message ?? 'Błąd opuszczania pokoju')
    });
  }
}
