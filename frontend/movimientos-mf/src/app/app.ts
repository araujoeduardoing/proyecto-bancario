import { Component, signal, inject, OnInit, computed } from '@angular/core';
import { Movimiento } from './models/movimiento.model';
import { MovimientoFormData } from './models/movimiento.dto';
import { MovimientoService } from './services/movimiento.service';
import { ErrorHandlerService } from './services/error-handler.service';
import { MovimientoListComponent } from './components/movimiento-list/movimiento-list.component';
import { MovimientoFormComponent } from './components/movimiento-form/movimiento-form.component';
import { MovimientoSearchComponent } from './components/movimiento-search/movimiento-search.component';

@Component({
  selector: 'app-root',
  imports: [
    MovimientoListComponent,
    MovimientoFormComponent,
    MovimientoSearchComponent,
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit {
  protected readonly title = signal('Gestión de Movimientos Bancarios');

  // Movimiento data
  protected movimientos = signal<Movimiento[]>([]);
  protected searchText = signal('');

  // UI State
  protected loading = signal(false);
  protected error = signal<string | null>(null);
  protected showForm = signal(false);
  protected creating = signal(false);
  protected editing = signal(false);
  protected editingMovimiento = signal<Movimiento | null>(null);
  protected deleting = signal<number | null>(null);

  // Computed values
  protected filteredMovimientos = computed(() => {
    const search = this.searchText().toLowerCase();
    if (!search) {
      return this.movimientos();
    }
    return this.movimientos().filter(
      (movimiento) =>
        movimiento.numeroMovimiento.toLowerCase().includes(search) ||
        movimiento.tipoMovimiento.toLowerCase().includes(search),
    );
  });

  // Services
  private readonly movimientoService = inject(MovimientoService);
  private readonly errorHandler = inject(ErrorHandlerService);

  ngOnInit(): void {
    this.loadMovimientos();
  }

  // Movimiento operations
  loadMovimientos(): void {
    this.loading.set(true);
    this.error.set(null);

    this.movimientoService.getAll().subscribe({
      next: (movimientos) => {
        this.movimientos.set(movimientos);
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
    this.editingMovimiento.set(null);
    this.showForm.set(true);
  }

  onEditMovimiento(movimiento: Movimiento): void {
    this.editing.set(true);
    this.editingMovimiento.set(movimiento);
    this.showForm.set(true);
  }

  onFormSubmit(formData: MovimientoFormData): void {
    this.creating.set(true);
    this.error.set(null);

    const operation =
      this.editing() && this.editingMovimiento()
        ? this.movimientoService.update(this.editingMovimiento()!.id, formData)
        : this.movimientoService.create(formData);

    operation.subscribe({
      next: () => {
        this.creating.set(false);
        this.closeForm();
        this.loadMovimientos();
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
    this.editingMovimiento.set(null);
  }

  // Delete operation
  onDeleteMovimiento(movimiento: Movimiento): void {
    if (
      !confirm(
        `¿Está seguro de eliminar el movimiento "${movimiento.numeroMovimiento}"?`,
      )
    ) {
      return;
    }

    this.deleting.set(movimiento.id);
    this.error.set(null);

    this.movimientoService.delete(movimiento.id).subscribe({
      next: () => {
        this.deleting.set(null);
        this.loadMovimientos();
      },
      error: (err) => {
        this.error.set(this.errorHandler.handleHttpError(err, 'delete'));
        this.deleting.set(null);
      },
    });
  }
}
