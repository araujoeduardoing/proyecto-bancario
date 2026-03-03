import { Component, signal, inject, OnInit, computed } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Client } from './models/client.model';

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
}
