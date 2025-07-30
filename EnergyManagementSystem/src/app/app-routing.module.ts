import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignupComponent } from './signup/signup.component';
import { LoginComponent } from './login/login.component';
import { ClientsComponent } from './admin/components/clients/clients.component';
import { DevicesComponent } from './admin/components/devices/devices.component';
import { UpdateClientComponent } from './admin/components/update-client/update-client.component';
import { CreateClientComponent } from './admin/components/create-client/create-client.component';
import { CreateDeviceComponent } from './admin/components/create-device/create-device.component';
import { UpdateDeviceComponent } from './admin/components/update-device/update-device.component';
import { DashboardComponent } from './customer/components/dashboard/dashboard.component';
import { DeviceDetailComponent } from './customer/components/device-detail/device-detail.component';
import { MessagesComponent } from './customer/components/messages/messages.component';
import { ChatComponent } from './admin/components/chat/chat.component';


const routes: Routes = [ 
  { path: "signup", component: SignupComponent},
  { path: 'customer', loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule) },
  { path: 'admin', loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule) },
  { path: "login", component: LoginComponent},
  { path: 'admin/clients', component: ClientsComponent },
  { path: 'admin/devices', component: DevicesComponent },
  { path: 'api/admin/clients/:id', component: UpdateClientComponent },
  { path: 'admin/clients/create', component: CreateClientComponent },
  { path: 'admin/devices/create', component: CreateDeviceComponent },
  { path: 'admin/devices/:deviceId', component: UpdateDeviceComponent },
  { path: 'customer/dashboard', component: DashboardComponent },
  { path: 'customer/device-detail/:deviceId', component: DeviceDetailComponent },
  { path: 'customer/messages/:id', component: MessagesComponent },
  { path: 'admin/chat', component: ChatComponent},
  { path: 'customer/chat', component: ChatComponent}
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
