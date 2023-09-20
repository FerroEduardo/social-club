<template>
  <div id="container">
    <n-card v-if="isGameTimeline && game" style="margin-bottom: 10px">
      <GameCard :game="game" />
    </n-card>
    <n-collapse>
      <n-collapse-item
        title="Criar postagem"
        name="create-post"
        :disabled="!userStore.isAuthenticated"
      >
        <CreatePost />
      </n-collapse-item>
    </n-collapse>
    <n-divider title-placement="right">
      <n-select
        v-model:value="currentFilter"
        :options="filterOptions"
        style="width: 100%"
        @update:value="handleUpdateFilter"
      />
    </n-divider>
    <div>
      <template v-if="isLoadingData && posts.length == 0">
        <PostSkeleton />
        <n-divider />
        <PostSkeleton />
      </template>
      <div v-for="(post, index) in posts" :key="post.id">
        <PostContainer :post="post" :high-loading-priority="index < 3" style="max-width: inherit" />
        <n-divider v-if="index != posts.length - 1" />
      </div>
      <n-empty v-if="posts.length === 0" description="Nenhum post encontrado" />
      <div ref="postContainer"></div>
    </div>
  </div>
</template>

<script lang="ts">
import {
  NCard,
  useMessage,
  NEmpty,
  NCollapse,
  NCollapseItem,
  NSelect,
  NDivider,
  type SelectOption
} from 'naive-ui';
import { ref, type Ref, defineAsyncComponent } from 'vue';
import axios from 'axios';

import type Post from '@/interface/post';
import type IndexPostRequest from '@/interface/response/indexPostRequest';
import PostContainer from '@/components/post/PostContainer.vue';
import CreatePost from '@/components/post/CreatePost.vue';
import type Game from '@/interface/game';
import PostSkeleton from '@/components/post/PostSkeleton.vue';
import { useUserStore } from '@/stores/userStore';

const GameCard = defineAsyncComponent(() => import('@/components/game/GameCard.vue'));

export default {
  components: {
    NCard,
    PostContainer,
    NEmpty,
    GameCard,
    CreatePost,
    NCollapse,
    NCollapseItem,
    NSelect,
    NDivider,
    PostSkeleton
  },
  setup() {
    return {
      message: useMessage(),
      game: ref() as Ref<Game>,
      userStore: useUserStore(),
      currentFilter: ref('most-recent') as Ref<string>,
      filterOptions: [
        {
          label: 'Maior reputação',
          value: 'highest-reputation'
        },
        {
          label: 'Menor reputação',
          value: 'lowest-reputation'
        },
        {
          label: 'Mais recente',
          value: 'most-recent'
        },
        {
          label: 'Menos recente',
          value: 'least-recent'
        }
      ] as SelectOption[]
    };
  },
  computed: {
    isGameTimeline() {
      return this.$route.query?.game !== undefined;
    },
    gameId() {
      return this.$route.query?.game;
    }
  },
  watch: {
    '$route.query.game'() {
      this.setupPage();
    }
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
      const filter = this.currentFilter;
      this.isLoadingData = true;

      const url = this.isGameTimeline
        ? `/game/${this.gameId}/post?page=${page}&size=${size}&filter=${filter}`
        : `/post?page=${page}&size=${size}&filter=${filter}`;

      axios
        .get<IndexPostRequest>(url)
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
                  avatarUrl: post.authorAvatarUrl,
                  miniAvatarUrl: post.authorMiniAvatarUrl
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
    },
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
    resetPageParams() {
      this.posts = [];
      this.page = 0;
      this.isLast = false;
    },
    setupPage() {
      this.resetPageParams();
      if (this.gameId) {
        this.fetchGame();
      }
      this.getPostPage();
    },
    handleUpdateFilter() {
      this.setupPage();
    }
  },
  mounted() {
    this.setupPage();
    this.setupInfiniteScroll();
  }
};
</script>

<style scoped>
#container {
  margin: auto;
  max-width: 800px;
  padding: 20px 10px;
}
</style>
