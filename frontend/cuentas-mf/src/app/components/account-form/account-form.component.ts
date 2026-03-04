import {
  Component,
  Output,
  EventEmitter,
  signal,
  effect,
  input,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Account } from '../../models/account.model';
import { AccountFormData } from '../../models/account.dto';

@Component({
  selector: 'app-account-form',
  imports: [FormsModule],
  templateUrl: './account-form.component.html',
  styleUrl: './account-form.component.scss',
})
export class AccountFormComponent {
  // Reactive inputs using input() signals
  isVisible = input<boolean>(false);
  isEditing = input<boolean>(false);
  isSubmitting = input<boolean>(false);
  accountToEdit = input<Account | null>(null);

  @Output() submitForm = new EventEmitter<AccountFormData>();
  @Output() cancel = new EventEmitter<void>();

  formData = signal<AccountFormData>({
    accountNumber: '',
    accountType: 'savings',
    initialBalance: 0,
    clientId: 0,
    status: true,
  });

  accountTypes = [
    { value: 'savings', label: 'Cuenta de Ahorros' },
    { value: 'checking', label: 'Cuenta Corriente' },
    { value: 'fixed_deposit', label: 'Depósito a Plazo' },
  ];

  constructor() {
    // Effect to populate form when editing
    effect(() => {
      const account = this.accountToEdit();
      if (account && this.isEditing()) {
        this.formData.set({
          accountNumber: account.accountNumber,
          accountType: account.accountType,
          initialBalance: account.initialBalance,
          clientId: account.clientId,
          status: account.status,
        });
      }
    });
  }

  updateField(field: keyof AccountFormData, value: any): void {
    this.formData.update((data) => ({ ...data, [field]: value }));
  }

  onSubmit(): void {
    const data = this.formData();

    // Basic validation
    if (!data.accountNumber || data.clientId <= 0 || data.initialBalance < 0) {
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
      accountNumber: '',
      accountType: 'savings',
      initialBalance: 0,
      clientId: 0,
      status: true,
    });
  }

  isFieldValid(field: keyof AccountFormData): boolean {
    const data = this.formData();
    if (field === 'accountNumber') {
      return !!data[field];
    }
    if (field === 'clientId') {
      return data[field] > 0;
    }
    if (field === 'initialBalance') {
      return data[field] >= 0;
    }
    return true;
  }
}
