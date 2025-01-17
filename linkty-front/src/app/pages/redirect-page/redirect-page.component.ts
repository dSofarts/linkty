import {Component, inject, signal} from '@angular/core';
import {ActivatedRoute, RouterLink} from "@angular/router";
import {LinkService} from "../../data/service/link.service";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-redirect-page',
  standalone: true,
  imports: [
    NgIf,
    RouterLink
  ],
  templateUrl: './redirect-page.component.html',
  styleUrl: './redirect-page.component.scss'
})
export class RedirectPageComponent {
  // @ts-ignore
  route = inject(ActivatedRoute).snapshot.paramMap.get('link');
  linkService = inject(LinkService);
  redirect = signal<boolean>(true)

  ngOnInit() {
    if (this.route) {
      this.linkService.getRedirectUrl(this.route).subscribe({
        next: (res) => {
          window.location.href = res.url
        },
        error: () => {
          this.redirect.set(false)
        }
      })
    }
  }
}
