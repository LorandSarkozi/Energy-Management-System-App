import { Component, OnInit, OnDestroy } from '@angular/core';
import { WebSocketService } from 'src/app/customer/service/web-socket.service';
import { UserStorageService } from 'src/app/services/storage/user-storage.service';
import { Subscription } from 'rxjs'; // Import Subscription to handle unsubscription

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.scss']
})
export class MessagesComponent implements OnInit, OnDestroy {
  messages: string[] = [];
  personId: string;
  private messageSubscription: Subscription; // Subscription to WebSocket messages

  constructor(
    private websocketService: WebSocketService,
    private userStorageService: UserStorageService
  ) {}

  ngOnInit(): void {
    // Get the personId from user storage
    this.personId = UserStorageService.getUserId();

    // Connect to WebSocket and listen to the topic
    this.websocketService.connectToMeasurements(this.personId);

    // Subscribe to WebSocket messages using the observable from WebSocketService
    this.messageSubscription = this.websocketService.measurementMessages$.subscribe((message: string) => {
      this.addMessage(message); // Add incoming message to the list
    });
  }

  ngOnDestroy(): void {
    // Disconnect from WebSocket and unsubscribe from message updates when the component is destroyed
    this.websocketService.disconnect();
    if (this.messageSubscription) {
      this.messageSubscription.unsubscribe();
    }
  }

  addMessage(message: string): void {
    this.messages.push(message); // Add message to the messages array
  }
}
