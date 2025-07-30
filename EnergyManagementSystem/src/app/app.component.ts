import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserStorageService } from './services/storage/user-storage.service';




@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  showWelcomeMessage: boolean;
  title = 'EnergyManagementSystem';

  isCustomerLoggedIn: boolean = UserStorageService.isCustomerLoggedIn();
  isAdminLoggedIn: boolean = UserStorageService.isAdminLoggedIn();

  constructor(private router: Router) {
    // Subscribe to router events to check the current route
    this.router.events.subscribe(() => {
      // Set showWelcomeMessage based on the current route
      this.showWelcomeMessage = this.router.url === '/';
    });
  }

  ngOnInit(): void{
    this.router.events.subscribe( event => {
      this.isCustomerLoggedIn=UserStorageService.isCustomerLoggedIn();
      this.isAdminLoggedIn = UserStorageService.isAdminLoggedIn();
    })
  }

  navigateToChatAdmin(){
    this.router.navigate(['/admin/chat']);
  }

  navigateToChatCustomer(){
    this.router.navigate(['/customer/chat']);
  }

  logout(){
    UserStorageService.signOut();
    this.router.navigateByUrl('login');
  }
}
