<template>
  <h1>Postagens</h1>
  <n-list hoverable clickable bordered>
    <n-list-item v-for="post in posts" :key="post.id" @click="openPost(post.id)">
      <n-thing>
        <template #avatar>
          <n-tag :bordered="false" type="info">
            {{ post.reputation }}
          </n-tag>
        </template>
        <template #header>
          {{ post.title }}
        </template>
        <template #header-extra>
          {{ parseTimestamp(post.createdAt) }}
        </template>
        {{ post.description }}
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
import { useMessage, NList, NListItem, NPagination, NTag, NThing } from 'naive-ui';
import axios from 'axios';

import type Post from '@/interface/post';
import type IndexUserPostRequest from '@/interface/response/user/indexUserPostResponse';

export default {
  components: { NList, NListItem, NPagination, NTag, NThing },
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
      pageCount: 0
    };
  },
  methods: {
    fetchUserPosts() {
      const page = this.page - 1; // component starts with page 1

      axios
        .get<IndexUserPostRequest>(`/user/post?page=${page}&size=5`, {
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
                imageUrl: post.authorImageUrl
              },
              author: {
                id: post.authorId,
                name: post.authorName,
                imageUrl:
                  'https://avatars.cloudflare.steamstatic.com/b69c069ae57724cc0bdbcf4eff87d4bb4feb3def_full.jpg' // post.authorImageUrl
              },
              userVote: post.userVote,
              createdAt: new Date(post.createdAt)
            };
          });
        })
        .catch((error) => {
          this.message.error('Ocorreu um erro ao buscar as postagens');
          console.error({ error });
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
