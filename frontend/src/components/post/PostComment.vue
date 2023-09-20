<template>
  <n-thing content-style="white-space: pre-line">
    <template #header>
      <div id="header">
        <img
          class="mini-avatar"
          loading="lazy"
          :src="comment.authorMiniAvatarUrl"
          width="20"
          height="20"
        />
        <span class="user-name">
          {{ comment.authorName }}
          <span v-if="isAuthor">(autor)</span>
        </span>
      </div>
    </template>
    <template v-if="isUserOwnerOfComment" #header-extra>
      {{ parseTimestamp(comment.createdAt) }}
      <n-popconfirm
        @positive-click="deleteComment"
        negative-text="Não"
        positive-text="Sim, quero remover"
      >
        <template #trigger>
          <n-button
            circle
            size="small"
            type="error"
            secondary
            :loading="isDeleting"
            style="margin-left: 4px"
          >
            <template #icon>
              <trash />
            </template>
          </n-button>
        </template>
        Tem certeza que deseja remover o comentário?
      </n-popconfirm>
    </template>
    {{ comment.value }}
  </n-thing>
</template>

<script lang="ts">
import { type PropType } from 'vue';
import { NThing, NButton, NPopconfirm, useMessage } from 'naive-ui';
import { Trash } from '@vicons/ionicons5';
import axios from 'axios';
import type Comment from '@/interface/comment';
import { useUserStore } from '@/stores/userStore';

export default {
  components: {
    NThing,
    NButton,
    Trash,
    NPopconfirm
  },
  props: {
    comment: {
      type: Object as PropType<Comment>,
      required: true
    },
    postAuthorId: {
      type: Number as PropType<Number>,
      required: true
    }
  },
  setup(props) {
    const userStore = useUserStore();
    const isUserOwnerOfComment = userStore.profile?.id === props.comment.authorId;
    const isAuthor = props.postAuthorId === props.comment.authorId;

    return {
      isAuthor,
      isUserOwnerOfComment,
      message: useMessage()
    };
  },
  data() {
    return {
      isDeleting: false
    };
  },
  methods: {
    parseTimestamp(timestamp: string) {
      return new Date(timestamp).toLocaleDateString(undefined, {
        year: 'numeric',
        month: 'numeric',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric'
      });
    },
    deleteComment() {
      this.isDeleting = true;
      axios
        .delete(`/comment/${this.comment.id}`)
        .then((response) => {
          this.$emit('refreshList');
        })
        .catch((error) => {
          this.isDeleting = false;
          this.message.error('Ocorreu um erro na remoção do comentário');
          console.error({ error });
        });
    }
  }
};
</script>

<style scoped>
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
