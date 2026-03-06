import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportFilterDto } from '../../models/report.dto';

@Component({
  selector: 'app-report-search',
  imports: [FormsModule],
  templateUrl: './report-search.component.html',
  styleUrl: './report-search.component.scss',
})
export class ReportSearchComponent {
  @Input() searchText: string = '';
  @Input() reportType: string = '';
  @Input() accountType: string = '';
  @Input() totalReports: number = 0;
  @Input() filteredReports: number = 0;

  @Output() searchChange = new EventEmitter<string>();
  @Output() reportTypeChange = new EventEmitter<string>();
  @Output() accountTypeChange = new EventEmitter<string>();
  @Output() clearSearch = new EventEmitter<void>();

  reportTypes = [
    { value: '', label: 'Todos los tipos' },
    { value: 'balance', label: 'Reporte de Saldos' },
    { value: 'transactions', label: 'Reporte de Transacciones' },
    { value: 'monthly', label: 'Reporte Mensual' },
    { value: 'client', label: 'Reporte por Cliente' },
  ];

  accountTypes = [
    { value: '', label: 'Todos los tipos de cuenta' },
    { value: 'savings', label: 'Cuenta de Ahorros' },
    { value: 'checking', label: 'Cuenta Corriente' },
    { value: 'fixed_deposit', label: 'Depósito a Plazo' },
  ];

  onSearchInput(value: string): void {
    this.searchChange.emit(value);
  }

  onReportTypeChange(value: string): void {
    this.reportTypeChange.emit(value);
  }

  onAccountTypeChange(value: string): void {
    this.accountTypeChange.emit(value);
  }

  onClearSearch(): void {
    this.clearSearch.emit();
  }

  hasActiveSearch(): boolean {
    return (
      this.searchText.length > 0 ||
      this.reportType.length > 0 ||
      this.accountType.length > 0
    );
  }
}
