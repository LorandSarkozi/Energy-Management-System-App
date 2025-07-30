import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AdminService } from 'src/app/admin/service/admin.service';
import { UserStorageService } from 'src/app/services/storage/user-storage.service';
import { WebSocketService } from '../../service/web-socket.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, OnDestroy {

  devices: any[] = []; // Array to hold devices
  userId: string = UserStorageService.getUserId();
  private messageSubscription: Subscription;

  constructor(
    private adminService: AdminService,
    private snackBar: MatSnackBar,
    private router: Router,
    private webSocketService: WebSocketService
  ) {}

  ngOnInit(): void {
    this.loadDevices();
    this.initializeWebSocket();
  }

  ngOnDestroy(): void {
    this.webSocketService.disconnect(); // Disconnect WebSocket when the component is destroyed
    if (this.messageSubscription) {
      this.messageSubscription.unsubscribe(); // Unsubscribe from message updates
    }
  }

  loadDevices(): void {
    if (this.userId) {
      this.adminService.getDevicesByCustomerId(this.userId).subscribe(
        (devices) => {
          this.devices = devices; // Store devices in the component
        },
        (error) => {
          this.snackBar.open('Error loading devices', 'Close', {
            duration: 3000,
          });
        }
      );
    } else {
      this.snackBar.open('No customer ID found', 'Close', {
        duration: 3000,
      });
    }
  }

  initializeWebSocket(): void {
    if (this.userId) {
      this.webSocketService.connectToMeasurements(this.userId);

      // Subscribe to WebSocket messages
      this.messageSubscription = this.webSocketService.measurementMessages$.subscribe((message: string) => {
        // Show the notification as a snack bar
        this.snackBar.open(message, 'Close', {
          duration: 8000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
        });
      });
    } else {
      console.error('User ID not found. Unable to initialize WebSocket.');
    }
  }

  inspectDevice(deviceId: any): void {
    console.log('Inspecting device with ID:', deviceId);
    this.router.navigate(['/customer/device-detail', deviceId]);
  }

  messagesUser(userId: any): void {
    this.router.navigate(['/customer/messages', userId]);
  }

  navigateToChat(){
    this.router.navigate(['/customer/chat']);
  }

}
