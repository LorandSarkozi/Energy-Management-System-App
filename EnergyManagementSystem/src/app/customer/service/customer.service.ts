import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserStorageService } from 'src/app/services/storage/user-storage.service';

const BASIC_URL_DEVICES = "http://measurement.localhost:80/";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {


  constructor(private http: HttpClient) { }


  getAllDevices(): Observable<any> {
    return this.http.get(BASIC_URL_DEVICES + 'devices', {
      headers: this.createAuthorizationHeader(),
    });
  }

  getEnergyConsumptionByDeviceAndDate(deviceId: string, date: string): Observable<any> {
    return this.http.get(BASIC_URL_DEVICES + `energy/consumption/${deviceId}?date=${date}`, {
      headers: this.createAuthorizationHeader(),
    });
  }
  

  private createAuthorizationHeader(): HttpHeaders {
    return new HttpHeaders().set(
      'Authorization', 'Bearer ' + UserStorageService.getToken()
    );
  }
  
}
