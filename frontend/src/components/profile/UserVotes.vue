<template>
  <h1>Votos</h1>
  <n-list hoverable clickable bordered>
    <n-list-item v-if="posts.length === 0 && !isLoadingData">
      <n-empty description="Nenhum post encontrado" />
    </n-list-item>
    <n-list-item v-for="post in posts" :key="post.id" @click="openPost(post.id)">
      <n-thing>
        <template #avatar>
          <n-tag :bordered="false" type="info">
            {{ post.reputation }}
          </n-tag>
        </template>
        <template #header>
          {{ post.title ?? '(Sem título)' }}
        </template>
        <template #header-extra>
          {{ parseTimestamp(post.createdAt) }}
        </template>
        {{ post.description ?? '(Sem descrição)' }}
      </n-thing>
    </n-list-item>
  </n-list>
  <n-pagination
    v-model:page="page"
    :page-count="pageCount"
    :onUpdatePage="fetchUserPosts"
    simple
    style="margin-top: 10px"
  />
</template>
<script lang="ts">
import { useMessage, NList, NListItem, NPagination, NTag, NThing, NEmpty } from 'naive-ui';
import axios from 'axios';

import type Post from '@/interface/post';
import type IndexUserPostRequest from '@/interface/response/user/indexUserPostResponse';

export default {
  components: {
    NList,
    NListItem,
    NPagination,
    NTag,
    NThing,
    NEmpty
  },
  setup() {
    return {
      message: useMessage()
    };
  },
  data() {
    return {
      posts: [] as Post[],
      page: 1,
      pageSize: 5,
      pageCount: 0,
      isLoadingData: true
    };
  },
  methods: {
    fetchUserPosts() {
      this.isLoadingData = true;
      const page = this.page - 1; // component starts with page 1

      axios
        .get<IndexUserPostRequest>(`/user/vote?page=${page}&size=${this.pageSize}`, {
          withCredentials: true
        })
        .then((response) => {
          this.pageCount = response.data.totalPages;
          this.posts = response.data.content.map((post) => {
            return {
              id: post.id,
              title: post.title,
              description: post.description,
              reputation: post.reputation,
              imageUrl: post.imageUrl,
              game: {
                id: post.gameId,
                name: post.gameName,
                studio: post.gameStudio,
                imageUrl: post.gameImageUrl
              },
              author: {
                id: post.authorId,
                name: post.authorName,
                avatarUrl: post.authorAvatarUrl,
                miniAvatarUrl: post.authorMiniAvatarUrl
              },
              userVote: post.userVote,
              createdAt: new Date(post.createdAt),
              modifiedAt: new Date(post.modifiedAt)
            };
          });
        })
        .catch((error) => {
          this.message.error('Ocorreu um erro ao buscar as postagens');
          console.error({ error });
        })
        .finally(() => {
          this.isLoadingData = false;
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
    openPost(postId: number) {
      this.$router.push(`/post/${postId}`);
    }
  },
  mounted() {
    this.fetchUserPosts();
  }
};
</script>
<style></style>
