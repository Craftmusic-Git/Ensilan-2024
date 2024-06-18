import {Component, inject, signal} from '@angular/core';
import {FormBuilder, FormControl, ReactiveFormsModule} from "@angular/forms";
import {ActivatedRoute, RouterLink, RouterOutlet} from "@angular/router";
import {UserAuthService} from '@app/core/service/user-auth.service';
import {GamesPlayerStore} from '@app/core/store/games-player.store';
import {GameStateEnum} from "@domain/games/game-state.enum";
import {UserClassEnum} from "@domain/user-class.enum";

@Component({
  selector: 'app-games',
  standalone: true,
  imports: [
    RouterOutlet,
    ReactiveFormsModule,
    RouterLink,
  ],
  templateUrl: './games.component.html',
  styleUrl: './games.component.scss',
})
export class GamesComponent {
  gamesStore = inject(GamesPlayerStore);
  userAuthService = inject(UserAuthService);
  activatedRoute = inject(ActivatedRoute);
  fb = inject(FormBuilder);

  form = this.fb.group({
    username: '',
    lastname: '',
    userClass: new FormControl(UserClassEnum.EXTERN),
  });

  games = this.gamesStore.games;
  isAuth = signal(false);

  constructor(){
    this.isAuth.set(this.userAuthService.getUser() != null);
  }

  onSubmit(){
    const credential = {
      username: this.form.get('username').value,
      lastname: this.form.get('lastname').value,
      userClass: this.form.get('userClass').value,
    };
    this.userAuthService.signIn(credential.username, credential.lastname, credential.userClass);
    this.isAuth.set(true);
  }

  statusClass( status: GameStateEnum ){
    switch ( status ) {
      case GameStateEnum.IN_PREPARATION:
        return 'text-blue-400';
      case GameStateEnum.READY:
        return 'text-green-400';
      case GameStateEnum.IN_PROGRESS:
        return 'text-yellow-800';
      case GameStateEnum.FINISHED:
        return 'text-red-500';
      default:
        return '';
    }
  }

  logout(){
    this.userAuthService.signOut();
    this.isAuth.set(false);
  }

  enumKeys = Object.keys(UserClassEnum);
  protected readonly GameStateEnum = GameStateEnum;
}
