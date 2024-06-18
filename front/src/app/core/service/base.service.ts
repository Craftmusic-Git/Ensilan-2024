import {HttpClient, HttpParams} from "@angular/common/http";
import {inject, signal} from "@angular/core";
import {Page} from "@domain/page";
import {PageableRequest} from "@domain/pageable-request";
import {catchError, EMPTY, finalize, first} from "rxjs";
import {environment} from "../../../environments/environment";

export class BaseService<DTO> {
  protected http = inject(HttpClient);
  protected path: string;

  constructor(path: string) {
    this.path = path;
  }

  protected _loading = signal(false);

  get loading() {
    return this._loading();
  }

  findAll(pageable?: PageableRequest) {
    let params = new HttpParams();

    if (pageable?.page != null) {
      params = params.append('page', pageable.page);
    }
    if (pageable?.size != null) {
      params = params.append('size', pageable.size);
    }
    if (pageable?.sort != null) {
      params = params.append('sort', pageable.sort);
    }
    if (pageable?.query != null) {
      params = params.append('query', pageable.query);
    }

    this._loading.set(true);
    return this.http
      .get<Page<DTO>>(environment.apiUrl + `/${this.path}`, {
        params,
      })
      .pipe(
        first(),
        catchError(() => EMPTY),
        finalize(() => this._loading.set(false))
      );
  }

  get(id: string) {
    this._loading.set(true);
    return this.http.get<DTO>(environment.apiUrl + `/${this.path}/` + id).pipe(
      first(),
      finalize(() => this._loading.set(false))
    );
  }

  create(dto: DTO) {
    this._loading.set(true);
    return this.http.post<DTO>(environment.apiUrl + `/${this.path}`, dto).pipe(
      first(),
      catchError(() => EMPTY),
      finalize(() => this._loading.set(false))
    );
  }

  update(id: string, dto: DTO) {
    this._loading.set(true);
    return this.http.put<DTO>(environment.apiUrl + `/${this.path}/` + id, dto).pipe(
      first(),
      catchError(() => EMPTY),
      finalize(() => this._loading.set(false))
    );
  }

  delete(id: string) {
    this._loading.set(true);
    return this.http.delete<void>(environment.apiUrl + `/${this.path}/` + id).pipe(
      first(),
      catchError(() => EMPTY),
      finalize(() => this._loading.set(false))
    );
  }

}
