<template>
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
        <span style="font-weight: bold">Nome:</span> <n-skeleton text class="skeleton" />
        <span style="font-weight: bold">Usuário:</span> <n-skeleton text class="skeleton" />
        <span style="font-weight: bold">E-mail:</span> <n-skeleton text class="skeleton" />
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { useMessage, NSkeleton } from 'naive-ui';
import axios from 'axios';

import type UserProfile from '@/interface/userProfile';
import type UserProfileResponse from '@/interface/response/user/userProfileResponse';

export default {
  components: { NSkeleton },
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
          console.error({ error });
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
.skeleton {
  width: 60%;
}
</style>
