import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    authenticated: false,
    username: undefined,
    email: undefined,
    imageUrl: undefined,
  }),
  getters: {
    isAuthenticated() {
      return this.authenticated;
    },
    getProfile() {
      return {
        username: this.username,
        email: this.email,
        imageUrl: this.imageUrl,
      };
    },
  },
  actions: {
    setAuthenticated(authenticated) {
      this.authenticated = authenticated;
    },
    setProfile({ username, email, imageUrl }) {
      this.username = username;
      this.email = email;
      this.imageUrl = imageUrl;
    },
  },
});
