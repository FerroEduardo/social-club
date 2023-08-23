<template>
  <n-card id="container">
    <n-list>
      <n-list-item v-for="post in posts" :key="post.id">
        <n-thing :title="post.title" :description="post.description">
          <template #header-extra>
            <n-button-group>
              <n-button size="small" type="success"> ⬆️ </n-button>
              <n-button size="small" type="info">
                {{ post.reputation }}
              </n-button>
              <n-button size="small" type="error"> ⬇️ </n-button>
            </n-button-group>
          </template>
          <img loading="lazy" :src="post.imageUrl" class="post-image" />
          <div style="display: flex; flex-direction: row">
            <n-popover trigger="hover" raw :show-arrow="false">
              <template #trigger>
                <div style="margin-right: auto">
                  {{ post.author.name }}
                </div>
              </template>
              <div>
                <img loading="lazy" :src="post.author.imageUrl" />
              </div>
            </n-popover>
            <n-popover trigger="hover" raw :show-arrow="false">
              <template #trigger>
                <div style="margin-left: auto">{{ post.game.name }} - {{ post.game.studio }}</div>
              </template>
              <div>
                <img loading="lazy" :src="post.game.imageUrl" />
              </div>
            </n-popover>
          </div>
        </n-thing>
      </n-list-item>
      <div ref="postContainer"></div>
    </n-list>
  </n-card>
</template>

<script lang="ts">
import { NList, NListItem, NThing, NCard, NButton, NButtonGroup, NPopover } from 'naive-ui';
import axios from 'axios';

import type Post from '@/interface/post';
import type FetchPosts from '@/interface/fetchPosts';

export default {
  components: {
    NList,
    NListItem,
    NThing,
    NCard,
    NButton,
    NButtonGroup,
    NPopover
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
      // eslint-disable-next-line no-plusplus
      const page = this.page++;
      const size = this.pageSize;
      this.isLoadingData = true;

      axios
        .get<FetchPosts>(`/post?page=${page}&size=${size}`, {
          withCredentials: true
        })
        .then((request) => {
          this.isLast = request.data.last;
          this.posts.push(
            ...request.data.content.map((post) => {
              return {
                id: post.id,
                title: 'Lorem ipsum dolor sit amet',
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
                }
              };
            })
          );
        })
        .catch((reason) => {
          this.$router.push('/');
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
      observer.observe(this.$refs.postContainer);
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

.post-image {
  width: 100%;
  max-width: 800px;
}
</style>
