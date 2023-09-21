<template>
  <div v-if="isReady" style="display: flex; flex-direction: row; gap: 10px">
    <img id="avatar" fetchpriority="high" :src="profile!.avatarUrl" />
    <div style="width: 100%; display: flex; flex-direction: column">
      <!-- Name -->
      <span style="font-weight: bold">Nome:</span> {{ profile!.name }}
      <!-- Username -->
      <span style="font-weight: bold">Usuário:</span> {{ profile!.username }}
      <!-- Name -->
      <span style="font-weight: bold">E-mail:</span> {{ profile!.email }}
      <!-- Reputation -->
      <span style="font-weight: bold">Reputação:</span>
      <n-number-animation show-separator :to="reputation!" :duration="1000" />
    </div>
  </div>
  <div v-else>
    <div style="display: flex; flex-direction: row; gap: 10px">
      <n-skeleton height="164px" width="100%" style="max-width: 164px" />
      <div style="width: 100%; display: flex; flex-direction: column">
        <span style="font-weight: bold">Nome:</span> <n-skeleton text class="skeleton" />
        <span style="font-weight: bold">Usuário:</span> <n-skeleton text class="skeleton" />
        <span style="font-weight: bold">E-mail:</span> <n-skeleton text class="skeleton" />
        <span style="font-weight: bold">Reputação:</span> <n-skeleton text class="skeleton" />
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { useMessage, NSkeleton, NNumberAnimation } from 'naive-ui';
import axios from 'axios';
import { ref, type Ref } from 'vue';

import type UserProfile from '@/interface/userProfile';
import type UserProfileResponse from '@/interface/response/user/userProfileResponse';

export default {
  components: {
    NSkeleton,
    NNumberAnimation
  },
  setup() {
    return {
      message: useMessage(),
      profile: ref(null) as Ref<UserProfile | null>,
      reputation: ref(null) as Ref<number | null>
    };
  },
  computed: {
    isReady() {
      return this.profile != null && this.reputation != null;
    }
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
            avatarUrl: response.data.avatarUrl,
            miniAvatarUrl: response.data.miniAvatarUrl
          };
        })
        .catch((error) => {
          this.message.error('Erro ao buscar dados do perfil');
          console.error({ error });
        });
    },
    fetchReputation() {
      axios
        .get('/user/reputation')
        .then((response) => {
          this.reputation = response.data.reputation;
        })
        .catch((error) => {
          this.message.error('Erro ao buscar dados do perfil');
          console.error({ error });
        });
    }
  },
  mounted() {
    this.fetchProfile();
    this.fetchReputation();
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
