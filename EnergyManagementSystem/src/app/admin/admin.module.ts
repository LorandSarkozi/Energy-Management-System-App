import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { AdminComponent } from './admin.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ClientsComponent } from './components/clients/clients.component';
import { DevicesComponent } from './components/devices/devices.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ReactiveFormsModule } from '@angular/forms';
import { UpdateClientComponent } from './components/update-client/update-client.component';
import { MatSelectModule } from '@angular/material/select';
import { CreateClientComponent } from './components/create-client/create-client.component';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { CreateDeviceComponent } from './components/create-device/create-device.component';
import { UpdateDeviceComponent } from './components/update-device/update-device.component';
import { ChatComponent } from './components/chat/chat.component';



@NgModule({
  declarations: [
    AdminComponent,
    DashboardComponent,
    ClientsComponent,
    DevicesComponent,
    UpdateClientComponent,
    CreateClientComponent,
    CreateDeviceComponent,
    UpdateDeviceComponent,
    ChatComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatButtonModule,
    MatSnackBarModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatIconModule,
    MatCardModule,
  ]
})
export class AdminModule { }
