export interface Report {
  id: number;
  reportType: string;
  reportTitle: string;
  accountNumber: string;
  accountType: string;
  balance: number;
  clientId: number;
  clientName: string;
  transactionCount: number;
  lastTransaction: string;
  status: boolean;
  generatedAt: string;
}
