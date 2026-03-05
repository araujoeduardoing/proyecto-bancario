export interface CreateMovimientoDto {
  movementDate: string;
  clientId: number;
  accountId: number;
  movementType: string;
  amount: number;
  movementStatus: string;
}

export interface UpdateMovimientoDto extends CreateMovimientoDto {}

export interface MovimientoFormData {
  movementDate: string;
  clientId: number;
  accountId: number;
  movementType: string;
  amount: number;
  movementStatus: string;
}
