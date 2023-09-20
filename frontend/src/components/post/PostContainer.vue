<template>
  <n-thing id="post" :description="post.description" description-style="word-wrap: anywhere">
    <template #header>
      <div id="header">
        <img
          class="mini-avatar"
          loading="lazy"
          :src="post.author.miniAvatarUrl"
          width="32"
          height="32"
        />
        <span class="user-name">{{ post.author.name }}</span>
      </div>
      <span style="font-weight: bold">{{ post.title }}</span>
    </template>
    <template #header-extra>
      <n-popover trigger="hover" show-arrow placement="bottom" :keep-alive-on-hover="false">
        <template #trigger>
          <div style="cursor: default">
            {{ parseTimestamp(post.createdAt) }}
          </div>
        </template>
        <div style="text-align: right; cursor: default">
          Criado: {{ parseTimestamp(post.createdAt) }} <br />
          Modificado: {{ parseTimestamp(post.modifiedAt) }}
        </div>
      </n-popover>
    </template>
    <div>
      <img
        :loading="highLoadingPriority ? 'eager' : 'lazy'"
        :src="post.imageUrl"
        class="post-image"
        @click="openPostDetails"
        :style="{ cursor: enableOpenPostDetails ? 'pointer' : 'default' }"
      />
      <div style="display: flex; flex-direction: row">
        <PostVoteButtons
          :reputation="reputation"
          :user-vote="userVote"
          :post-id="post.id"
          @update-reputation="updateReputation"
          @update-user-vote="updateUserVote"
        />
        <PostManagementButtons
          v-if="showManagementButtons && isUserOwnerOfPost"
          :post-id="post.id"
          @update-post="onPostUpdated"
        />
        <n-popover trigger="hover" raw :show-arrow="false" :keep-alive-on-hover="false">
          <template #trigger>
            <div style="margin-left: auto; cursor: pointer" @click="goToGame(post.game.id)">
              {{ post.game.name }}
            </div>
          </template>
          <div>
            <img loading="lazy" :src="post.game.imageUrl" />
          </div>
        </n-popover>
      </div>
    </div>
    <template v-if="showComments" #footer>
      <PostCommentSection :post-id="post.id" :post-author-id="post.author.id" />
    </template>
  </n-thing>
</template>
<script lang="ts">
import { NThing, NPopover, useMessage } from 'naive-ui';
import { type PropType, defineAsyncComponent } from 'vue';
import type Post from '@/interface/post';
import { useUserStore } from '@/stores/userStore';
import PostVoteButtons from './PostVoteButtons.vue';
const PostCommentSection = defineAsyncComponent(() => import('./PostCommentSection.vue'));
const PostManagementButtons = defineAsyncComponent(() => import('./PostManagementButtons.vue'));

export default {
  components: {
    NThing,
    NPopover,
    PostCommentSection,
    PostVoteButtons,
    PostManagementButtons
  },
  props: {
    post: {
      type: Object as PropType<Post>,
      required: true
    },
    enableOpenPostDetails: {
      type: Boolean,
      required: false,
      default: true
    },
    showComments: {
      type: Boolean,
      required: false,
      default: false
    },
    showManagementButtons: {
      type: Boolean,
      required: false,
      default: false
    },
    highLoadingPriority: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  emits: {
    'update-post': () => true
  },
  setup(props) {
    const userStore = useUserStore();
    const isUserOwnerOfPost = userStore.profile?.id === props.post.author.id;

    return {
      message: useMessage(),
      isUserOwnerOfPost
    };
  },
  data() {
    return {
      commentInput: '' as string,
      userVote: this.post.userVote as number,
      reputation: this.post.reputation as number
    };
  },
  methods: {
    openPostDetails() {
      if (this.enableOpenPostDetails) {
        this.$router.push(`/post/${this.post.id}`);
      }
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
        this.$router.push({
          name: 'timeline',
          query: {
            game: gameId
          }
        });
      }
    },
    updateReputation(newValue: number) {
      this.reputation = newValue;
    },
    updateUserVote(newValue: number) {
      this.userVote = newValue;
    },
    onPostUpdated() {
      this.$emit('update-post');
    }
  }
};
</script>
<style>
#post .n-thing-header__extra {
  /* margin-left: auto; */
  /* width: 100%; */
  display: flex;
}
</style>

<style scoped>
.post-image {
  width: 100%;
}
#header {
  cursor: default;
  .mini-avatar {
    border-radius: 10%;
    vertical-align: middle;
    object-fit: cover;
  }
  .user-name {
    vertical-align: middle;
    margin-left: 4px;
  }
}
</style>
