import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../service/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';
 // Assume you have a Snackbar service for notifications

@Component({
  selector: 'app-update-client',
  templateUrl: './update-client.component.html',
  styleUrls: ['./update-client.component.scss']
})
export class UpdateClientComponent implements OnInit {
  clientForm: FormGroup;
  clientId: string;

  constructor(
    private route: ActivatedRoute,
    private adminService: AdminService,
    private formBuilder: FormBuilder,
    private router: Router,
    private snackBar: MatSnackBar,
  ) {
    this.clientForm = this.formBuilder.group({
      name: ['', Validators.required],
      address: ['', Validators.required],
      age: ['', Validators.required],
      role: ['CUSTOMER', Validators.required], // Default role
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.clientId = this.route.snapshot.paramMap.get('id');
    this.getClientData();
  }

  getClientData(): void {
    // Fetch the client data using the admin service
    this.adminService.getAllClients().subscribe(clients => {
      const client = clients.find(c => c.id === this.clientId);
      if (client) {
        this.clientForm.patchValue({
          name: client.name,
          address: client.address,
          age: client.age,
          role: client.role,
          password: '' // Password should be empty for security reasons
        });
      }
    });
  }

  onSubmit(): void {
    if (this.clientForm.valid) {
      this.adminService.editClient(this.clientId, this.clientForm.value).subscribe(() => {
        this.snackBar.open('Client updated successfully', 'Close', {
          duration: 3000,
        });
        this.router.navigate(['admin/clients']);
      });
    }
  }

  goBack(): void {
    this.router.navigate(['admin/clients']);
  }
}
