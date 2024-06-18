import { Injectable } from '@angular/core';
import {BaseService} from "@core/service/base.service";
import {UserDto} from "@domain/user.dto";

@Injectable({
  providedIn: 'root'
})
export class UsersAdminService extends BaseService<UserDto> {
  constructor() {
    super('admin/users');
  }
}
