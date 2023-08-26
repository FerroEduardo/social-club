import { defineStore } from 'pinia';

import type UserProfile from '../interface/userProfile';

export const useUserStore = defineStore('user', {
  state: () => ({
    authenticated: false as boolean,
    profile: undefined as UserProfile | undefined
  }),
  getters: {
    isAuthenticated(): boolean {
      return this.authenticated;
    },
    getProfile(): UserProfile | undefined {
      return this.profile;
    }
  },
  actions: {
    setAuthenticated(authenticated: boolean) {
      this.authenticated = authenticated;
    },
    setProfile(id: number, name: string, email: string, imageUrl: string) {
      const profile: UserProfile = {
        id,
        name,
        email,
        imageUrl
      };
      this.profile = profile;
    }
  }
});
