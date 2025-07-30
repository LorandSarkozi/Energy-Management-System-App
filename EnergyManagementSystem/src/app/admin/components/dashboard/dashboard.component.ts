import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  constructor(private router: Router) {}

  navigateToClients() {
    this.router.navigate(['/admin/clients']);
  }

  navigateToDevices() {
    this.router.navigate(['/admin/devices']);
  }

  navigateToChat(){
    this.router.navigate(['/admin/chat']);
  }

}
