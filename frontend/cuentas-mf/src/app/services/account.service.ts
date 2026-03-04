import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Account } from '../models/account.model';
import { CreateAccountDto, UpdateAccountDto } from '../models/account.dto';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:4102/business/retail/v1';

  getAll(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.baseUrl}/accounts/all`);
  }

  create(account: CreateAccountDto): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/accounts`, account);
  }

  update(id: number, account: UpdateAccountDto): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/accounts/${id}`, account);
  }

  delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/accounts/${id}`);
  }
}
