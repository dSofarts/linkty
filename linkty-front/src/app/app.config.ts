import {ApplicationConfig, importProvidersFrom, provideZoneChangeDetection} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideHttpClient, withInterceptors, withInterceptorsFromDi} from "@angular/common/http";
import {userInterceptor} from "./data/interceptor/user.interceptor";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {NgToastModule} from "ng-angular-popup";

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    importProvidersFrom([BrowserAnimationsModule, NgToastModule]),
    provideHttpClient(withInterceptorsFromDi()),
    provideHttpClient(withInterceptors([userInterceptor]))
  ]
};
