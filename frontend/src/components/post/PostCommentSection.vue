<template>
  <n-card title="Comentários">
    <n-list v-if="comments.length > 0">
      <n-list-item v-for="comment in comments" :key="comment.id">
        <PostComment :comment="comment" @refreshList="fetchComments" />
      </n-list-item>
    </n-list>
    <n-empty v-else description="Seja o primeiro a comentar" />
    <template #header-extra>
      <n-pagination
        v-model:page="page"
        :page-count="pageCount"
        :onUpdatePage="fetchComments"
        simple
      />
    </template>
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
        />
        <n-button type="primary" ghost @click="sendComment" style="height: auto">
          Comentar
        </n-button>
      </n-input-group>
    </template>
  </n-card>
</template>
<script lang="ts">
import {
  NCard,
  NList,
  NListItem,
  NEmpty,
  NButton,
  NInputGroup,
  NInput,
  NPagination
} from 'naive-ui';
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
    PostComment,
    NPagination
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
      page: 1,
      pageSize: 5,
      pageCount: 0,
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
          this.fetchComments();
        })
        .catch((error) => {
          // display index failed
        });
    },
    fetchComments() {
      console.log('fetchComments');

      const page = this.page - 1; // component starts with page 1
      axios
        .get<IndexCommentResponse>(
          `/post/${this.postId}/comment?page=${page}&size=${this.pageSize}`
        )
        .then((response) => {
          this.comments = response.data.content;
          this.pageCount = response.data.totalPages;
        })
        .catch((error) => {
          // display index failed
        });
    }
  },
  mounted() {
    this.fetchComments();
  }
};
</script>
<style></style>
