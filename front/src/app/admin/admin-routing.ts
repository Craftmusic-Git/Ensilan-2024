import { Routes } from "@angular/router";
import { AdminComponent } from "./admin.component";
import { UsersComponent } from "./users/users.component";
import { QuestionsSetComponent } from "./questions-set/questions-set.component";
import { GamesComponent } from "./games/games.component";
import { QuestionsComponent } from "./questions-set/questions/questions.component";
import { GameComponent } from "./games/game/game.component";
import { inject } from "@angular/core";
import { AdminAuthService } from "../core/service/admin-auth.service";

export const routes: Routes = [
  {
    path: '',
    component: AdminComponent,
    canActivateChild: [() => inject(AdminAuthService).isAdmin()],
    children: [
      {
        path: 'users',
        component: UsersComponent,
      },
      {
        path: 'questions',
        children: [
          {
            path: '',
            component: QuestionsSetComponent,
          },
          {
            path: ':id',
            component: QuestionsComponent,
          }
        ]
      },
      {
        path: 'games',
        children: [
          {
            path: '',
            component: GamesComponent
          },
          {
            path: ':id',
            component: GameComponent
          }
        ]
      }
    ]
  }
]
