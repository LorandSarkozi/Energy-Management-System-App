import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminService } from '../../service/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-update-device',
  templateUrl: './update-device.component.html',
  styleUrls: ['./update-device.component.scss']
})
export class UpdateDeviceComponent implements OnInit{

  deviceForm: FormGroup;
  deviceId: string;

  constructor(
    private route: ActivatedRoute,
    private adminService: AdminService,
    private formBuilder: FormBuilder,
    private router: Router,
    private snackBar: MatSnackBar,
  ) {
    this.deviceForm = this.formBuilder.group({
      description: ['', Validators.required],
      address: ['', Validators.required],
      maximumHourlyEnergyConsumption: ['', [Validators.required, Validators.min(0)]],
      personId: [''] 
    });
  }

  ngOnInit(): void {
    this.deviceId = this.route.snapshot.paramMap.get('deviceId');
    this.getDeviceData();
  }
  


  getDeviceData(): void {
    // Fetch the client data using the admin service
    this.adminService.getAllDevices().subscribe(devices => {
      const device = devices.find(c => c.deviceId === this.deviceId);
      if (device) {
        this.deviceForm.patchValue({
          description: device.description,
          address: device.address,
          maximumHourlyEnergyConsumption: device.maximumHourlyEnergyConsumption,
          personId: device.personId,
        });
      }
    });
  }

  onSubmit(): void {
    if (this.deviceForm.valid) {
      this.adminService.editDevice(this.deviceId, this.deviceForm.value).subscribe(() => {
        this.snackBar.open('Device updated successfully', 'Close', {
          duration: 3000,
        });
        this.router.navigate(['admin/devices']);
      });
    }
  }

  goBack(): void {
    this.router.navigate(['admin/devices']);
  }
}
