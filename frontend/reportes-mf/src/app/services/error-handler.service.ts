import { Injectable } from '@angular/core';

export interface ApiError {
  message?: string;
  detailMessage?: string;
}

@Injectable({
  providedIn: 'root',
})
export class ErrorHandlerService {
  handleError(error: any, defaultMessage: string = 'Error inesperado'): string {
    console.error('Error:', error);

    if (error?.error?.message || error?.error?.detailMessage) {
      const message = error.error.message || '';
      const detail = error.error.detailMessage || '';

      if (message && detail) {
        return `${message}: ${detail}`;
      }

      return message || detail;
    }

    return error?.message || defaultMessage;
  }

  handleHttpError(error: any, operation: 'load' | 'filter' = 'load'): string {
    const defaultMessages = {
      load: 'Error al cargar reportes',
      filter: 'Error al filtrar reportes',
    };

    return this.handleError(error, defaultMessages[operation]);
  }
}
