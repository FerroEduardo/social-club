import type Game from './game';

export default interface Post {
  id: number;
  title: string;
  description: string;
  reputation: number;
  userVote?: number;
  imageUrl: string;
  game: Game;
  author: {
    id: number;
    name: string;
    avatarUrl: string;
    miniAvatarUrl: string;
  };
  createdAt: Date;
  modifiedAt: Date;
}
