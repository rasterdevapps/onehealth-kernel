import { Injectable, OnDestroy } from '@angular/core';
import { Subject, Observable } from 'rxjs';

export interface LabResultNotification {
  orderId: string;
  patientId: string;
  tenantId: string;
  testType: string;
  result: string;
  completedAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class WebSocketService implements OnDestroy {

  private socket: WebSocket | null = null;
  private messageSubject = new Subject<LabResultNotification>();

  readonly messages$: Observable<LabResultNotification> = this.messageSubject.asObservable();

  connect(url: string): void {
    if (this.socket &&
        (this.socket.readyState === WebSocket.OPEN ||
         this.socket.readyState === WebSocket.CONNECTING)) {
      return;
    }
    this.socket = new WebSocket(url);

    this.socket.onopen = () => {
      console.log('[WebSocket] Connected to', url);
    };

    this.socket.onmessage = (event: MessageEvent) => {
      try {
        const notification: LabResultNotification = JSON.parse(event.data);
        this.messageSubject.next(notification);
      } catch (e) {
        console.error('[WebSocket] Failed to parse message:', event.data);
      }
    };

    this.socket.onerror = (error) => {
      console.error('[WebSocket] Error:', error);
    };

    this.socket.onclose = (event) => {
      console.log('[WebSocket] Connection closed:', event.code, event.reason);
    };
  }

  disconnect(): void {
    if (this.socket) {
      this.socket.close();
      this.socket = null;
    }
  }

  ngOnDestroy(): void {
    this.disconnect();
    this.messageSubject.complete();
  }
}
