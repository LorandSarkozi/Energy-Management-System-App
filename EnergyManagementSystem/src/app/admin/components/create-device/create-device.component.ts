import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../service/admin.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-create-device',
  templateUrl: './create-device.component.html',
  styleUrls: ['./create-device.component.scss']
})
export class CreateDeviceComponent {

  deviceForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private adminService: AdminService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    // Initialize the form
    this.deviceForm = this.formBuilder.group({
      description: ['', Validators.required],
      address: ['', Validators.required],
      maximumHourlyEnergyConsumption: [0, [Validators.required, Validators.min(0)]],
      personId: [''] // Optional field
    });
  }

  // Method to create a device
  createDevice(): void {
    if (this.deviceForm.valid) {
      this.adminService.createDevice(this.deviceForm.value).subscribe(
        (response) => {
          this.showSnackBar('Device created successfully');
          this.router.navigate(['/admin/devices']); // Redirect to devices list
        },
        (error) => {
          console.error('Error creating device', error);
          this.showSnackBar('Error creating device');
        }
      );
    } else {
      this.showSnackBar('Please fill in all required fields');
    }
  }

  // Navigate back to the devices list
  goBack(): void {
    this.router.navigate(['/admin/devices']);
  }

  // Show a message at the bottom of the page
  private showSnackBar(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
    });
  }

}
