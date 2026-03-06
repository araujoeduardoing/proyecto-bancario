import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { MovementReport } from '../models/report.model';
import { MovementReportRequest } from '../models/report.dto';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:4103/business/retail/v1';

  generateMovementReport(
    request: MovementReportRequest,
  ): Observable<MovementReport[]> {
    return this.http.post<MovementReport[]>(
      `${this.baseUrl}/movements/report`,
      request,
    );
  }

  // Método para obtener reportes vacíos al inicio
  getEmptyReport(): Observable<MovementReport[]> {
    return of([]);
  }
}
