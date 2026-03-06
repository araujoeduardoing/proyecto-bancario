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
import { DatePipe, CurrencyPipe, TitleCasePipe } from '@angular/common';
import { Movimiento } from '../../models/movimiento.model';
import { MovimientoFormData } from '../../models/movimiento.dto';
import { Client } from '../../models/client.model';
import { Account } from '../../models/account.model';
import { ClientService } from '../../services/client.service';
import { MovimientoService } from '../../services/movimiento.service';

@Component({
  selector: 'app-movimiento-form',
  imports: [FormsModule, DatePipe, CurrencyPipe, TitleCasePipe],
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
  private readonly movimientoService = inject(MovimientoService);

  // Client data
  clients = signal<Client[]>([]);
  loadingClients = signal(false);

  // Account data
  accounts = signal<Account[]>([]);
  loadingAccounts = signal(false);

  formData = signal<MovimientoFormData>({
    clientId: 0,
    accountId: 0,
    movementType: 'AHORROS',
    initialBalance: 0,
    amount: 0,
    availableBalance: 0,
    movementStatus: 'ACTIVE',
  });

  movimientoTypes = [
    { value: 'DEPOSITO', label: 'Depósito' },
    { value: 'RETIRO', label: 'Retiro' },
  ];

  constructor() {
    // Effect to populate form when editing
    effect(() => {
      const movimiento = this.movimientoToEdit();
      if (movimiento && this.isEditing()) {
        this.formData.set({
          clientId: movimiento.clientId,
          accountId: movimiento.accountId,
          movementType: movimiento.movementType,
          initialBalance: movimiento.initialBalance || 0,
          amount: movimiento.amount,
          availableBalance: movimiento.availableBalance || 0,
          movementStatus: movimiento.movementStatus,
        });

        // Load accounts for the selected client when editing
        if (movimiento.clientId > 0) {
          this.loadAccountsByClient(movimiento.clientId);
        }
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

    // Load accounts when client changes
    if (field === 'clientId' && value > 0) {
      this.loadAccountsByClient(value);
      // Reset account selection when client changes
      this.formData.update((data) => ({ ...data, accountId: 0 }));
    }
  }

  onSubmit(): void {
    const data = this.formData();

    // Basic validation
    if (
      data.clientId <= 0 ||
      data.accountId <= 0 ||
      data.amount < 0 ||
      data.initialBalance < 0 ||
      data.availableBalance < 0
    ) {
      return;
    }

    this.submitForm.emit(data);
  }

  onCancel(): void {
    this.resetForm();
    this.cancel.emit();
  }

  loadAccountsByClient(clientId: number): void {
    this.loadingAccounts.set(true);
    this.accounts.set([]);

    this.movimientoService.getAccountsByClientId(clientId).subscribe({
      next: (accounts) => {
        this.accounts.set(accounts.filter((account) => account.status));
        this.loadingAccounts.set(false);
      },
      error: () => {
        this.accounts.set([]);
        this.loadingAccounts.set(false);
      },
    });
  }

  private resetForm(): void {
    this.formData.set({
      clientId: 0,
      accountId: 0,
      movementType: 'AHORROS',
      initialBalance: 0,
      amount: 0,
      availableBalance: 0,
      movementStatus: 'ACTIVE',
    });
    this.accounts.set([]);
  }

  getSelectedClientName(): string {
    const clientId = this.formData().clientId;
    if (clientId === 0) return '';

    const client = this.clients().find((c) => c.clientId === clientId);
    return client ? client.name : '';
  }

  isFieldValid(field: keyof MovimientoFormData): boolean {
    const data = this.formData();
    if (field === 'clientId' || field === 'accountId') {
      return data[field] > 0;
    }
    if (
      field === 'amount' ||
      field === 'initialBalance' ||
      field === 'availableBalance'
    ) {
      return data[field] >= 0;
    }
    if (field === 'movementType') {
      return !!data[field];
    }
    return true;
  }
}
