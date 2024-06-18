import {output, signal} from "@angular/core";
import {BaseService} from "@core/service/base.service";
import {finalize} from "rxjs";
import { Page } from "@domain/page";
import { PageableRequest } from "@domain/pageable-request";

export abstract class AbstractStore<DTO> {
  private service: BaseService<DTO>;
  protected defaultPageable = new PageableRequest();

  protected _pageRequest = signal<PageableRequest>(this.defaultPageable);
  protected _loading = signal<boolean>(true);
  protected _page = signal<Page<DTO>>(null);

  $loaded = output();

  protected constructor(service: BaseService<DTO>) {
    this.service = service;
  }

  protected loadData() {
    this._loading.set(true);

    this.service
      .findAll(this._pageRequest())
      .pipe(finalize(() => {
        this.$loaded.emit();
        this._loading.set(false)
      }))
      .subscribe(page => this._page.set(page));
  }

  get loading(): boolean {
    return this._loading();
  }

  get page(): Page<DTO> {
    return this._page();
  }

  get records(): DTO[] {
    return this._page()?.content ?? [];
  }

  get totalRecord(): number {
    return this._page()?.totalElements;
  }

  onLazyLoad({ first, rows }: { first: number; rows: number }) {
    const page = first / rows;
    this._pageRequest.update(value => {
      return {
        ...value,
        page: rows != null ? page : undefined,
        size: rows,
      };
    });

    this.loadData();
  }

  load(pageable?: PageableRequest) {
    if (pageable != null) {
      this._pageRequest.set(pageable);
    }

    this.loadData();
  }

  search(query: string, pageSize: number = 10) {
    const pageable = Object.assign(new PageableRequest(), {
      page: 0,
      size: pageSize,
      query,
    } as PageableRequest);

    this._pageRequest.set(pageable);
    this.loadData();
  }

  refresh() {
    this.loadData();
  }

  reset() {
    this._pageRequest.set(this.defaultPageable);
    this._page.set(null)
  }

}
