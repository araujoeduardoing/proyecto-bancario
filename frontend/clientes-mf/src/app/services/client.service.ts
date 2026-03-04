import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from '../models/client.model';
import { CreateClientDto, UpdateClientDto } from '../models/client.dto';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:4101/business/retail/v1';

  getAll(): Observable<Client[]> {
    return this.http.get<Client[]>(`${this.baseUrl}/customers/all`);
  }

  create(client: CreateClientDto): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/customers/register`, client);
  }

  update(id: number, client: UpdateClientDto): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/customers/${id}`, client);
  }

  delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/customers/${id}`);
  }
}
