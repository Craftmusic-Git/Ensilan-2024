import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink, RouterOutlet } from "@angular/router";
import { AdminAuthService } from '../core/service/admin-auth.service';
import { FormsModule } from "@angular/forms";

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [
    RouterOutlet,
    FormsModule,
    RouterLink
  ],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss'
})
export class AdminComponent {
  authService = inject(AdminAuthService);
  route = inject(ActivatedRoute);
  isAuth = signal<boolean>(false);
  apiKey = signal<string>('');
  error = signal<boolean>(false);

  constructor(){
    this.authService.isAdmin().subscribe(auth => {
      this.isAuth.set(auth)
    });
  }

  login(){
    this.authService.setApiKey(this.apiKey());
    this.authService.isAdmin().pipe().subscribe(auth => {
      this.error.set(!auth);
      this.isAuth.set(auth)
    });
  }

  signOut(){
    this.authService.signOut();
    this.isAuth.set(false);
  }

  isGameRoute() {
    return this.route.snapshot.firstChild?.routeConfig?.path === 'games';
  }

  isUsersRoute() {
    return this.route.snapshot.firstChild?.routeConfig?.path === 'users';
  }

  isQuestionsRoute() {
    return this.route.snapshot.firstChild?.routeConfig?.path === 'questions';
  }
}
