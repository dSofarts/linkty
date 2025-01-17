import {HttpInterceptorFn, HttpRequest} from "@angular/common/http";
import {inject} from "@angular/core";
import {environment} from "../../environments/environment";
import {CookieService} from "ngx-cookie-service";

export const userInterceptor: HttpInterceptorFn = (req, next) => {
  const userId = inject(CookieService).get("user-id")

  if (!userId) return next(addServiceName(req));
  return next(addUserId(req, userId));
}

const addUserId = (req: HttpRequest<any>, userId: string) => {
  const serviceName = environment.SERVICE_NAME;
  return req.clone({
    setHeaders: {
      'x-user-id': userId,
      'service-name': serviceName
    }
  })
}

const addServiceName = (req: HttpRequest<any>) => {
  const serviceName = environment.SERVICE_NAME;
  return req.clone({
    setHeaders: {
      'service-name': serviceName
    }
  })
}


