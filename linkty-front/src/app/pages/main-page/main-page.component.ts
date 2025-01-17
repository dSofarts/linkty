import {Component, inject} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgToastService} from "ng-angular-popup";
import {LinkService} from "../../data/service/link.service";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {FrontResponse} from "../../data/interface/response.interface";
import {map, Observable, of} from "rxjs";
import {Link} from "../../data/interface/link.interface";
import {environment} from "../../environments/environment";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    NgForOf,
    AsyncPipe,
    RouterLink,
  ],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.scss'
})
export class MainPageComponent {

  toastService = inject(NgToastService)
  linkService = inject(LinkService)
  redirectUrl = environment.REDIRECT_URL

  createLinkForm = new FormGroup({
    link: new FormControl(null, [Validators.required,
      Validators.pattern(/^(http:\/\/|https:\/\/)[\w-]+(\.[\w-]+)+([\w.,@?^=%&:/~+#-]*[\w@?^=%&/~+#-])?$/i)]),
    expired: new FormControl(null, [Validators.required]),
    limit: new FormControl(null),
  })

  userIdForm = new FormGroup({
    userId: new FormControl(null, [Validators.required,
      Validators.pattern(/^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$/)]),
  })

  links$: Observable<Link[]>

  constructor() {
    this.links$ = this.linkService.getLinks(null).pipe(
      map(response => response.links)
    )
  }

  createLink($event: any) {
    if (this.createLinkForm.valid) {
      // @ts-ignore
      this.linkService.userId = this.userIdForm.value.userId?.toString()
      const formValue = this.createLinkForm.value;

      if (formValue.expired) {
        // @ts-ignore
        formValue.expired = this.formatDate(formValue.expired);
      }

      // @ts-ignore
      this.linkService.createLink(this.createLinkForm.value).subscribe({
        next: (res) => {
          this.createLinkForm.reset();
          this.links$ = of(res.links)
        },
        error: (err: FrontResponse) => {
          this.toastService.danger(err.status, "Возникла ошибка", 5000)
        }
      })
    } else {
      this.toastService.warning("Пожалуйста корректно заполните все поля", "Ошибка", 5000)
    }
  }

  private formatDate(date: string): string {
    const dateObj = new Date(date)
    const timezoneOffset = -dateObj.getTimezoneOffset()
    const sign = timezoneOffset >= 0 ? '+' : '-'
    const hours = Math.floor(Math.abs(timezoneOffset) / 60)
      .toString()
      .padStart(2, '0')
    const minutes = (Math.abs(timezoneOffset) % 60).toString().padStart(2, '0')
    const timezone = `${sign}${hours}:${minutes}`

    const localISO = dateObj.toISOString().slice(0, -1)
    return `${localISO}${timezone}`
  }


  deleteLink(link: Link) {
    this.linkService.deleteLink(link.linkId).subscribe({
      next: () => {
        this.links$ = this.links$.pipe(
          map(links => links.filter(existingLink => existingLink.linkId !== link.linkId))
        )
        this.toastService.info("Ссылка успешно удалена", "Удаление", 5000)
      },
      error: (err: FrontResponse) => {
        this.toastService.danger(err.status, "Ошибка", 5000)
      }
    })
  }

  auth($event: any) {
    if (this.userIdForm.valid) {
      // @ts-ignore
      this.linkService.getLinks(this.userIdForm.value.userId).subscribe({
        next: (res) => {
          this.links$ = of(res.links)
          this.userIdForm.reset()
        },
        error: () => {
          this.toastService.warning("Неправильный id пользователя", "Ошибка", 5000)
        }
      })
    } else {
      this.toastService.warning("Неправильный id пользователя", "Ошибка", 5000)
    }
  }

  logout() {
    this.linkService.logout()
    this.toastService.info("Вы успешно вышли", "Выход из аккаунта", 5000)
  }
}
