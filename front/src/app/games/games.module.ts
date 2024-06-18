import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { routes } from "./games-routing";
import { CommonModule } from "@angular/common";

@NgModule({
  imports: [RouterModule.forChild(routes), CommonModule],
  exports: [RouterModule]
})
export class GamesModule { }
