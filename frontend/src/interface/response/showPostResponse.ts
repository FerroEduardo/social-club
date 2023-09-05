export default interface Post {
  id: number;
  title: string;
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
  gameImageUrl: string;
  imageUrl: string;
  userVote?: number;
}
