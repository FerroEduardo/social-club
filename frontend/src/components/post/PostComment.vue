<template>
  <n-thing
    :title="comment.authorName"
    :title-extra="parseTimestamp(comment.createdAt)"
    content-style="white-space: pre-line"
  >
    <template v-if="isUserOwnerOfComment" #header-extra>
      <n-button
        circle
        size="small"
        type="error"
        secondary
        @click="deleteComment"
        :loading="isDeleting"
      >
        <template #icon>
          <trash />
        </template>
      </n-button>
    </template>
    {{ comment.value }}
  </n-thing>
</template>
<script lang="ts">
import { type PropType } from 'vue';
import { NThing, NButton } from 'naive-ui';
import { Trash } from '@vicons/ionicons5';
import axios from 'axios';
import type Comment from '@/interface/comment';
import { useUserStore } from '@/stores/userStore';

export default {
  components: {
    NThing,
    NButton,
    Trash
  },
  props: {
    comment: {
      type: Object as PropType<Comment>,
      required: true
    }
  },
  setup(props) {
    const userStore = useUserStore();
    const isUserOwnerOfComment = userStore.profile?.id === props.comment.authorId;

    return {
      isUserOwnerOfComment
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
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric'
      });
    },
    deleteComment() {
      this.isDeleting = true;
      axios.delete(`/comment/${this.comment.id}`).then((response) => {
        this.$emit('refreshList');
      });
    }
  }
};
</script>
<style></style>
