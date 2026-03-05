export interface Movimiento {
  movementId: number;
  movementDate: string;
  clientId: number;
  accountId: number;
  movementType: string;
  initialBalance: number;
  movementStatus: string;
  amount: number;
  availableBalance: number;
  createdAt: string;
  updatedAt: string;
  accountNumber: string;
  clientName: string;
}
