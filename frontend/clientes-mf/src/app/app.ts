import { Component, signal, inject, OnInit, computed } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Client } from './models/client.model';

interface NewClient {
  name: string;
  gender: string;
  age: number;
  identification: string;
  address: string;
  phone: string;
  password: string;
  status: boolean;
}

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit {
  protected readonly title = signal('clientes-mf');
  protected clients = signal<Client[]>([]);
  protected loading = signal(false);
  protected error = signal<string | null>(null);
  protected searchText = signal('');
  protected showForm = signal(false);
  protected creating = signal(false);
  protected editing = signal(false);
  protected editingClientId = signal<number | null>(null);

  protected newClient = signal<NewClient>({
    name: '',
    gender: 'M',
    age: 0,
    identification: '',
    address: '',
    phone: '',
    password: '',
    status: true,
  });

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

  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:4101/business/retail/v1';

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(): void {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<Client[]>(`${this.baseUrl}/customers/all`).subscribe({
      next: (clients) => {
        this.clients.set(clients);
        this.loading.set(false);
      },
      error: (err) => {
        let errorMessage = 'Error al conectar con el servidor';
        if (err?.error?.message || err?.error?.detailMessage) {
          errorMessage =
            (err.error.message || '') +
            (err.error.detailMessage
              ? err.error.message
                ? ': ' + err.error.detailMessage
                : err.error.detailMessage
              : '');
        }
        this.error.set(errorMessage);
        this.loading.set(false);
        console.error('Error:', err);
      },
    });
  }

  onSearchChange(value: string): void {
    this.searchText.set(value);
  }

  toggleForm(): void {
    this.showForm.set(!this.showForm());
    if (!this.showForm()) {
      this.editing.set(false);
      this.editingClientId.set(null);
      this.resetForm();
    }
  }

  createClient(): void {
    const client = this.newClient();

    if (!client.name || !client.identification || !client.password) {
      this.error.set('Nombre, identificación y contraseña son obligatorios');
      return;
    }

    this.creating.set(true);
    this.error.set(null);

    const request = this.editing()
      ? this.http.put<any>(
          `${this.baseUrl}/customers/${this.editingClientId()}`,
          client,
        )
      : this.http.post<any>(`${this.baseUrl}/customers/register`, client);

    // Debug logs
    if (this.editing()) {
      console.log(
        'PUT URL:',
        `${this.baseUrl}/customers/${this.editingClientId()}`,
      );
      console.log('PUT Data:', client);
      console.log('Editing Client ID:', this.editingClientId());
    }

    request.subscribe({
      next: () => {
        this.creating.set(false);
        this.showForm.set(false);
        this.editing.set(false);
        this.editingClientId.set(null);
        this.resetForm();
        this.loadClients();
      },
      error: (err) => {
        let errorMessage = this.editing()
          ? 'Error al actualizar cliente'
          : 'Error al crear cliente';
        if (err?.error?.message || err?.error?.detailMessage) {
          errorMessage =
            (err.error.message || '') +
            (err.error.detailMessage
              ? err.error.message
                ? ': ' + err.error.detailMessage
                : err.error.detailMessage
              : '');
        }
        this.error.set(errorMessage);
        this.creating.set(false);
        console.error('Error:', err);
      },
    });
  }

  updateClientField(field: keyof NewClient, value: any): void {
    this.newClient.update((client) => ({ ...client, [field]: value }));
  }

  editClient(client: Client): void {
    this.editing.set(true);
    this.editingClientId.set(client.clientId);
    this.newClient.set({
      name: client.name,
      gender: client.gender,
      age: client.age,
      identification: client.identification,
      address: client.address,
      phone: client.phone,
      password: client.password,
      status: client.status,
    });
    this.showForm.set(true);
  }

  private resetForm(): void {
    this.newClient.set({
      name: '',
      gender: 'M',
      age: 0,
      identification: '',
      address: '',
      phone: '',
      password: '',
      status: true,
    });
  }
}
