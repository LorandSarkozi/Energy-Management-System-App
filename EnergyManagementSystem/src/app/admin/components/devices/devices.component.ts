import { Component } from '@angular/core';
import { AdminService } from '../../service/admin.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-devices',
  templateUrl: './devices.component.html',
  styleUrls: ['./devices.component.scss']
})
export class DevicesComponent {

  devices: any[] = [];

  constructor(
    private deviceService: AdminService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.getAllDevices();
  }

  // Fetch all devices
  getAllDevices(): void {
    this.deviceService.getAllDevices().subscribe(
      (response) => {
        this.devices = response; // Assign the response data to the devices array
        console.log('Devices fetched successfully', this.devices); // Log the fetched devices
      },
      (error) => {
        console.error('Error fetching devices', error); // Log any errors that occur
      }
    );
  }

  // Navigate to Create Device form
  goToCreateDevice(): void {
    this.router.navigate(['/admin/devices/create']);
  }

  // Edit a device by navigating to the Edit Device form
  onEditDevice(device: any): void {
    this.router.navigate(['/admin/devices/' + device.deviceId]);
  }

  // Delete a device and reload the devices list
  onDeleteDevice(deviceId: string) {
    this.deviceService.deleteDevice(deviceId).subscribe(() => {
      this.snackBar.open('Client deleted successfully', 'Close', {
        duration: 3000,
      });
      this.getAllDevices(); // Refresh the client list
    });
  }

  // Navigate back to the previous page
  goBack(): void {
    this.router.navigate(['/admin/dashboard']);
  }

  // Show a message at the bottom of the page
  private showSnackBar(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
    });
  }

}
