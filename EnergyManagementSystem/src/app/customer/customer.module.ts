import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CustomerRoutingModule } from './customer-routing.module';
import { CustomerComponent } from './customer.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { DeviceDetailComponent } from './components/device-detail/device-detail.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MessagesComponent } from './components/messages/messages.component';
import { ChatComponent } from './components/chat/chat.component';


@NgModule({
  declarations: [
    CustomerComponent,
    DashboardComponent,
    DeviceDetailComponent,
    MessagesComponent,
    ChatComponent
  ],
  imports: [
    CommonModule,
    CustomerRoutingModule,
    FormsModule,
    ReactiveFormsModule
    
  ]
})
export class CustomerModule { }
