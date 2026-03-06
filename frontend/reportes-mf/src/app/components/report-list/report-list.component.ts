import { Component, Input } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Report } from '../../models/report.model';

@Component({
  selector: 'app-report-list',
  imports: [DatePipe],
  templateUrl: './report-list.component.html',
  styleUrl: './report-list.component.scss',
})
export class ReportListComponent {
  @Input() reports: Report[] = [];
  @Input() loading: boolean = false;

  getReportTypeLabel(type: string): string {
    const types: Record<string, string> = {
      balance: 'Reporte de Saldos',
      transactions: 'Reporte de Transacciones',
      monthly: 'Reporte Mensual',
      client: 'Reporte por Cliente',
    };
    return types[type] || type;
  }

  getAccountTypeLabel(type: string): string {
    const types: Record<string, string> = {
      savings: 'Ahorros',
      checking: 'Corriente',
      fixed_deposit: 'Depósito a Plazo',
    };
    return types[type] || type;
  }

  formatBalance(balance: number): string {
    return new Intl.NumberFormat('es-EC', {
      style: 'currency',
      currency: 'USD',
    }).format(balance);
  }
}
