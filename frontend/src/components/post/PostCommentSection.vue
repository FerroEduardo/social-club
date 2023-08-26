<template>
  <n-card title="Comentários">
    <n-list v-if="comments.length > 0">
      <n-list-item v-for="comment in comments" :key="comment.id">
        <PostComment :comment="comment" />
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
<script lang="ts">
import { NCard, NList, NListItem, NEmpty, NButton, NInputGroup, NInput } from 'naive-ui';
import { type PropType } from 'vue';
import axios from 'axios';
import type Comment from '@/interface/comment';
import type IndexCommentResponse from '@/interface/response/indexCommentResponse';
import PostComment from './PostComment.vue';

export default {
  components: {
    NCard,
    NList,
    NListItem,
    NEmpty,
    NButton,
    NInputGroup,
    NInput,
    PostComment
  },
  props: {
    postId: {
      type: Number as PropType<Number>,
      required: true
    }
  },
  data() {
    return {
      commentInput: '' as string,
      comments: [] as Comment[]
    };
  },
  methods: {
    sendComment() {
      axios
        .post<IndexCommentResponse>(`/post/${this.postId}/comment`, {
          value: this.commentInput
        })
        .then((response) => {
          this.commentInput = '';
          this.indexComments();
        })
        .catch((error) => {
          // display index failed
        });
    },
    indexComments() {
      // TODO infinite scroll
      axios
        .get<IndexCommentResponse>(`/post/${this.postId}/comment?page=0&size=100`)
        .then((response) => {
          this.comments = response.data.content;
        })
        .catch((error) => {
          // display index failed
        });
    }
  },
  mounted() {
    this.indexComments();
  }
};
</script>
<style></style>
