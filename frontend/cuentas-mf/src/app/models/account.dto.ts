export interface CreateAccountDto {
  accountNumber: string;
  accountType: string;
  initialBalance: number;
  clientId: number;
  status: boolean;
}

export interface UpdateAccountDto extends CreateAccountDto {}

export interface AccountFormData {
  accountNumber: string;
  accountType: string;
  initialBalance: number;
  clientId: number;
  status: boolean;
}
