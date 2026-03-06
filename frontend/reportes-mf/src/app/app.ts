import { Component, signal, inject, OnInit } from '@angular/core';
import { MovementReport } from './models/report.model';
import { MovementReportRequest } from './models/report.dto';
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
  protected readonly title = signal(
    'Sistema de Reportes de Movimientos Bancarios',
  );

  // Movement data
  protected movements = signal<MovementReport[]>([]);

  // UI State
  protected loading = signal(false);
  protected error = signal<string | null>(null);

  // Services
  private readonly reportService = inject(ReportService);
  private readonly errorHandler = inject(ErrorHandlerService);

  ngOnInit(): void {
    // Initialize with empty movements
    this.movements.set([]);
  }

  // Generate movement report
  onGenerateReport(request: MovementReportRequest): void {
    this.loading.set(true);
    this.error.set(null);

    this.reportService.generateMovementReport(request).subscribe({
      next: (movements) => {
        this.movements.set(movements);
        this.loading.set(false);

        if (movements.length === 0) {
          this.error.set(
            'No se encontraron movimientos para el cliente y período seleccionado',
          );
        }
      },
      error: (err) => {
        this.error.set(this.errorHandler.handleHttpError(err, 'load'));
        this.loading.set(false);
        this.movements.set([]);
      },
    });
  }

  // Clear error message
  onClearError(): void {
    this.error.set(null);
  }
}
