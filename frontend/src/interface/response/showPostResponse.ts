export default interface Post {
  id: number;
  description: string;
  reputation: number;
  createdAt: string;
  modifiedAt: string;
  authorId: number;
  authorName: string;
  authorImageUrl: string;
  gameId: number;
  gameName: string;
  gameStudio: string;
  imageUrl: string;
}
