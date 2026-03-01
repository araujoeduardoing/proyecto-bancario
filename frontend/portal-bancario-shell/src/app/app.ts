import { Component, signal } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal('Shell Bancario');

  navigationItems = [
    { name: 'Inicio', route: '/home', icon: '🏠' },
    { name: 'Clientes', route: '/clientes', icon: '👥' },
    { name: 'Cuentas', route: '/cuentas', icon: '💳' },
    { name: 'Movimientos', route: '/movimientos', icon: '↔️' },
    { name: 'Reportes', route: '/reportes', icon: '📊' },
  ];

  constructor(private router: Router) {}

  navigateTo(route: string) {
    this.router.navigate([route]);
  }

  isCurrentRoute(route: string): boolean {
    return this.router.url === route;
  }
}
