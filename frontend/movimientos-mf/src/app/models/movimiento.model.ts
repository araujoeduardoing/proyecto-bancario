export interface Movimiento {
  id: number;
  numeroMovimiento: string;
  tipoMovimiento: string;
  monto: number;
  clientId: number;
  status: boolean;
  createdAt: string;
  updatedAt: string;
}
