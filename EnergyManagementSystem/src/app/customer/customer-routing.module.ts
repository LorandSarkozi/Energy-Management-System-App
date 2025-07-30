import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerComponent } from './customer.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { DeviceDetailComponent } from './components/device-detail/device-detail.component';
import { MessagesComponent } from './components/messages/messages.component';
import { ChatComponent } from './components/chat/chat.component';


const routes: Routes = [
  { path: '', component: CustomerComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'device-detail/:deviceId', component: DeviceDetailComponent },
  { path: 'messages/:id', component: MessagesComponent },
  {path: 'customer/chat', component: ChatComponent}

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomerRoutingModule { }
