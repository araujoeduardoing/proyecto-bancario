export interface MovementReportRequest {
  clientId: number;
  startDate: string;
  endDate: string;
}

export interface ReportFilters {
  clientId: number | null;
  startDate: string;
  endDate: string;
  movementType?: string;
}
