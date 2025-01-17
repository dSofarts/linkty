export interface LinkResponse {
  userId: string,
  links: Link[]
}

export interface Link {
  linkId: string,
  link: string,
  shortLink: string,
  limit: number,
  used: number,
  created: Date,
  expired: Date,
  valid: boolean
}

export interface Redirect {
  url: string
}
