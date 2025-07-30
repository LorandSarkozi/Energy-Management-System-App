import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserStorageService } from 'src/app/services/storage/user-storage.service';

const BASIC_URL = "http://user.localhost:80/";
const BASIC_URL_DEVICES = "http://device.localhost:80/";
const BASIC_URL_CHAT = "http://chat.localhost:80/";

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) {}

  // Fetch all clients
  getAllClients(): Observable<any> {
    return this.http.get(BASIC_URL + 'api/admin/clients', {
      headers: this.createAuthorizationHeader(),
    });
  }

  getAllDevices(): Observable<any> {
    return this.http.get(BASIC_URL_DEVICES + 'devices', {
      headers: this.createAuthorizationHeader(),
    });
  }


  createDevice(deviceDto: any): Observable<any> {
    return this.http.post(BASIC_URL_DEVICES + 'devices', deviceDto);
  }


  // Search clients by title
  searchClientByTitle(title: string): Observable<any> {
    return this.http.get(BASIC_URL + 'api/admin/clients/search?title=' + title, {
      headers: this.createAuthorizationHeader(),
    });
  }

  // Edit client
  editClient(clientId: string, updatedClient: any): Observable<any> {
    return this.http.put(BASIC_URL + 'api/admin/clients/' + clientId, updatedClient, {
      headers: this.createAuthorizationHeader(),
    });
}

  editDevice(deviceId: string, updatedDevice: any): Observable<any> {
  return this.http.put(BASIC_URL_DEVICES + 'devices/' + deviceId, updatedDevice, {
    headers: this.createAuthorizationHeader(),
  });
}

  // Delete client
  deleteClient(clientId: string): Observable<any> {
    return this.http.delete(BASIC_URL + 'api/admin/clients/' + clientId, {
      headers: this.createAuthorizationHeader(),
    });
  }


  deleteDevice(deviceId: string): Observable<any> {
    return this.http.delete(BASIC_URL_DEVICES + 'devices/' + deviceId, {
      headers: this.createAuthorizationHeader(),
    });
  }

  getDevicesByCustomerId(customerId: string): Observable<any[]> {
    return this.http.get<any[]>(BASIC_URL_DEVICES + "devices/find/" + customerId);
  }

  sendMessageToClient(clientId: string, messageContent: string): Observable<any> {
    const message = {
        clientIdSender: UserStorageService.getUserId(),
        clientIdReceiver: clientId,  // Correct field name
        content: messageContent,
        timestamp: new Date().toISOString()  // Optional: to track when the message is sent
    };

    return this.http.post(BASIC_URL_CHAT + 'chat/messages', message, {
        headers: this.createAuthorizationHeader(),
    });
}

  getChatHistory(senderId: string, receiverId: string): Observable<any[]> {
    return this.http.get<any[]>(BASIC_URL_CHAT + `chat/messages/history/${senderId}/${receiverId}`, {
      headers: this.createAuthorizationHeader(),
    });
  }

  // Helper function to create the authorization header
  private createAuthorizationHeader(): HttpHeaders {
    return new HttpHeaders().set(
      'Authorization', 'Bearer ' + UserStorageService.getToken()
    );
  }
}
