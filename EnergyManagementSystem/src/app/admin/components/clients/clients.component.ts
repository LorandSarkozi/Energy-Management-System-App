import { Component } from '@angular/core';
import { AdminService } from '../../service/admin.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.scss']
})
export class ClientsComponent {


  searchClientForm!: FormGroup;
  clients: any[] = [];

  constructor(
    private adminService: AdminService,
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router
  ){}

  ngOnInit(){
    this.getAllClients();
    this.searchClientForm = this.fb.group({
      title:[null, [Validators.required]]
    })
  }

  getAllClients(): void {
    this.adminService.getAllClients().subscribe(
      (response) => {
        // Filter clients to show only those with the 'CUSTOMER' role
        this.clients = response.filter(client => client.role === 'CUSTOMER');
        console.log('Filtered Clients:', this.clients); // Log the filtered clients
      },
      (error) => {
        console.error('Error fetching clients', error);
      }
    );
  }

  goToCreateClient() {
    this.router.navigate(['/admin/clients/create']);
  }
  

  // Search for a client
onSearchClient() {
  const title = this.searchClientForm.get('title')?.value;
  if (title) {
    this.adminService.searchClientByTitle(title).subscribe(res => {
      this.clients = res;
    });
  } else {
    this.getAllClients(); // Show all clients if no title is entered
  }
}

// Edit client (implement the edit logic as needed)
onEditClient(client: any) {
  this.router.navigate(['/api/admin/clients/' + client.id]);

}

// Delete client
onDeleteClient(clientId: string) {
  this.adminService.deleteClient(clientId).subscribe(() => {
    this.snackBar.open('Client deleted successfully', 'Close', {
      duration: 3000,
    });
    this.getAllClients(); // Refresh the client list
  });
}

goBack() {
  this.router.navigate(['/admin/dashboard']);  // Adjust the route based on your setup
}
}
