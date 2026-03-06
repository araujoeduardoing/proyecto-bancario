export interface CreateMovimientoDto {
  clientId: number;
  accountId: number;
  movementType: string;
  initialBalance: number;
  movementStatus: string;
  amount: number;
  availableBalance: number;
}

export interface UpdateMovimientoDto extends CreateMovimientoDto {}

export interface MovimientoFormData {
  clientId: number;
  accountId: number;
  movementType: string;
  initialBalance: number;
  movementStatus: string;
  amount: number;
  availableBalance: number;
}
