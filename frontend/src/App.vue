<template>
  <div>
    <button type="button" @click="oauthGoogle">Google OAuth</button>
    <button type="button" @click="oauthGithub">GitHub OAuth</button>
    <button type="button" @click="listPosts">List Posts</button>
    <div v-for="post in posts" :key="post.id">
      post id: {{ post.id }} <br>
      username: {{ post.user.name }} <br>
      game: {{ post.game.name }} - studio: {{ post.game.studio }} <br>
      <img :src="getImageSrc(post.image)" style="max-width: 200px;" />
    </div>
  </div>
</template>

<script>
import axios from 'axios'
const API_URL = import.meta.env.VITE_API_URL

export default {
  data() {
    return {
      posts: [],
    }
  },
  methods: {
    oauthLogin(platform) {
      location.href = `${API_URL}/oauth2/authorize/${platform}`
    },
    oauthGoogle() {
      this.oauthLogin('google')
    },
    oauthGithub() {
      this.oauthLogin('github')
    },
    listPosts() {
      axios.get(`${API_URL}/post`, {
        withCredentials: true
      }).then(response => {
        console.log(response.data);
        this.posts = response.data.content
      })
    },
    getImageSrc(imageData) {
      if (imageData.blob) {
        return `data:image/png;base64,${imageData.blob}`;
      }

      return imageData.s3 ?? imageData.local;
    }
  },
  mounted() {
  }
}
</script>

<style scoped>
</style>
