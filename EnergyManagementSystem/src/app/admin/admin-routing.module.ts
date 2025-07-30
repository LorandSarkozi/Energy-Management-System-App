import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { UpdateClientComponent } from './components/update-client/update-client.component';
import { DevicesComponent } from './components/devices/devices.component';
import { UpdateDeviceComponent } from './components/update-device/update-device.component';
import { ChatComponent } from './components/chat/chat.component';

const routes: Routes = [
  { path: '', component: AdminComponent },
  { path: 'dashboard', component: DashboardComponent },
  {path: 'api/admin/clients/:id', component: UpdateClientComponent},
  {path: 'admin/devices', component: DevicesComponent},
  {path: 'admin/devices/:id', component: UpdateDeviceComponent},
  {path: 'admin/chat', component: ChatComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
