<template>
  <div id="container">
    <n-input
      v-model:value="gameInput"
      placeholder="Counter Strike: 2"
      :on-input="onInput"
      clearable
      :loading="isLoading"
    />
    <n-list hoverable clickable>
      <n-list-item v-for="game in gameList" :key="game.id" @click="handleGameSelect(game)">
        <GameCard :game="game" />
      </n-list-item>
      <div ref="postContainerGame"></div>
    </n-list>
  </div>
</template>
<script lang="ts">
import axios from 'axios';
import { useMessage, NInput, NList, NListItem } from 'naive-ui';
import { ref, type Ref } from 'vue';
import type IndexGameResponse from '@/interface/response/indexGameResponse';
import type Game from '@/interface/game';
import GameCard from '@/components/game/GameCard.vue';

export default {
  components: {
    NInput,
    NList,
    NListItem,
    GameCard
  },
  setup() {
    return {
      message: useMessage(),
      gameList: ref([]) as Ref<Game[]>,
      gameInput: ref('') as Ref<string>,
      isLoading: ref(false) as Ref<boolean>,
      page: ref(0) as Ref<number>
    };
  },
  computed: {
    gameOptions() {
      return this.gameList;
    }
  },
  methods: {
    onInput() {
      this.gameList = [];
      this.page = 0;
      this.fetchGames();
    },
    fetchGames() {
      const page = this.page++;
      const name = this.gameInput ?? '';

      if (name.trim().length === 0 || this.isLoading) {
        return;
      }
      this.isLoading = true;

      axios
        .get<IndexGameResponse>(`/game?page=${page}&size=20&name=${name}`)
        .then((response) => {
          this.gameList.push(...response.data.content);
        })
        .catch((error) => {
          console.error({ error });
          this.message.error('Ocorreu um erro na busca por jogos');

          this.gameList = [];
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
    handleGameSelect(game: Game) {
      this.$router.push({
        name: 'timeline',
        query: {
          game: game.id
        }
      });
    },
    setupInfiniteScroll() {
      const observer = new IntersectionObserver(
        (entries) => {
          entries.forEach((entry) => {
            if (entry.isIntersecting) {
              this.fetchGames();
            }
          });
        },
        {
          root: null,
          rootMargin: '20px',
          threshold: 0
        }
      );
      observer.observe(this.$refs.postContainerGame as Element);
    }
  },
  mounted() {
    this.setupInfiniteScroll();
  }
};
</script>
<style scoped>
#container {
  max-width: 600px;
  margin: auto;
  padding: 5px;
}
</style>
