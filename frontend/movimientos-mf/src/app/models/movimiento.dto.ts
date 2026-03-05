export interface CreateMovimientoDto {
  numeroMovimiento: string;
  tipoMovimiento: string;
  monto: number;
  clientId: number;
  status: boolean;
}

export interface UpdateMovimientoDto extends CreateMovimientoDto {}

export interface MovimientoFormData {
  numeroMovimiento: string;
  tipoMovimiento: string;
  monto: number;
  clientId: number;
  status: boolean;
}
