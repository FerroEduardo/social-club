export default interface Post {
  id: number
  title: string
  description: string
  reputation: number
  imageUrl: string
  game: {
    id: number
    name: string
    studio: string
    imageUrl: string
  }
  author: {
    id: number
    name: string
    imageUrl: string
  }
}
