import { Component, Input } from '@angular/core';
import { DatePipe, CurrencyPipe } from '@angular/common';
import { MovementReport } from '../../models/report.model';

@Component({
  selector: 'app-report-list',
  imports: [DatePipe, CurrencyPipe],
  templateUrl: './report-list.component.html',
  styleUrl: './report-list.component.scss',
})
export class ReportListComponent {
  @Input() movements: MovementReport[] = [];
  @Input() loading: boolean = false;

  getMovementTypeLabel(type: string): string {
    const types: Record<string, string> = {
      DEPOSITO: 'Depósito',
      RETIRO: 'Retiro',
      TRANSFERENCIA: 'Transferencia',
    };
    return types[type] || type;
  }

  getMovementTypeClass(type: string): string {
    const classes: Record<string, string> = {
      DEPOSITO: 'movement-deposit',
      RETIRO: 'movement-withdrawal',
      TRANSFERENCIA: 'movement-transfer',
    };
    return classes[type] || 'movement-default';
  }

  getStatusLabel(status: string): string {
    const statuses: Record<string, string> = {
      ACTIVE: 'Activo',
      INACTIVE: 'Inactivo',
      PENDING: 'Pendiente',
    };
    return statuses[status] || status;
  }

  formatAmount(amount: number): string {
    return new Intl.NumberFormat('es-EC', {
      style: 'currency',
      currency: 'USD',
    }).format(Math.abs(amount));
  }
}
