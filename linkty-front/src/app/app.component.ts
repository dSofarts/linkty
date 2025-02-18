import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {NgToastModule, ToasterPosition} from "ng-angular-popup";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgToastModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'linkty-front';
  protected readonly ToasterPosition = ToasterPosition;
}
