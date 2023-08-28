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
    <n-card style="max-width: 600px">
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
    </n-card>
  </div>
</template>

<script lang="ts">
import { NCard, useMessage, NSkeleton } from 'naive-ui';
import axios from 'axios';

import type UserProfileResponse from '@/interface/response/user/userProfileResponse';
import type UserProfile from '@/interface/userProfile';

export default {
  components: { NCard, NSkeleton },
  setup() {
    return {
      message: useMessage()
    };
  },
  data() {
    return {
      profile: null as UserProfile | null
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
    }
  },
  mounted() {
    this.fetchProfile();
  }
};
</script>

<style scoped>
#avatar {
  max-width: 164px;
  width: 100%;
  flex-basis: auto;
}
</style>
