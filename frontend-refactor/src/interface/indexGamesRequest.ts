import type Page from './page';

export interface FetchGame {
  id: number;
  name: string;
  studio: string;
}

export interface GamesRequest extends Page<FetchGame> {}
