import {Component, inject} from '@angular/core';
import {ActivatedRoute, RouterLink, RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  activatedRoute = inject(ActivatedRoute)
}
