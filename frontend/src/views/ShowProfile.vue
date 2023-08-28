<template>
  <div
    style="
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      margin-top: 10px;
      padding: 10px 5px;
    "
  >
    <n-card class="card">
      <div v-if="profile" style="display: flex; flex-direction: row; gap: 10px">
        <img id="avatar" fetchpriority="high" :src="profile.imageUrl" />
        <div style="width: 100%; display: flex; flex-direction: column">
          <span style="font-weight: bold">Nome:</span> {{ profile.name }}
          <span style="font-weight: bold">Usuário:</span> {{ profile.username }}
          <span style="font-weight: bold">E-mail:</span> {{ profile.email }}
        </div>
      </div>
      <div v-else>
        <div style="display: flex; flex-direction: row; gap: 10px">
          <n-skeleton height="164px" width="100%" style="max-width: 164px" />
          <div style="width: 100%; display: flex; flex-direction: column">
            <span style="font-weight: bold">Nome:</span> <n-skeleton text style="width: 60%" />
            <span style="font-weight: bold">Usuário:</span> <n-skeleton text style="width: 60%" />
            <span style="font-weight: bold">E-mail:</span> <n-skeleton text style="width: 60%" />
          </div>
        </div>
      </div>
      <n-divider />
      <h1>Postagens</h1>
      <n-list hoverable clickable>
        <n-list-item v-for="post in posts" :key="post.id">
          {{ post.description }}
        </n-list-item>
        <n-pagination
          v-model:page="page"
          :page-count="pageCount"
          :onUpdatePage="fetchUserPosts"
          simple
        />
      </n-list>
    </n-card>
  </div>
</template>

<script lang="ts">
import { NCard, useMessage, NSkeleton, NDivider, NList, NListItem, NPagination } from 'naive-ui';
import axios from 'axios';

import type Post from '@/interface/post';
import type UserProfileResponse from '@/interface/response/user/userProfileResponse';
import type IndexUserPostRequest from '@/interface/response/user/indexUserPostResponse';
import type UserProfile from '@/interface/userProfile';

export default {
  components: { NCard, NSkeleton, NDivider, NList, NListItem, NPagination },
  setup() {
    return {
      message: useMessage()
    };
  },
  data() {
    return {
      profile: null as UserProfile | null,
      posts: [] as Post[],
      page: 1,
      pageSize: 5,
      pageCount: 0
    };
  },
  methods: {
    fetchProfile() {
      axios
        .get<UserProfileResponse>('/user/me')
        .then((response) => {
          this.profile = {
            id: response.data.id,
            name: response.data.name,
            username: undefined,
            email: response.data.email,
            imageUrl: response.data.imageUrl
          };
        })
        .catch((error) => {
          this.message.error('Erro ao buscar dados do perfil');
        });
    },
    fetchUserPosts() {
      // eslint-disable-next-line no-plusplus
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
              title: 'Lorem ipsum dolor sit amet',
              description: post.description,
              reputation: post.reputation,
              imageUrl: post.imageUrl,
              game: {
                id: post.gameId,
                name: post.gameName,
                studio: post.gameStudio,
                imageUrl: 'https://cdn.cloudflare.steamstatic.com/steam/apps/730/header.jpg'
              },
              author: {
                id: post.authorId,
                name: post.authorName,
                imageUrl:
                  'https://avatars.cloudflare.steamstatic.com/b69c069ae57724cc0bdbcf4eff87d4bb4feb3def_full.jpg' // post.authorImageUrl
              },
              userVote: post.userVote
            };
          });
        })
        .catch((reason) => {
          // failed
        });
    }
  },
  mounted() {
    this.fetchProfile();
    this.fetchUserPosts();
  }
};
</script>

<style scoped>
#avatar {
  max-width: 164px;
  width: 100%;
  flex-basis: auto;
}
.card {
  max-width: 600px;
}
</style>
