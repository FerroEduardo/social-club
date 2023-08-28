import type Page from '../page';

interface Post {
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
  imageUrl: string;
  userVote?: number;
}

export default interface IndexPostRequest extends Page<Post> {}
