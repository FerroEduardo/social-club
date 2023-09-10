<template>
  <n-card id="container">
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
    <n-list>
      <n-list-item v-for="(post, index) in posts" :key="post.id">
        <PostContainer :post="post" :high-loading-priority="index < 3" />
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
import {
  NList,
  NListItem,
  NCard,
  useMessage,
  NEmpty,
  NButton,
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
import { useUserStore } from '@/stores/userStore';

const GameCard = defineAsyncComponent(() => import('@/components/game/GameCard.vue'));

export default {
  components: {
    NList,
    NListItem,
    NCard,
    PostContainer,
    NEmpty,
    NButton,
    GameCard,
    CreatePost,
    NCollapse,
    NCollapseItem,
    NSelect,
    NDivider
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
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
