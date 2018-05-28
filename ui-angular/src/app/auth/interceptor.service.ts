import { Injectable } from '@angular/core';
import {AuthenticationService} from './authentication.service';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
@Injectable()
export class InterceptorService implements HttpInterceptor {

  constructor(private authService: AuthenticationService) {}
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    if ((request.url.match("http://localhost:8081/transaction/*") && (request.method == "GET"))) {
      console.log("Request is not intercepted. URL doesn't match")
    } else {

      //if(request.url != ("http://localhost:8081/transaction/list" || "http://localhost:8081/transaction/*")) {
      console.log("Request URL: " + request.url);
      console.log("Interceptor in action");
      request = request.clone({
        setHeaders: {
          Authorization: "Basic " + this.authService.getToken()
        }
      });
      console.log("Auth header is: " + request.headers.get("Authorization"));
    }
    return next.handle(request);
  }
}
