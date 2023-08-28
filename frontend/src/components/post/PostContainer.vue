<template>
  <n-thing
    id="post"
    :title="post.title"
    :description="post.description"
    description-style="word-wrap: anywhere"
  >
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
      <PostCommentSection :post-id="post.id" />
    </template>
  </n-thing>
</template>
<script lang="ts">
import { NThing, NButton, NButtonGroup, NPopover } from 'naive-ui';
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
    PostCommentSection
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
      reputation: this.post.reputation
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
      axios
        .post<PostVoteResponse>(`/post/${this.post.id}/vote/${value}`)
        .then((response) => {
          this.reputation = response.data.reputation;
          this.userVote = value;
        })
        .catch((error) => {
          // display vote failed
        });
    }
  }
});
</script>
<style>
#post .n-thing-header__extra {
  margin-left: auto;
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
