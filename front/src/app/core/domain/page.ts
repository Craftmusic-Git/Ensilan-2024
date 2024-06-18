export abstract class Page<T> {
  content: T[];
  first: boolean;
  hasContent: boolean;
  last: true;
  number: number;
  numberOfElements: number;
  size: number;
  totalElements: number;
  totalPages: number;
}
