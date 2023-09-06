<template>
  <n-thing id="post" :description="post.description" description-style="word-wrap: anywhere">
    <template #header>
      <n-popover trigger="hover" raw show-arrow placement="bottom">
        <template #trigger>
          <div>
            {{ post.author.name }}
          </div>
        </template>
        <img loading="lazy" :src="post.author.imageUrl" />
      </n-popover>
      <span style="font-weight: bold">{{ post.title }}</span>
    </template>
    <template #header-extra>
      <n-popover trigger="hover" show-arrow placement="bottom">
        <template #trigger>
          <div>
            {{ parseTimestamp(post.createdAt) }}
          </div>
        </template>
        <div style="text-align: right">
          Criado: {{ parseTimestamp(post.createdAt) }} <br />
          Modificado: {{ parseTimestamp(post.modifiedAt) }}
        </div>
      </n-popover>
    </template>
    <div>
      <img
        loading="lazy"
        :src="post.imageUrl"
        class="post-image"
        @click="handlePostImageClick"
        :style="{ cursor: enableShowPostLink ? 'pointer' : 'auto' }"
      />
      <div style="display: flex; flex-direction: row">
        <n-button-group>
          <n-button size="small" :quaternary="isUpVoteGhosted" type="success" @click="upvote">
            <template #icon>
              <n-icon>
                <ArrowUp />
              </n-icon>
            </template>
          </n-button>
          <n-button size="small" type="info">{{ reputation }}</n-button>
          <n-button size="small" :quaternary="isDownVoteGhosted" type="error" @click="downvote">
            <template #icon>
              <n-icon>
                <arrow-down />
              </n-icon>
            </template>
          </n-button>
        </n-button-group>
        <n-popover trigger="hover" raw :show-arrow="false">
          <template #trigger>
            <div style="margin-left: auto" @click="goToGame(post.game.id)">
              {{ post.game.name }} - {{ post.game.studio }}
            </div>
          </template>
          <div>
            <img loading="lazy" :src="post.game.imageUrl" />
          </div>
        </n-popover>
      </div>
    </div>
    <template v-if="showComments" #footer>
      <PostCommentSection :post-id="post.id" />
    </template>
  </n-thing>
</template>
<script lang="ts">
import { NThing, NButton, NButtonGroup, NPopover, useMessage, NIcon } from 'naive-ui';
import { ArrowUp, ArrowDown } from '@vicons/ionicons5';
import { defineComponent, type PropType } from 'vue';
import axios from 'axios';
import type Post from '@/interface/post';
import type PostVoteResponse from '@/interface/response/postVoteResponse';
import PostCommentSection from './PostCommentSection.vue';

export default defineComponent({
  components: {
    NThing,
    NButton,
    NButtonGroup,
    NPopover,
    PostCommentSection,
    NIcon,
    ArrowUp,
    ArrowDown
  },
  props: {
    post: {
      type: Object as PropType<Post>,
      required: true
    },
    enableShowPostLink: {
      type: Boolean,
      required: false,
      default: true
    },
    showComments: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  setup() {
    return {
      message: useMessage()
    };
  },
  computed: {
    isUpVoteGhosted() {
      return this.userVote === -1 || !this.userVote;
    },
    isDownVoteGhosted() {
      return this.userVote === 1 || !this.userVote;
    }
  },
  data() {
    return {
      commentInput: '' as string,
      userVote: this.post.userVote,
      reputation: this.post.reputation,
      isVoteRequestRunning: false
    };
  },
  methods: {
    handlePostImageClick() {
      if (this.enableShowPostLink) {
        this.$router.push(`/post/${this.post.id}`);
      }
    },
    upvote() {
      const value = this.userVote === 1 ? 0 : 1;
      this.vote(value);
    },
    downvote() {
      const value = this.userVote === -1 ? 0 : -1;
      this.vote(value);
    },
    vote(value: number) {
      if (this.isVoteRequestRunning) return;

      this.isVoteRequestRunning = true;
      this.reputation += value;
      axios
        .post<PostVoteResponse>(`/post/${this.post.id}/vote/${value}`)
        .then((response) => {
          this.reputation = response.data.reputation;
          this.userVote = value;
        })
        .catch((error) => {
          this.message.error('Ocorreu um erro durante o voto');
          console.error({ error });
        })
        .finally(() => {
          this.isVoteRequestRunning = false;
        });
    },
    parseTimestamp(date: Date) {
      return date.toLocaleDateString(undefined, {
        year: 'numeric',
        month: 'numeric',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric'
      });
    },
    goToGame(gameId: number) {
      if (this.$route.name !== 'post') {
        this.$router.push(`/game/${gameId}`);
      }
    }
  }
});
</script>
<style>
#post .n-thing-header__extra {
  /* margin-left: auto; */
  /* width: 100%; */
  display: flex;
}
</style>

<style scoped>
#post {
  max-width: 800px;
  width: 100%;
}
.post-image {
  width: 100%;
}
</style>
