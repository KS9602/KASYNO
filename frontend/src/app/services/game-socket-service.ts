import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class GameSocketService {
  private client: Client | null = null;
  private connected = new Subject<void>();

  connect(): Observable<void> {
    if (!this.client) {
      const protocol = window.location.protocol === 'https:' ? 'wss' : 'ws';

      this.client = new Client({
        brokerURL: `${protocol}://${window.location.host}/ws`,
        reconnectDelay: 5000,
        onConnect: () => this.connected.next(),
        onStompError: frame => console.error('Stomp error: ', frame),
      });

      this.client.activate();
    }

    return this.connected.asObservable();
  }

  disconnect(): void {
    this.client?.deactivate();
    this.client = null;
  }

  subscribe<T>(destination: string): Observable<T> {
    return new Observable(subscriber => {
      if (!this.client) {
        subscriber.error('Socket not connected');
        return;
      }

      const subscription = this.client.subscribe(destination, (message: IMessage) => {
        subscriber.next(JSON.parse(message.body) as T);
      });

      return () => subscription.unsubscribe();
    });
  }
}
