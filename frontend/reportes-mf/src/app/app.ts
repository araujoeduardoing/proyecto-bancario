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

  // Download PDF report
  onDownloadPdf(): void {
    const movements = this.movements();
    if (movements.length === 0) {
      return;
    }

    // Create a new window for PDF generation
    const printWindow = window.open('', '_blank');
    if (!printWindow) {
      alert('Por favor, permite las ventanas emergentes para descargar el PDF');
      return;
    }

    const clientName = movements[0].clientName;
    const reportDate = new Date().toLocaleDateString('es-EC');
    const startDate = new Date().toLocaleDateString('es-EC');
    const endDate = new Date().toLocaleDateString('es-EC');

    const htmlContent = `
      <!DOCTYPE html>
      <html>
      <head>
        <title>Reporte de Movimientos - ${clientName}</title>
        <style>
          body { font-family: Arial, sans-serif; margin: 20px; color: #333; }
          .header { text-align: center; margin-bottom: 30px; border-bottom: 2px solid #333; padding-bottom: 20px; }
          .header h1 { margin: 0; color: #333; }
          .info { margin-bottom: 20px; }
          .info p { margin: 5px 0; }
          table { width: 100%; border-collapse: collapse; margin-top: 20px; }
          th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; font-size: 12px; }
          th { background-color: #f8f9fa; font-weight: bold; }
          .amount-positive { color: #28a745; font-weight: bold; }
          .amount-negative { color: #dc3545; font-weight: bold; }
          .balance { text-align: right; font-weight: bold; }
          .movement-type { padding: 3px 6px; border-radius: 3px; font-size: 11px; }
          .deposit { background-color: #d4edda; color: #155724; }
          .withdrawal { background-color: #f8d7da; color: #721c24; }
          .footer { margin-top: 30px; text-align: center; font-size: 10px; color: #666; }
          @media print {
            body { margin: 0; }
            .no-print { display: none; }
          }
        </style>
      </head>
      <body>
        <div class="header">
          <h1>Reporte de Movimientos Bancarios</h1>
        </div>
        
        <div class="info">
          <p><strong>Cliente:</strong> ${clientName}</p>
          <p><strong>Fecha de generación:</strong> ${reportDate}</p>
          <p><strong>Total de movimientos:</strong> ${movements.length}</p>
        </div>

        <table>
          <thead>
            <tr>
              <th>Fecha</th>
              <th>Cuenta</th>
              <th>Tipo</th>
              <th>Saldo Inicial</th>
              <th>Monto</th>
              <th>Saldo Final</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            ${movements
              .map(
                (movement) => `
              <tr>
                <td>${new Date(movement.movementDate).toLocaleString('es-EC')}</td>
                <td>${movement.accountNumber}</td>
                <td><span class="movement-type ${movement.movementType === 'DEPOSITO' ? 'deposit' : 'withdrawal'}">
                  ${movement.movementType === 'DEPOSITO' ? 'Depósito' : movement.movementType === 'RETIRO' ? 'Retiro' : movement.movementType}
                </span></td>
                <td class="balance">$${movement.initialBalance.toFixed(2)}</td>
                <td class="${movement.amount >= 0 ? 'amount-positive' : 'amount-negative'} balance">
                  ${movement.amount >= 0 ? '+' : ''}$${Math.abs(movement.amount).toFixed(2)}
                </td>
                <td class="balance">$${movement.availableBalance.toFixed(2)}</td>
                <td>${movement.movementStatus === 'ACTIVE' ? 'Activo' : movement.movementStatus}</td>
              </tr>
            `,
              )
              .join('')}
          </tbody>
        </table>

        <div class="footer">
          <p>Reporte generado el ${reportDate} - Sistema de Reportes Bancarios</p>
        </div>
        
        <script>
          window.onload = function() {
            window.print();
            setTimeout(() => window.close(), 1000);
          }
        </script>
      </body>
      </html>
    `;

    printWindow.document.write(htmlContent);
    printWindow.document.close();
  }
}
