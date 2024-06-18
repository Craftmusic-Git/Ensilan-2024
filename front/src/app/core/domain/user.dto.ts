import { UserClassEnum } from "./user-class.enum";

export class UserDto {
  public id: string;
  public username: string;
  public lastname: string;
  public userClass: UserClassEnum
}
