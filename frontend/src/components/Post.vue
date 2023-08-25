<template>
  <n-thing :title="post.title" :description="post.description" style="max-width: 800px">
    <template #header-extra>
      <n-button-group>
        <n-button size="small" :ghost="isUpVoteGhosted" type="success" @click="upvote">⬆️</n-button>
        <n-button size="small" type="info">{{ reputation }}</n-button>
        <n-button size="small" :ghost="isDownVoteGhosted" type="error" @click="downvote"
          >⬇️</n-button
        >
      </n-button-group>
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
    </div>
    <template v-if="showComments" #footer>
      <n-card title="Comentários">
        <n-list v-if="comments.length > 0">
          <n-list-item v-for="comment in comments" :key="comment.id">
            <n-thing
              :title="comment.authorName"
              :title-extra="parseTimestamp(comment.createdAt)"
              :description="comment.value"
            />
          </n-list-item>
        </n-list>
        <n-empty v-else description="Seja o primeiro a comentar" />
        <template #action>
          <n-input-group>
            <n-input
              v-model:value="commentInput"
              placeholder="O que está pensando?"
              type="textarea"
              :autosize="{
                minRows: 1,
                maxRows: 5
              }"
              :maxlength="200"
            />
            <n-button type="primary" ghost @click="sendComment"> Comentar </n-button>
          </n-input-group>
        </template>
      </n-card>
    </template>
  </n-thing>
</template>
<script lang="ts">
import {
  NThing,
  NButton,
  NButtonGroup,
  NPopover,
  NCard,
  NList,
  NListItem,
  NEmpty,
  NInput,
  NInputGroup
} from 'naive-ui';
import { defineComponent, type PropType } from 'vue';
import axios from 'axios';
import type Post from '@/interface/post';
import type PostVoteResponse from '@/interface/response/postVoteResponse';
import type IndexCommentResponse from '@/interface/response/indexCommentResponse';
import type Comment from '@/interface/comment';

export default defineComponent({
  components: {
    NThing,
    NButton,
    NButtonGroup,
    NPopover,
    NCard,
    NList,
    NListItem,
    NEmpty,
    NInput,
    NInputGroup
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
      comments: [] as Comment[]
    };
  },
  methods: {
    parseTimestamp(timestamp: string) {
      return new Date(timestamp).toLocaleDateString(undefined, {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric'
      });
    },
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
      axios
        .post<PostVoteResponse>(`/post/${this.post.id}/vote/${value}`)
        .then((response) => {
          this.reputation = response.data.reputation;
          this.userVote = value;
        })
        .catch((error) => {
          // display vote failed
        });
    },
    indexComments() {
      // TODO infinite scroll
      axios
        .get<IndexCommentResponse>(`/post/${this.post.id}/comment?page=0&size=100`)
        .then((response) => {
          this.comments = response.data.content;
        })
        .catch((error) => {
          // display index failed
        });
    },
    sendComment() {
      axios
        .post<IndexCommentResponse>(`/post/${this.post.id}/comment`, {
          value: this.commentInput
        })
        .then((response) => {
          this.commentInput = '';
          this.indexComments();
        })
        .catch((error) => {
          // display index failed
        });
    }
  },
  mounted() {
    this.indexComments();
  }
});
</script>
<style scoped>
.post-image {
  width: 100%;
}
</style>
