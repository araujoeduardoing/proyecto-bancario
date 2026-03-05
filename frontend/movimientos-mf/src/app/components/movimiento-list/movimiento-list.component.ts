import { Component, Input, Output, EventEmitter } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Movimiento } from '../../models/movimiento.model';

@Component({
  selector: 'app-movimiento-list',
  imports: [DatePipe],
  templateUrl: './movimiento-list.component.html',
  styleUrl: './movimiento-list.component.scss',
})
export class MovimientoListComponent {
  @Input() movimientos: Movimiento[] = [];
  @Input() loading: boolean = false;
  @Input() deleting: number | null = null;

  @Output() editMovimiento = new EventEmitter<Movimiento>();
  @Output() deleteMovimiento = new EventEmitter<Movimiento>();

  onEditMovimiento(movimiento: Movimiento): void {
    this.editMovimiento.emit(movimiento);
  }

  onDeleteMovimiento(movimiento: Movimiento): void {
    this.deleteMovimiento.emit(movimiento);
  }

  isDeleting(id: number): boolean {
    return this.deleting === id;
  }

  getMovimientoTypeLabel(type: string): string {
    const types: Record<string, string> = {
      credito: 'Crédito',
      debito: 'Débito',
      transferencia: 'Transferencia',
    };
    return types[type] || type;
  }

  formatMonto(monto: number): string {
    return new Intl.NumberFormat('es-EC', {
      style: 'currency',
      currency: 'USD',
    }).format(monto);
  }
}
