import { Routes } from "@angular/router";
import { GamesComponent } from "./games.component";
import { LobbyComponent } from "./lobby/lobby.component";

export const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        component: GamesComponent
      },
      {
        path: ':id',
        children: [
          {
            path: 'lobby',
            component: LobbyComponent,
          },
        ]
      }
    ]
  }
]
