import {
  Component,
  Output,
  EventEmitter,
  signal,
  effect,
  input,
  inject,
  OnInit,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Movimiento } from '../../models/movimiento.model';
import { MovimientoFormData } from '../../models/movimiento.dto';
import { Client } from '../../models/client.model';
import { ClientService } from '../../services/client.service';

@Component({
  selector: 'app-movimiento-form',
  imports: [FormsModule],
  templateUrl: './movimiento-form.component.html',
  styleUrl: './movimiento-form.component.scss',
})
export class MovimientoFormComponent implements OnInit {
  // Reactive inputs using input() signals
  isVisible = input<boolean>(false);
  isEditing = input<boolean>(false);
  isSubmitting = input<boolean>(false);
  movimientoToEdit = input<Movimiento | null>(null);

  @Output() submitForm = new EventEmitter<MovimientoFormData>();
  @Output() cancel = new EventEmitter<void>();

  // Services
  private readonly clientService = inject(ClientService);

  // Client data
  clients = signal<Client[]>([]);
  loadingClients = signal(false);

  formData = signal<MovimientoFormData>({
    numeroMovimiento: '',
    tipoMovimiento: 'credito',
    monto: 0,
    clientId: 0,
    status: true,
  });

  movimientoTypes = [
    { value: 'credito', label: 'Crédito' },
    { value: 'debito', label: 'Débito' },
    { value: 'transferencia', label: 'Transferencia' },
  ];

  constructor() {
    // Effect to populate form when editing
    effect(() => {
      const movimiento = this.movimientoToEdit();
      if (movimiento && this.isEditing()) {
        this.formData.set({
          numeroMovimiento: movimiento.numeroMovimiento,
          tipoMovimiento: movimiento.tipoMovimiento,
          monto: movimiento.monto,
          clientId: movimiento.clientId,
          status: movimiento.status,
        });
      }
    });
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

  updateField(field: keyof MovimientoFormData, value: any): void {
    this.formData.update((data) => ({ ...data, [field]: value }));
  }

  onSubmit(): void {
    const data = this.formData();

    // Basic validation
    if (!data.numeroMovimiento || data.clientId <= 0 || data.monto < 0) {
      return;
    }

    this.submitForm.emit(data);
  }

  onCancel(): void {
    this.resetForm();
    this.cancel.emit();
  }

  private resetForm(): void {
    this.formData.set({
      numeroMovimiento: '',
      tipoMovimiento: 'credito',
      monto: 0,
      clientId: 0,
      status: true,
    });
  }

  getSelectedClientName(): string {
    const clientId = this.formData().clientId;
    if (clientId === 0) return '';

    const client = this.clients().find((c) => c.clientId === clientId);
    return client ? client.name : '';
  }

  isFieldValid(field: keyof MovimientoFormData): boolean {
    const data = this.formData();
    if (field === 'numeroMovimiento') {
      return !!data[field];
    }
    if (field === 'clientId') {
      return data[field] > 0;
    }
    if (field === 'monto') {
      return data[field] >= 0;
    }
    return true;
  }
}
