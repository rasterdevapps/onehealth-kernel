import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar">
      <div class="navbar-brand">OneHealth ERP</div>
      <div class="navbar-links">
        <a routerLink="/dashboard" routerLinkActive="active">Dashboard</a>
        <a routerLink="/his" routerLinkActive="active">HIS - Patients</a>
        <a routerLink="/emr" routerLinkActive="active">EMR - Encounters</a>
        <a routerLink="/lis" routerLinkActive="active">LIS - Lab Orders</a>
      </div>
    </nav>
    <main class="content">
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [`
    .navbar {
      display: flex;
      align-items: center;
      background: #1a237e;
      color: white;
      padding: 0 24px;
      height: 56px;
      gap: 32px;
    }
    .navbar-brand {
      font-size: 20px;
      font-weight: 700;
    }
    .navbar-links {
      display: flex;
      gap: 16px;
    }
    .navbar-links a {
      color: rgba(255,255,255,0.7);
      text-decoration: none;
      padding: 8px 16px;
      border-radius: 4px;
      transition: all 0.2s;
    }
    .navbar-links a:hover,
    .navbar-links a.active {
      color: white;
      background: rgba(255,255,255,0.15);
    }
    .content {
      padding: 24px;
      max-width: 1200px;
      margin: 0 auto;
    }
  `]
})
export class AppComponent {
  title = 'OneHealth ERP';
}
