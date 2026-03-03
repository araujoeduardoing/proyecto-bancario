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
        this.error.set('Error al conectar con el servidor');
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
  }

  createClient(): void {
    const client = this.newClient();

    if (!client.name || !client.identification || !client.password) {
      this.error.set('Nombre, identificación y contraseña son obligatorios');
      return;
    }

    this.creating.set(true);
    this.error.set(null);

    this.http
      .post<any>(`${this.baseUrl}/customers/register`, client)
      .subscribe({
        next: () => {
          this.creating.set(false);
          this.showForm.set(false);
          // Resetear formulario
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
          // Recargar lista
          this.loadClients();
        },
        error: (err) => {
          this.error.set('Error al crear cliente');
          this.creating.set(false);
          console.error('Error:', err);
        },
      });
  }

  updateClientField(field: keyof NewClient, value: any): void {
    this.newClient.update((client) => ({ ...client, [field]: value }));
  }
}
