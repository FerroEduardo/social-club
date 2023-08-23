export default interface Page<T> {
  content: Array<T>;
  pageable: Object;
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: Object;
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}
