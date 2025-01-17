import {Routes} from '@angular/router';
import {MainPageComponent} from "./pages/main-page/main-page.component";
import {RedirectPageComponent} from "./pages/redirect-page/redirect-page.component";

export const routes: Routes = [
  {path: '', component: MainPageComponent},
  {path: ':link', component: RedirectPageComponent},
];
