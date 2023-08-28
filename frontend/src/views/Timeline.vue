<template>
  <n-card id="container">
    <n-list>
      <n-list-item v-for="post in posts" :key="post.id">
        <PostContainer :post="post" />
      </n-list-item>
      <div ref="postContainer"></div>
    </n-list>
  </n-card>
</template>

<script lang="ts">
import { NList, NListItem, NCard, useMessage } from 'naive-ui';
import axios from 'axios';

import type Post from '@/interface/post';
import type IndexPostRequest from '@/interface/response/indexPostRequest';
import PostContainer from '@/components/post/PostContainer.vue';

export default {
  components: {
    NList,
    NListItem,
    NCard,
    PostContainer
  },
  setup() {
    return {
      message: useMessage()
    };
  },
  data() {
    return {
      posts: [] as Post[],
      page: 0,
      pageSize: 5,
      isLoadingData: false,
      isLast: false
    };
  },
  methods: {
    getPostPage() {
      if (this.isLoadingData || this.isLast) return;
      const page = this.page++;
      const size = this.pageSize;
      this.isLoadingData = true;

      axios
        .get<IndexPostRequest>(`/post?page=${page}&size=${size}`, {
          withCredentials: true
        })
        .then((request) => {
          this.isLast = request.data.last;
          this.posts.push(
            ...request.data.content.map((post) => {
              return {
                id: post.id,
                title: post.title,
                description: post.description,
                reputation: post.reputation,
                imageUrl: post.imageUrl,
                game: {
                  id: post.gameId,
                  name: post.gameName,
                  studio: post.gameStudio,
                  imageUrl: 'https://cdn.cloudflare.steamstatic.com/steam/apps/730/header.jpg'
                },
                author: {
                  id: post.authorId,
                  name: post.authorName,
                  imageUrl:
                    'https://avatars.cloudflare.steamstatic.com/b69c069ae57724cc0bdbcf4eff87d4bb4feb3def_full.jpg' // post.authorImageUrl
                },
                userVote: post.userVote,
                createdAt: new Date(post.createdAt)
              };
            })
          );
        })
        .catch((reason) => {
          this.message.error('Ocorreu um erro na busca por postagens');
          console.error(reason);
        })
        .finally(() => {
          this.isLoadingData = false;
        });
    },
    setupInfiniteScroll() {
      const observer = new IntersectionObserver(
        (entries) => {
          entries.forEach((entry) => {
            if (entry.isIntersecting) {
              this.getPostPage();
            }
          });
        },
        {
          root: null,
          rootMargin: '20px',
          threshold: 0
        }
      );
      observer.observe(this.$refs.postContainer as Element);
    }
  },
  mounted() {
    this.getPostPage();
    this.setupInfiniteScroll();
  }
};
</script>

<style scoped>
#container {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
