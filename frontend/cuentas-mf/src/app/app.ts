import { Component, signal, inject, OnInit, computed } from '@angular/core';
import { Account } from './models/account.model';
import { AccountFormData } from './models/account.dto';
import { AccountService } from './services/account.service';
import { ErrorHandlerService } from './services/error-handler.service';
import { AccountListComponent } from './components/account-list/account-list.component';
import { AccountFormComponent } from './components/account-form/account-form.component';
import { AccountSearchComponent } from './components/account-search/account-search.component';

@Component({
  selector: 'app-root',
  imports: [AccountListComponent, AccountFormComponent, AccountSearchComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit {
  protected readonly title = signal('Gestión de Cuentas Bancarias');

  // Account data
  protected accounts = signal<Account[]>([]);
  protected searchText = signal('');

  // UI State
  protected loading = signal(false);
  protected error = signal<string | null>(null);
  protected showForm = signal(false);
  protected creating = signal(false);
  protected editing = signal(false);
  protected editingAccount = signal<Account | null>(null);
  protected deleting = signal<number | null>(null);

  // Computed values
  protected filteredAccounts = computed(() => {
    const search = this.searchText().toLowerCase();
    if (!search) {
      return this.accounts();
    }
    return this.accounts().filter(
      (account) =>
        account.accountNumber.toLowerCase().includes(search) ||
        account.accountType.toLowerCase().includes(search),
    );
  });

  // Services
  private readonly accountService = inject(AccountService);
  private readonly errorHandler = inject(ErrorHandlerService);

  ngOnInit(): void {
    this.loadAccounts();
  }

  // Account operations
  loadAccounts(): void {
    this.loading.set(true);
    this.error.set(null);

    this.accountService.getAll().subscribe({
      next: (accounts) => {
        this.accounts.set(accounts);
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

  onClearSearch(): void {
    this.searchText.set('');
  }

  // Form operations
  onShowCreateForm(): void {
    this.editing.set(false);
    this.editingAccount.set(null);
    this.showForm.set(true);
  }

  onEditAccount(account: Account): void {
    this.editing.set(true);
    this.editingAccount.set(account);
    this.showForm.set(true);
  }

  onFormSubmit(formData: AccountFormData): void {
    this.creating.set(true);
    this.error.set(null);

    const operation =
      this.editing() && this.editingAccount()
        ? this.accountService.update(this.editingAccount()!.id, formData)
        : this.accountService.create(formData);

    operation.subscribe({
      next: () => {
        this.creating.set(false);
        this.closeForm();
        this.loadAccounts();
      },
      error: (err) => {
        const errorType = this.editing() ? 'update' : 'create';
        this.error.set(this.errorHandler.handleHttpError(err, errorType));
        this.creating.set(false);
      },
    });
  }

  onFormCancel(): void {
    this.closeForm();
  }

  private closeForm(): void {
    this.showForm.set(false);
    this.editing.set(false);
    this.editingAccount.set(null);
  }

  // Delete operation
  onDeleteAccount(account: Account): void {
    if (
      !confirm(`¿Está seguro de eliminar la cuenta "${account.accountNumber}"?`)
    ) {
      return;
    }

    this.deleting.set(account.id);
    this.error.set(null);

    this.accountService.delete(account.id).subscribe({
      next: () => {
        this.deleting.set(null);
        this.loadAccounts();
      },
      error: (err) => {
        this.error.set(this.errorHandler.handleHttpError(err, 'delete'));
        this.deleting.set(null);
      },
    });
  }
}
