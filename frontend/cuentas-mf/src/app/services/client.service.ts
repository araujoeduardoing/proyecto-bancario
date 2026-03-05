import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from '../models/client.model';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:4101/business/retail/v1';

  getAll(): Observable<Client[]> {
    return this.http.get<Client[]>(`${this.baseUrl}/customers/all`);
  }
}
