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
import { DatePipe } from '@angular/common';
import { Movimiento } from '../../models/movimiento.model';
import { MovimientoFormData } from '../../models/movimiento.dto';
import { Client } from '../../models/client.model';
import { ClientService } from '../../services/client.service';

@Component({
  selector: 'app-movimiento-form',
  imports: [FormsModule, DatePipe],
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
    movementDate: new Date().toISOString(),
    clientId: 0,
    accountId: 0,
    movementType: 'AHORROS',
    amount: 0,
    movementStatus: 'ACTIVE',
  });

  movimientoTypes = [
    { value: 'AHORROS', label: 'Ahorros' },
    { value: 'CORRIENTE', label: 'Corriente' },
    { value: 'DEPOSITO', label: 'Depósito' },
    { value: 'RETIRO', label: 'Retiro' },
  ];

  constructor() {
    // Effect to populate form when editing
    effect(() => {
      const movimiento = this.movimientoToEdit();
      if (movimiento && this.isEditing()) {
        this.formData.set({
          movementDate: movimiento.movementDate,
          clientId: movimiento.clientId,
          accountId: movimiento.accountId,
          movementType: movimiento.movementType,
          amount: movimiento.amount,
          movementStatus: movimiento.movementStatus,
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
    if (
      !data.movementDate ||
      data.clientId <= 0 ||
      data.accountId <= 0 ||
      data.amount < 0
    ) {
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
      movementDate: new Date().toISOString(),
      clientId: 0,
      accountId: 0,
      movementType: 'AHORROS',
      amount: 0,
      movementStatus: 'ACTIVE',
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
    if (field === 'movementDate') {
      return !!data[field];
    }
    if (field === 'clientId' || field === 'accountId') {
      return data[field] > 0;
    }
    if (field === 'amount') {
      return data[field] >= 0;
    }
    if (field === 'movementType') {
      return !!data[field];
    }
    return true;
  }

  formatDateForInput(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toISOString().slice(0, 16);
  }
}
