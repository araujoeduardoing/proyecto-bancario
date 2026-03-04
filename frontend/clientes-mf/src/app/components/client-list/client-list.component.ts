import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Client } from '../../models/client.model';

@Component({
  selector: 'app-client-list',
  imports: [],
  templateUrl: './client-list.component.html',
  styleUrl: './client-list.component.scss',
})
export class ClientListComponent {
  @Input() clients: Client[] = [];
  @Input() loading: boolean = false;
  @Input() deleting: number | null = null;

  @Output() editClient = new EventEmitter<Client>();
  @Output() deleteClient = new EventEmitter<Client>();

  onEditClient(client: Client): void {
    this.editClient.emit(client);
  }

  onDeleteClient(client: Client): void {
    this.deleteClient.emit(client);
  }

  isDeleting(clientId: number): boolean {
    return this.deleting === clientId;
  }
}
