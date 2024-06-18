import { Injectable, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { catchError, EMPTY, finalize, map, of, tap } from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AdminAuthService {
  apiUrl = environment.apiUrl + '/admin';

  _loading = signal(false);

  constructor( private http: HttpClient ){
  }

  getApiKey(){
    return localStorage.getItem(environment.apiKeyHeader);
  }

  setApiKey( apiKey: string ){
    localStorage.setItem(environment.apiKeyHeader, apiKey);
  }

  private getHttpHeaders(): HttpHeaders{
    const apiKey = this.getApiKey();
    return new HttpHeaders({
      "x-api-key": apiKey ?? ""
    });
  }

  isAdmin(){
    this._loading.set(true);
    return this.http.get<{ user: string }>(`${this.apiUrl}`, {headers: this.getHttpHeaders()}).pipe(
      map(response => {
        return response.user === 'Admin'
      }),
      catchError(() => of(false)),
      finalize(() => this._loading.set(false)));
  }

  signOut(){
    localStorage.removeItem(environment.apiKeyHeader);
  }
}
