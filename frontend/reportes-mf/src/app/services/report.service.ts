import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Report } from '../models/report.model';
import { ReportFilterDto } from '../models/report.dto';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:4102/business/retail/v1';

  getAllReports(): Observable<Report[]> {
    return this.http.get<Report[]>(`${this.baseUrl}/reports/all`);
  }

  getReportsByType(reportType: string): Observable<Report[]> {
    return this.http.get<Report[]>(`${this.baseUrl}/reports/type/${reportType}`);
  }

  getReportsWithFilters(filters: ReportFilterDto): Observable<Report[]> {
    let params = new HttpParams();
    
    if (filters.reportType) {
      params = params.set('reportType', filters.reportType);
    }
    if (filters.accountType) {
      params = params.set('accountType', filters.accountType);
    }
    if (filters.clientId) {
      params = params.set('clientId', filters.clientId.toString());
    }
    if (filters.dateFrom) {
      params = params.set('dateFrom', filters.dateFrom);
    }
    if (filters.dateTo) {
      params = params.set('dateTo', filters.dateTo);
    }
    if (filters.status !== undefined) {
      params = params.set('status', filters.status.toString());
    }

    return this.http.get<Report[]>(`${this.baseUrl}/reports/filter`, { params });
  }

  getReportById(id: number): Observable<Report> {
    return this.http.get<Report>(`${this.baseUrl}/reports/${id}`);
  }
}
