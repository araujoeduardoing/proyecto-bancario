import { Component, signal, inject, OnInit, computed } from '@angular/core';
import { Report } from './models/report.model';
import { ReportFilterDto } from './models/report.dto';
import { ReportService } from './services/report.service';
import { ErrorHandlerService } from './services/error-handler.service';
import { ReportListComponent } from './components/report-list/report-list.component';
import { ReportSearchComponent } from './components/report-search/report-search.component';

@Component({
  selector: 'app-root',
  imports: [ReportListComponent, ReportSearchComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit {
  protected readonly title = signal('Sistema de Reportes Bancarios');

  // Report data
  protected reports = signal<Report[]>([]);
  protected searchText = signal('');
  protected reportTypeFilter = signal('');
  protected accountTypeFilter = signal('');

  // UI State
  protected loading = signal(false);
  protected error = signal<string | null>(null);

  // Computed values
  protected filteredReports = computed(() => {
    const search = this.searchText().toLowerCase();
    const reportType = this.reportTypeFilter();
    const accountType = this.accountTypeFilter();

    if (!search && !reportType && !accountType) {
      return this.reports();
    }

    return this.reports().filter((report) => {
      const matchesSearch =
        !search ||
        report.accountNumber.toLowerCase().includes(search) ||
        report.reportTitle.toLowerCase().includes(search) ||
        report.clientName.toLowerCase().includes(search);

      const matchesReportType = !reportType || report.reportType === reportType;
      const matchesAccountType =
        !accountType || report.accountType === accountType;

      return matchesSearch && matchesReportType && matchesAccountType;
    });
  });

  // Services
  private readonly reportService = inject(ReportService);
  private readonly errorHandler = inject(ErrorHandlerService);

  ngOnInit(): void {
    this.loadReports();
  }

  // Report operations
  loadReports(): void {
    this.loading.set(true);
    this.error.set(null);

    this.reportService.getAllReports().subscribe({
      next: (reports) => {
        this.reports.set(reports);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(this.errorHandler.handleHttpError(err, 'load'));
        this.loading.set(false);
      },
    });
  }

  // Search operations
  onSearchChange(value: string): void {
    this.searchText.set(value);
  }

  onReportTypeChange(value: string): void {
    this.reportTypeFilter.set(value);
  }

  onAccountTypeChange(value: string): void {
    this.accountTypeFilter.set(value);
  }

  onClearSearch(): void {
    this.searchText.set('');
    this.reportTypeFilter.set('');
    this.accountTypeFilter.set('');
  }

  // Refresh operation
  onRefreshReports(): void {
    this.loadReports();
  }
}
