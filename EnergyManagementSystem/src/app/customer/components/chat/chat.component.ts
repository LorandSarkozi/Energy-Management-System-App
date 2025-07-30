import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AdminService } from 'src/app/admin/service/admin.service';
import { UserStorageService } from 'src/app/services/storage/user-storage.service';
import { WebSocketService } from '../../service/web-socket.service';

interface Message {
  sender: string;
  content: string;
  timestamp?: string;
}

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {
  searchClientForm!: FormGroup;
  clients: any[] = [];
  typingStatus: string | null = null;
  readStatus: string | null = null;
  selectedClient: any = null;

  senderMessages: Message[] = [];
  receiverMessages: Message[] = [];
  chatMessages: Message[] = [];

  chatForm: FormGroup;
  loading = false;

  constructor(
    private adminService: AdminService,
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router,
    private webSocketService: WebSocketService // Inject WebSocketService
  ) {
    this.chatForm = this.fb.group({
      newMessage: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.getAllClients();
    this.searchClientForm = this.fb.group({
      title: [null, [Validators.required]]
    });

    // Connect to the WebSocket
    const userId = UserStorageService.getUserId();
    this.webSocketService.connectToChat(userId);

    // Listen for incoming chat messages
    this.webSocketService.chatMessages$.subscribe((message) => {
      const receivedMessage: Message = JSON.parse(message);
      if (receivedMessage.sender === this.selectedClient.id) {
        this.receiverMessages.push(receivedMessage);
        this.chatMessages.push(receivedMessage);
      }
    });

  }

  getAllClients(): void {
    this.loading = true;
    this.adminService.getAllClients().subscribe(
      (response) => {
        this.clients = response;
        console.log('All Clients:', this.clients);
        this.loading = false;
      },
      (error) => {
        this.snackBar.open('Error fetching clients', 'Close', { duration: 3000 });
        this.loading = false;
      }
    );
  }

  selectClient(client: any) {
    this.selectedClient = client;
    this.chatMessages = [];
    this.senderMessages = [];
    this.receiverMessages = [];
    this.getChatHistory();

    const userId = UserStorageService.getUserId();
    // ✅ Connect to the typing WebSocket for the selected client
    this.webSocketService.connectToTyping(userId);
    this.webSocketService.connectToRead(userId);

    // ✅ Listen for typing notifications for the selected client
    this.webSocketService.typingMessages$.subscribe((typingNotification) => {
      // Ensure that the message is parsed as JSON if it's a string
      const parsedNotification = JSON.parse(typingNotification);
    
      console.log('Received typing notification:', parsedNotification);
      console.log("TYPING SENDER: ", parsedNotification.personId);
      console.log("TYPING ISTYPING: ", parsedNotification.typing);
    
      if (parsedNotification.personId === userId && parsedNotification.typing) {
        this.typingStatus = `${this.selectedClient.name} is typing...`;
      } else {
        this.typingStatus = null;
      }
    });

    this.webSocketService.readMessages$.subscribe((readNotification) => {
      // Ensure that the message is parsed as JSON if it's a string
      const parsedNotification = JSON.parse(readNotification);
    
      console.log('Received read notification:', parsedNotification);
      console.log("READ SENDER: ", parsedNotification.personId);
      console.log("READ ISREAD: ", parsedNotification.read);
    
      if (parsedNotification.personId === userId && parsedNotification.read) {
        this.readStatus = `Read`;
      } else {
        this.readStatus = null;
      }
    });


  }

  getChatHistory() {
    this.adminService.getChatHistory(UserStorageService.getUserId(), this.selectedClient.id).subscribe(
      (response: Message[]) => {
        this.chatMessages = response;
        console.log('Chat history:', this.chatMessages);

        // Separate sender and receiver messages
        const userId = UserStorageService.getUserId();
        this.senderMessages = response.filter(message => message.sender === userId);
        this.receiverMessages = response.filter(message => message.sender !== userId);
      },
      (error) => {
        this.snackBar.open('Error fetching chat history', 'Close', { duration: 3000 });
      }
    );
  }

  sendMessage() {
    if (this.chatForm.valid) {
      const newMessage: Message = {
        sender: UserStorageService.getUserId(),
        content: this.chatForm.value.newMessage,
        timestamp: new Date().toISOString()
      };

      // Send the message through WebSocket
      if (this.webSocketService['chatClient'] && this.webSocketService['chatClient'].connected) {
        this.webSocketService['chatClient'].publish({
          destination: `/app/chat/${this.selectedClient.id}`,
          body: JSON.stringify(newMessage),
        });
      }

      // Update local message lists
      this.chatMessages.push(newMessage);
      this.senderMessages.push(newMessage);

      // Reset the form
      if (this.webSocketService['readClient'] && this.webSocketService['readClient'].connected) {
        this.webSocketService.sendReadStatus(UserStorageService.getUserId(), false); // Notify that the user stopped typing
      }
      this.chatForm.reset();

      // Update receiverMessages if necessary
      this.receiverMessages = this.chatMessages.filter(message => message.sender !== UserStorageService.getUserId());
  
      // No need to call getChatHistory() here as you've already added the new message
      this.adminService.sendMessageToClient(this.selectedClient.id, newMessage.content).subscribe(
        () => {
          console.log('Message sent successfully');
        },
        (error) => {
          console.error('Error sending message', error);
          this.snackBar.open('Error sending message', 'Close', { duration: 3000 });
        }
      );
    } else {
      console.error('Form is invalid');
    }
  }

  onTyping() {
    if (this.webSocketService['typingClient'] && this.webSocketService['typingClient'].connected) {
      this.webSocketService.sendTypingStatus(this.selectedClient.id, true); // Notify that the user is typing
    }

    if (this.webSocketService['readClient'] && this.webSocketService['readClient'].connected) {
      this.webSocketService.sendReadStatus(this.selectedClient.id, true); // Notify that the user read
    }
  }

  onStopTyping() {
    if (this.webSocketService['typingClient'] && this.webSocketService['typingClient'].connected) {
      this.webSocketService.sendTypingStatus(this.selectedClient.id, false); // Notify that the user stopped typing
    }

  }

  goBack() {
    this.router.navigate(['/customer/dashboard']);
  }
}
