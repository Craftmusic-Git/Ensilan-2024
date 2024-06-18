import { HttpInterceptorFn } from '@angular/common/http';
import {environment} from "../../environments/environment";

export const apiKeyInterceptor: HttpInterceptorFn = (req, next) => {
  const key = localStorage.getItem(environment.apiKeyHeader);
  if (key != null) {
    const authReq = req.clone({
      headers: req.headers.set(environment.apiKeyHeader, key)
    });
    return next(authReq);
  }
  return next(req);
};
