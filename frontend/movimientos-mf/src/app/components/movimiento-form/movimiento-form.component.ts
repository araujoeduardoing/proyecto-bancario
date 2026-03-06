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
  shouldReset = input<boolean>(false);
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
    movementType: 'DEPOSITO',
    initialBalance: 0,
    amount: 0,
    availableBalance: 0,
    movementStatus: 'ACTIVE',
  });

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

    // Effect to reset form when shouldReset changes
    effect(() => {
      if (this.shouldReset()) {
        this.resetForm();
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
      this.formData.update((data) => ({
        ...data,
        accountId: 0,
        initialBalance: 0,
        availableBalance: 0,
      }));
    }

    // Auto-fill initial balance when account is selected
    if (field === 'accountId' && value > 0) {
      const selectedAccount = this.accounts().find(
        (account) => account.id === value,
      );
      if (selectedAccount) {
        this.formData.update((data) => ({
          ...data,
          initialBalance: selectedAccount.initialBalance,
          availableBalance: selectedAccount.initialBalance + data.amount,
        }));
      }
    }

    // Recalculate available balance when amount changes
    if (field === 'amount') {
      const currentData = this.formData();
      // Auto-set movement type based on amount
      const movementType = value < 0 ? 'RETIRO' : 'DEPOSITO';
      this.formData.update((data) => ({
        ...data,
        availableBalance: data.initialBalance + value,
        movementType: movementType,
      }));
    }
  }

  onSubmit(): void {
    const data = this.formData();

    // Log para debugging - ver qué JSON se envía
    console.log('📋 Datos del formulario a enviar:');
    console.log(JSON.stringify(data, null, 2));

    // Basic validation
    if (
      data.clientId <= 0 ||
      data.accountId <= 0 ||
      data.initialBalance < 0 ||
      data.availableBalance < 0
    ) {
      console.log('❌ Validación fallida:', {
        clientId: data.clientId,
        accountId: data.accountId,
        initialBalance: data.initialBalance,
        availableBalance: data.availableBalance,
      });
      return;
    }

    console.log('✅ Validación exitosa, enviando datos...');
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
      movementType: 'DEPOSITO',
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
    if (field === 'initialBalance' || field === 'availableBalance') {
      return data[field] >= 0;
    }
    if (field === 'amount') {
      return true; // Amount can be any number (positive or negative)
    }
    return true;
  }
}
