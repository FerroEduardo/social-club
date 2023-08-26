<template>
  <n-thing
    :title="comment.authorName"
    :title-extra="parseTimestamp(comment.createdAt)"
    content-style="white-space: pre-wrap"
  >
    {{ comment.value }}
  </n-thing>
</template>
<script lang="ts">
import { type PropType } from 'vue';
import type Comment from '@/interface/comment';
import { NThing } from 'naive-ui';

export default {
  components: {
    NThing
  },
  props: {
    comment: {
      type: Object as PropType<Comment>,
      required: true
    }
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
    }
  }
};
</script>
<style></style>
