<template>
  <n-card id="container">
    <n-card>
      <GameCard v-if="game" :game="game" />
    </n-card>
    <n-list>
      <n-list-item v-for="post in posts" :key="post.id">
        <PostContainer :post="post" />
      </n-list-item>
      <div v-if="posts.length === 0">
        <n-empty description="Nenhum post encontrado">
          <template #extra>
            <n-button size="small" @click="$router.push('/post')">
              Seja o primeiro a fazer uma postagem
            </n-button>
          </template>
        </n-empty>
      </div>
      <div ref="postContainer"></div>
    </n-list>
  </n-card>
</template>

<script lang="ts">
import { NList, NListItem, NCard, useMessage, NEmpty, NButton } from 'naive-ui';
import { ref, type Ref, type PropType } from 'vue';
import type Game from '@/interface/game';
import axios from 'axios';

import type Post from '@/interface/post';
import type IndexPostRequest from '@/interface/response/indexPostRequest';
import PostContainer from '@/components/post/PostContainer.vue';
import GameCard from '@/components/game/GameCard.vue';

export default {
  components: {
    NList,
    NListItem,
    NCard,
    PostContainer,
    NEmpty,
    NButton,
    GameCard
  },
  props: {
    gameId: {
      type: String as PropType<string>,
      required: true
    }
  },
  setup() {
    return {
      message: useMessage(),
      game: ref() as Ref<Game>
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
    fetchGame() {
      axios
        .get<Game>(`/game/${this.gameId}`)
        .then((response) => {
          this.game = response.data;
        })
        .catch((error) => {
          console.error({ error });
          this.message.error('Ocorreu um erro na busca do jogo');
        });
    },
    getPostPage() {
      if (this.isLoadingData || this.isLast) return;
      const page = this.page++;
      const size = this.pageSize;
      this.isLoadingData = true;

      axios
        .get<IndexPostRequest>(`/game/${this.gameId}/post?page=${page}&size=${size}`, {
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
                  imageUrl: post.gameImageUrl
                },
                author: {
                  id: post.authorId,
                  name: post.authorName,
                  imageUrl: post.authorImageUrl
                },
                userVote: post.userVote,
                createdAt: new Date(post.createdAt),
                modifiedAt: new Date(post.modifiedAt)
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
    this.fetchGame();
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
