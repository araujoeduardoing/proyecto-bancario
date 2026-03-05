import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movimiento } from '../models/movimiento.model';
import {
  CreateMovimientoDto,
  UpdateMovimientoDto,
} from '../models/movimiento.dto';

@Injectable({
  providedIn: 'root',
})
export class MovimientoService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:4103/business/retail/v1';

  getAll(): Observable<Movimiento[]> {
    return this.http.get<Movimiento[]>(`${this.baseUrl}/movements/details/all`);
  }

  create(movimiento: CreateMovimientoDto): Observable<any> {
    return this.http.post<any>(
      `${this.baseUrl}/movimientos/create`,
      movimiento,
    );
  }

  update(id: number, movimiento: UpdateMovimientoDto): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/movimientos/${id}`, movimiento);
  }

  delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/movimientos/${id}`);
  }
}
