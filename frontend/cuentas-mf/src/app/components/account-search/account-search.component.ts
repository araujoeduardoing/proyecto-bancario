import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-account-search',
  imports: [FormsModule],
  templateUrl: './account-search.component.html',
  styleUrl: './account-search.component.scss',
})
export class AccountSearchComponent {
  @Input() searchText: string = '';
  @Input() totalAccounts: number = 0;
  @Input() filteredAccounts: number = 0;

  @Output() searchChange = new EventEmitter<string>();
  @Output() clearSearch = new EventEmitter<void>();

  onSearchInput(value: string): void {
    this.searchChange.emit(value);
  }

  onClearSearch(): void {
    this.clearSearch.emit();
  }

  hasActiveSearch(): boolean {
    return this.searchText.length > 0;
  }
}
