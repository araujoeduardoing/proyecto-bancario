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
      AHORROS: 'Ahorros',
      CORRIENTE: 'Corriente',
      DEPOSITO: 'Depósito',
      RETIRO: 'Retiro',
    };
    return types[type] || type;
  }

  formatAmount(amount: number): string {
    return new Intl.NumberFormat('es-EC', {
      style: 'currency',
      currency: 'USD',
    }).format(amount);
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString('es-EC');
  }
}
