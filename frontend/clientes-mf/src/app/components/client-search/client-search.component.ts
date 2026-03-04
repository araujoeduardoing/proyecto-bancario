import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-client-search',
  imports: [FormsModule],
  templateUrl: './client-search.component.html',
  styleUrl: './client-search.component.scss',
})
export class ClientSearchComponent {
  @Input() searchText: string = '';
  @Input() totalClients: number = 0;
  @Input() filteredClients: number = 0;

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
