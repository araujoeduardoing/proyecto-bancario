export interface Account {
  id: number;
  accountNumber: string;
  accountType: string;
  initialBalance: number;
  status: boolean;
  clientId: number;
  name: string | null;
  createdAt: string;
  updatedAt: string;
}
