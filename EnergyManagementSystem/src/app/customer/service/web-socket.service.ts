import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  // WebSocket clients for different functionalities
  private measurementClient: Client;
  private chatClient: Client;
  private typingClient: Client; // New WebSocket client for typing notifications
  private readClient: Client;

  // Subjects to emit messages from both WebSocket connections
  private measurementSubject: Subject<any> = new Subject<any>();
  private chatSubject: Subject<any> = new Subject<any>();
  private typingSubject: Subject<any> = new Subject<any>(); // New subject for typing status
  private readSubject: Subject<any> = new Subject<any>(); // New subject for read status

  constructor() {}

  // 👉 Connect to the WebSocket for device measurements
  connectToMeasurements(personId: string): void {
    this.measurementClient = new Client({
      brokerURL: 'http://measurement.localhost:80/notifications',
      reconnectDelay: 5000,
      debug: (msg) => console.log('STOMP Debug (Measurements):', msg),
    });

    this.measurementClient.onConnect = () => {
      console.log('Connected to measurement notifications');

      // Subscribe to device measurement notifications
      this.measurementClient.subscribe(`/topic/alerts/${personId}`, (message) => {
        this.measurementSubject.next(message.body);
      });
    };

    this.measurementClient.onStompError = (frame) => {
      console.error('STOMP Error (Measurements):', frame.headers['message']);
    };

    // Activate the WebSocket connection
    this.measurementClient.activate();
  }

  // 👉 Connect to the WebSocket for chat messages
  connectToChat(personId: string): void {
    this.chatClient = new Client({
      brokerURL: 'http://chat.localhost:80/chat',
      reconnectDelay: 5000,
      debug: (msg) => console.log('STOMP Debug (Chat):', msg),
    });

    this.chatClient.onConnect = () => {
      console.log('Connected to chat WebSocket');

      // Subscribe to chat messages
      this.chatClient.subscribe(`/topic/chat/${personId}`, (message) => {
        this.chatSubject.next(message.body);
      });
    };

    this.chatClient.onStompError = (frame) => {
      console.error('STOMP Error (Chat):', frame.headers['message']);
    };

    // Activate the WebSocket connection
    this.chatClient.activate();
  }

  // 👉 Connect to the WebSocket for typing notifications
  connectToTyping(personId: string): void {
    this.typingClient = new Client({
      brokerURL: 'http://chat.localhost:80/typing',
      reconnectDelay: 5000,
      debug: (msg) => console.log('STOMP Debug (Typing):', msg),
    });
  
    this.typingClient.onConnect = () => {
      console.log('Connected to typing notifications WebSocket');
  
      // Subscribe to typing notifications
      this.typingClient.subscribe(`/topic/typing/${personId}`, (message) => {
        // Directly handle the received message as an object (STOMP client auto-parses JSON)
        const typingStatus = message.body; // message.body should be an object if it's properly serialized
        console.log("TYPING STATUS: ", typingStatus);
        this.typingSubject.next(typingStatus);  // Send the typing status to your subscribers
      });
    };
  
    this.typingClient.onStompError = (frame) => {
      console.error('STOMP Error (Typing):', frame.headers['message']);
    };
  
    // Activate the WebSocket connection
    this.typingClient.activate();
  }

  connectToRead(personId: string): void {
    this.readClient = new Client({
      brokerURL: 'http://chat.localhost:80/read',
      reconnectDelay: 5000,
      debug: (msg) => console.log('STOMP Debug (Read):', msg),
    });
  
    this.readClient.onConnect = () => {
      console.log('Connected to read notifications WebSocket');
  
      // Subscribe to typing notifications
      this.readClient.subscribe(`/topic/read/${personId}`, (message) => {
        // Directly handle the received message as an object (STOMP client auto-parses JSON)
        const readStatus = message.body; // message.body should be an object if it's properly serialized
        console.log("READ STATUS: ", readStatus);
        this.readSubject.next(readStatus);  // Send the typing status to your subscribers
      });
    };
  
    this.readClient.onStompError = (frame) => {
      console.error('STOMP Error (Read):', frame.headers['message']);
    };
  
    // Activate the WebSocket connection
    this.readClient.activate();
  }
  

  // 👉 Observable to listen for measurement notifications
  get measurementMessages$() {
    return this.measurementSubject.asObservable();
  }

  // 👉 Observable to listen for chat messages
  get chatMessages$() {
    return this.chatSubject.asObservable();
  }

  // 👉 Observable to listen for typing notifications
  get typingMessages$() {
    return this.typingSubject.asObservable(); // Exposing the typing status as observable
  }

  get readMessages$() {
    return this.readSubject.asObservable(); // Exposing the typing status as observable
  }

  // 👉 Send typing status (true for typing, false for stop typing)
  sendTypingStatus(personId: string, typing: boolean): void {
    const message = { personId, typing };
  
    // Use the 'publish' method to send the message to the specified destination
    this.typingClient.publish({
      destination: `/app/typing/${personId}`,
      body: JSON.stringify(message),
    });
  }

  sendReadStatus(personId: string, read: boolean): void {
    const message = { personId, read };
    
    // Send a "Read" status to the server
    this.readClient.publish({
      destination: `/app/read/${personId}`,
      body: JSON.stringify(message),
    });
  }

  // Disconnect WebSocket connections
  disconnect(): void {
    if (this.measurementClient) {
      this.measurementClient.deactivate();
      console.log('Measurement WebSocket disconnected');
    }

    if (this.chatClient) {
      this.chatClient.deactivate();
      console.log('Chat WebSocket disconnected');
    }

    if (this.typingClient) {
      this.typingClient.deactivate();
      console.log('Typing WebSocket disconnected');
    }

    if(this.readClient) {
      this.readClient.deactivate();
      console.log("Read Websocket Disconnected");
    }
  }
}
