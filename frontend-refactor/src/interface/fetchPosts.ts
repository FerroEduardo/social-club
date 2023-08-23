interface FetchPost {
  id: number
  description: string
  reputation: number
  createdAt: string
  modifiedAt: string
  authorId: number
  authorName: string
  authorImageUrl: string
  gameId: number
  gameName: string
  gameStudio: string
  imageUrl: string
}

export default interface FetchPosts {
  content: FetchPost[]
  pageable: Object
  last: boolean
  totalPages: number
  totalElements: number
  size: number
  number: number
  sort: Object
  numberOfElements: number
  first: boolean
  empty: boolean
}
