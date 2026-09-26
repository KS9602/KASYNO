import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { RoomResponse } from '../models/room-response';
import { RoomService } from '../services/room-service';

@Component({
  selector: 'app-rooms-list',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './rooms-list.html',
  styleUrl: './rooms-list.css'
})
export class RoomsList implements OnInit {

  private fb = inject(FormBuilder).nonNullable;
  private roomService = inject(RoomService);
  private router = inject(Router);

  rooms = signal<RoomResponse[]>([]);

  createForm = this.fb.group({
    name: ['', Validators.required],
    password: ['', Validators.required],
    maxPlayers: [4, [Validators.required, Validators.min(2)]]
  });

  ngOnInit(): void {
    this.loadRooms();
  }

  loadRooms(): void {
    this.roomService.getRooms().subscribe(rooms => this.rooms.set(rooms));
  }

  createRoom(): void {
    if (this.createForm.invalid) {
      this.createForm.markAllAsTouched();
      return;
    }
    const form = this.createForm.value;
    this.roomService.createRoom(form.name!, form.password!, form.maxPlayers!).subscribe({
      next: room => this.router.navigate(['/rooms', room.id]),
      error: e => alert(e?.error?.message ?? 'Nie udało się utworzyć pokoju')
    });
  }

  joinRoom(room: RoomResponse): void {
    const password = prompt(`Hasło do pokoju "${room.name}":`) ?? '';
    this.roomService.joinRoom(room.id, password).subscribe({
      next: () => this.router.navigate(['/rooms', room.id]),
      error: e => alert(e?.error?.message ?? 'Nie udało się dołączyć do pokoju')
    });
  }
}
