import {Component, inject} from '@angular/core';
import {UsersAdminService} from "@core/service/users-admin.service";
import {UserStore} from "@core/store/user.store";

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [],
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss'
})
export class UsersComponent {
  userService = inject(UsersAdminService);
  userStore = inject(UserStore);

  constructor() {
    this.userStore.refresh();
  }

  deleteUser( id: string ){
    this.userService.delete(id).subscribe(() => {
      this.userStore.refresh();
    });
  }
}
