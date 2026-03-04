import { Component, signal, inject, OnInit, computed } from '@angular/core';
import { Client } from './models/client.model';
import { ClientFormData } from './models/client.dto';
import { ClientService } from './services/client.service';
import { ErrorHandlerService } from './services/error-handler.service';
import { ClientListComponent } from './components/client-list/client-list.component';
import { ClientFormComponent } from './components/client-form/client-form.component';
import { ClientSearchComponent } from './components/client-search/client-search.component';

@Component({
  selector: 'app-root',
  imports: [ClientListComponent, ClientFormComponent, ClientSearchComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit {
  protected readonly title = signal('Gestión de Clientes');

  // Client data
  protected clients = signal<Client[]>([]);
  protected searchText = signal('');

  // UI State
  protected loading = signal(false);
  protected error = signal<string | null>(null);
  protected showForm = signal(false);
  protected creating = signal(false);
  protected editing = signal(false);
  protected editingClient = signal<Client | null>(null);
  protected deleting = signal<number | null>(null);

  // Computed values
  protected filteredClients = computed(() => {
    const search = this.searchText().toLowerCase();
    if (!search) {
      return this.clients();
    }
    return this.clients().filter(
      (client) =>
        client.name.toLowerCase().includes(search) ||
        client.identification.includes(search) ||
        client.address.toLowerCase().includes(search) ||
        client.phone.includes(search),
    );
  });

  // Services
  private readonly clientService = inject(ClientService);
  private readonly errorHandler = inject(ErrorHandlerService);

  ngOnInit(): void {
    this.loadClients();
  }

  // Client operations
  loadClients(): void {
    this.loading.set(true);
    this.error.set(null);

    this.clientService.getAll().subscribe({
      next: (clients) => {
        this.clients.set(clients);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(this.errorHandler.handleHttpError(err, 'load'));
        this.loading.set(false);
      },
    });
  }

  // Search operations
  onSearchChange(value: string): void {
    this.searchText.set(value);
  }

  onClearSearch(): void {
    this.searchText.set('');
  }

  // Form operations
  onShowCreateForm(): void {
    this.editing.set(false);
    this.editingClient.set(null);
    this.showForm.set(true);
  }

  onEditClient(client: Client): void {
    this.editing.set(true);
    this.editingClient.set(client);
    this.showForm.set(true);
  }

  onFormSubmit(formData: ClientFormData): void {
    this.creating.set(true);
    this.error.set(null);

    const operation =
      this.editing() && this.editingClient()
        ? this.clientService.update(this.editingClient()!.clientId, formData)
        : this.clientService.create(formData);

    operation.subscribe({
      next: () => {
        this.creating.set(false);
        this.closeForm();
        this.loadClients();
      },
      error: (err) => {
        const errorType = this.editing() ? 'update' : 'create';
        this.error.set(this.errorHandler.handleHttpError(err, errorType));
        this.creating.set(false);
      },
    });
  }

  onFormCancel(): void {
    this.closeForm();
  }

  private closeForm(): void {
    this.showForm.set(false);
    this.editing.set(false);
    this.editingClient.set(null);
  }

  // Delete operation
  onDeleteClient(client: Client): void {
    if (!confirm(`¿Está seguro de eliminar al cliente "${client.name}"?`)) {
      return;
    }

    this.deleting.set(client.clientId);
    this.error.set(null);

    this.clientService.delete(client.clientId).subscribe({
      next: () => {
        this.deleting.set(null);
        this.loadClients();
      },
      error: (err) => {
        this.error.set(this.errorHandler.handleHttpError(err, 'delete'));
        this.deleting.set(null);
      },
    });
  }
}
