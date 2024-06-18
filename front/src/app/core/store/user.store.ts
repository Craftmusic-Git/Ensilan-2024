import { Injectable } from "@angular/core";
import {UsersAdminService} from "@core/service/users-admin.service";
import {UsersService} from "@core/service/users.service";
import {AbstractStore} from "@core/store/abstract.store";
import {UserDto} from "@domain/user.dto";

@Injectable({providedIn: 'root'})
export class UserStore extends AbstractStore<UserDto>{
  constructor(userService: UsersAdminService){
    super(userService);
  }
}
