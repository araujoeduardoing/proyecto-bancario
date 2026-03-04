import { Component, Input, Output, EventEmitter } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Account } from '../../models/account.model';

@Component({
  selector: 'app-account-list',
  imports: [DatePipe],
  templateUrl: './account-list.component.html',
  styleUrl: './account-list.component.scss',
})
export class AccountListComponent {
  @Input() accounts: Account[] = [];
  @Input() loading: boolean = false;
  @Input() deleting: number | null = null;

  @Output() editAccount = new EventEmitter<Account>();
  @Output() deleteAccount = new EventEmitter<Account>();

  onEditAccount(account: Account): void {
    this.editAccount.emit(account);
  }

  onDeleteAccount(account: Account): void {
    this.deleteAccount.emit(account);
  }

  isDeleting(id: number): boolean {
    return this.deleting === id;
  }

  getAccountTypeLabel(type: string): string {
    const types: Record<string, string> = {
      savings: 'Ahorros',
      checking: 'Corriente',
      fixed_deposit: 'Depósito a Plazo',
    };
    return types[type] || type;
  }

  formatBalance(balance: number): string {
    return new Intl.NumberFormat('es-EC', {
      style: 'currency',
      currency: 'USD',
    }).format(balance);
  }
}
