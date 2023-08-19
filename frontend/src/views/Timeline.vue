<template>
  <PageHeader :isMenuOpen="isMenuOpen" :menus="menus" @openMenu="openMenu" @closeMenu="closeMenu" />
  <div class="bg-white py-24 sm:py-32">
    <div class="mx-auto max-w-7xl px-6 lg:px-8">
      <div class="mx-auto max-w-2xl lg:mx-0">
        <h2 class="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
          Lorem ipsum dolor
        </h2>
        <p class="mt-2 text-lg leading-8 text-gray-600">
          Lorem ipsum dolor sit amet, consectetur adipiscing elit
        </p>
      </div>
      <div class="mx-auto max-w-2xl gap-x-8 pt-10 lg:max-w-none">
        <Post v-for="post in posts" v-bind="post" :key="post.id" />
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import PageHeader from '../components/PageHeader.vue';
import Post from '../components/timeline/Post.vue';

const API_URL = import.meta.env.VITE_API_URL;

export default {
  components: {
    PageHeader,
    Post,
  },
  data() {
    return {
      posts: [],
    };
  },
  methods: {
    getPosts() {
      axios.get(`${API_URL}/post`, {
        withCredentials: true,
      })
        .then((request) => {
          this.posts = request.data.content;
        })
        .catch((reason) => {
          this.$router.push('/');
        });
    },
  },
  mounted() {
    this.getPosts();
  },
};
</script>

<style>

</style>
