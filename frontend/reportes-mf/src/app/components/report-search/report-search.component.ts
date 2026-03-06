import {
  Component,
  Input,
  Output,
  EventEmitter,
  inject,
  OnInit,
  signal,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MovementReportRequest } from '../../models/report.dto';
import { ClientService } from '../../services/client.service';
import { Client } from '../../models/client.model';

@Component({
  selector: 'app-report-search',
  imports: [FormsModule],
  templateUrl: './report-search.component.html',
  styleUrl: './report-search.component.scss',
})
export class ReportSearchComponent implements OnInit {
  @Input() loading: boolean = false;
  @Input() totalMovements: number = 0;

  @Output() generateReport = new EventEmitter<MovementReportRequest>();
  @Output() downloadPdf = new EventEmitter<void>();

  // Services
  private readonly clientService = inject(ClientService);

  // Client data
  clients = signal<Client[]>([]);
  loadingClients = signal(false);

  clientId: number | null = null;
  startDate: string = '';
  endDate: string = '';

  constructor() {
    // Set default dates (last month)
    const today = new Date();
    const lastMonth = new Date();
    lastMonth.setMonth(today.getMonth() - 1);

    this.endDate = today.toISOString().split('T')[0];
    this.startDate = lastMonth.toISOString().split('T')[0];
  }

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(): void {
    this.loadingClients.set(true);
    this.clientService.getAll().subscribe({
      next: (clients) => {
        this.clients.set(clients.filter((client) => client.status));
        this.loadingClients.set(false);
      },
      error: () => {
        this.loadingClients.set(false);
      },
    });
  }

  onGenerateReport(): void {
    if (!this.clientId || !this.startDate || !this.endDate) {
      return;
    }

    if (new Date(this.startDate) > new Date(this.endDate)) {
      alert('La fecha de inicio debe ser menor o igual a la fecha de fin');
      return;
    }

    const request: MovementReportRequest = {
      clientId: this.clientId,
      startDate: this.startDate,
      endDate: this.endDate,
    };

    this.generateReport.emit(request);
  }

  onDownloadPdf(): void {
    this.downloadPdf.emit();
  }

  isValidForm(): boolean {
    return !!(this.clientId && this.startDate && this.endDate);
  }
}
