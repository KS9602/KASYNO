import { Component, inject, signal } from '@angular/core';
import { FormArray, FormBuilder, FormControl, ReactiveFormsModule } from '@angular/forms';

import { Card } from '../card/card';
import { CardModel } from '../models/card-model';
import { BoardService } from '../services/board-service'
import { CreateGameResponse } from '../models/create-game-response'
import { StartRoundRequest } from '../models/start-round-request'



@Component({
  selector: 'app-board',
  standalone: true,
  imports: [Card, ReactiveFormsModule],
  templateUrl: './board.html',
  styleUrl: './board.css'
})
export class Board {

  private fb = inject(FormBuilder);
  private boardService = inject(BoardService);

  formPlayers = this.fb.group({
    players: this.fb.array<FormControl<string>>([])
  })
  get players(): FormArray<FormControl<string>>{
    return this.formPlayers.get('players') as FormArray<FormControl<string>>
  }
  addPlayer(){
    this.players.push(new FormControl('', { nonNullable: true }));
  }
  save(){
    console.log(this.players.value)
  }


  formDrawAmount = this.fb.group({
    amount: this.fb.control(1, { nonNullable:true })
  })
  get amount(): FormControl<number> {
    return this.formDrawAmount.get("amount") as FormControl<number>;
  }




  cards = signal<CardModel[]>([]);
  gameIdS = signal<number | null>(null);
  roundIdS = signal<number | null>(null);


startGame(): void{
  this.boardService.startGame(this.players.value  as string[]).subscribe({
    next: r => {
      this.gameIdS.set(r.gameId)
    }
  })
}

startRound(): void{
  const gameId = this.gameIdS()
  if(!gameId){
    console.error("Game does not exist")
    return;
  }
  this.boardService.startRound(gameId).subscribe({
    next: r => {
      this.roundIdS.set(r.roundId)
    }
  })
  console.log("Runda: ",this.roundIdS())
}

draw(): void{
  const gameId = this.gameIdS()
  const roundId = this.roundIdS()
  const amount = this.amount.value

  if(!gameId || !roundId){
    console.log("Error while drawing cards")
    return;
  }
  this.boardService.draw(gameId, roundId, amount).subscribe({
    next: r => {
      this.cards.set(r)
    }
  })

}

}