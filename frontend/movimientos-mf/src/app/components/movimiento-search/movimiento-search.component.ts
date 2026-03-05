import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-movimiento-search',
  imports: [FormsModule],
  templateUrl: './movimiento-search.component.html',
  styleUrl: './movimiento-search.component.scss',
})
export class MovimientoSearchComponent {
  @Input() searchText: string = '';
  @Input() totalMovimientos: number = 0;
  @Input() filteredMovimientos: number = 0;

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
