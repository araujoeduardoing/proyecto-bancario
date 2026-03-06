export interface ReportFilterDto {
  reportType?: string;
  accountType?: string;
  clientId?: number;
  dateFrom?: string;
  dateTo?: string;
  status?: boolean;
}

export interface ReportSearchData {
  reportType: string;
  accountNumber: string;
  clientId: number;
  dateFrom: string;
  dateTo: string;
}
