import {HttpClient} from "@angular/common/http";
import {inject, Injectable, signal} from '@angular/core';
import {UserClassEnum} from "@domain/user-class.enum";
import {UserDto} from "@domain/user.dto";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root',
})
export class UserAuthService {
  private static readonly LOCALSTORAGE_KEY = 'USER';

  private http = inject(HttpClient);
  user = signal<UserDto>(null);

  constructor(){
    try {
      this.user.set(this.getUser());
    } catch ( e ){
      console.warn('User not found in local storage')
      this.user.set(null);
    }
  }

  getUser(): UserDto{
    return this.user() ?? JSON.parse(localStorage.getItem(UserAuthService.LOCALSTORAGE_KEY));
  }

  signOut(): void{
    localStorage.removeItem(UserAuthService.LOCALSTORAGE_KEY);
    this.user.set(null);
  }

  signIn( username: string, lastname: string, userClass: UserClassEnum ){
    this.http.post<UserDto>(environment.apiUrl + '/public/users/auth', {username, lastname, userClass})
      .subscribe(user => {
        this.user.set(user);
        localStorage.setItem(UserAuthService.LOCALSTORAGE_KEY, JSON.stringify(user));
      });
  }
}
