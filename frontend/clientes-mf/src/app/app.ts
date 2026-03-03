import { Component, signal, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Client } from './models/client.model';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit {
  protected readonly title = signal('clientes-mf');
  protected clients = signal<Client[]>([]);
  protected loading = signal(false);
  protected error = signal<string | null>(null);

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
}
