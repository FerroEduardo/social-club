<template>
  <PageHeader />
  <div class="bg-white py-24 sm:py-32" @scroll="onScroll">
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
        <div ref="postContainer"></div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import PageHeader from '../components/PageHeader.vue';
import Post from '../components/timeline/Post.vue';

export default {
  components: {
    PageHeader,
    Post,
  },
  data() {
    return {
      posts: [],
      page: 0,
      pageSize: 5,
      isLoadingData: false,
      isLast: false,
    };
  },
  methods: {
    getPostPage() {
      if (this.isLoadingData || this.isLast) return;
      // eslint-disable-next-line no-plusplus
      const page = this.page++;
      const size = this.pageSize;
      this.isLoadingData = true;

      axios.get(`/post?page=${page}&size=${size}`, {
        withCredentials: true,
      })
        .then((request) => {
          this.isLast = request.data.last;
          this.posts.push(...request.data.content);
        })
        .catch((reason) => {
          this.$router.push('/');
        }).finally(() => {
          this.isLoadingData = false;
        });
    },
    setupInfiniteScroll() {
      const observer = new IntersectionObserver((entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            this.getPostPage();
          }
        });
      }, {
        root: null,
        rootMargin: '20px',
        threshold: 0,
      });
      observer.observe(this.$refs.postContainer);
    },
  },
  mounted() {
    this.getPostPage();
    this.setupInfiniteScroll();
  },
};
</script>

<style>

</style>
