import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CookieService} from "ngx-cookie-service";
import {environment} from "../../environments/environment";
import {LinkResponse, Redirect} from "../interface/link.interface";
import {tap} from "rxjs";
import {FrontResponse} from "../interface/response.interface";

@Injectable({
  providedIn: 'root'
})
export class LinkService {
  http = inject(HttpClient)
  cookieService = inject(CookieService)
  baseApiUrl = environment.BASE_BACKEND_URL + "links/"

  userId: string | null = null

  get isLoggedIn(): boolean {
    if (!this.userId) {
      this.userId = this.cookieService.get("user-id");
    }
    return !!this.userId
  }

  createLink(payload: { link: string, limit: number, expired: Date }) {
    return this.http.post<LinkResponse>(`${this.baseApiUrl}create-link`, payload).pipe(
      tap(res => {
        this.userId = res.userId
        this.cookieService.set("user-id", this.userId)
      }))
  }

  getLinks(userId: string | null) {
    if (userId === null) {
      userId = this.cookieService.get("user-id");
    }

    return this.http.get<LinkResponse>(`${this.baseApiUrl}get/${userId}`).pipe(
      tap(res => {
        this.userId = res.userId
        this.cookieService.set("user-id", this.userId)
      })
    )
  }

  getRedirectUrl(shortLink: string) {
    return this.http.get<Redirect>(`${this.baseApiUrl}redirect/${shortLink}`)
  }

  deleteLink(linkId: string) {
    return this.http.delete<FrontResponse>(`${this.baseApiUrl}delete-link/${linkId}`)
  }

  logout() {
    this.cookieService.deleteAll()
    this.userId = null
  }
}
