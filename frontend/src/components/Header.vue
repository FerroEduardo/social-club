<template>
  <header id="header-container">
    <div id="header-left">
      <img src="https://avatars.githubusercontent.com/u/92893192" style="max-height: 30px" />
    </div>
    <nav id="nav-container">
      <router-link
        v-for="link in links"
        :key="link.label"
        :to="link.to"
        #="{ navigate, href }"
        custom
      >
        <n-a class="no-decoration" :href="href" @click="navigate">
          {{ link.label }}
        </n-a>
      </router-link>
    </nav>
    <div id="header-right">
      <router-link v-if="!userStore.isAuthenticated" to="/login">
        <n-button type="primary"> Fazer login </n-button>
      </router-link>
      <n-button v-else type="default" @click="logout"> Log out </n-button>
    </div>
  </header>
</template>
<script lang="ts">
import { NButton, NA } from 'naive-ui';

import { useUserStore } from '@/stores/userStore';
const API_URL = import.meta.env.VITE_API_URL;

export default {
  components: {
    NButton,
    NA
  },
  setup() {
    return {
      userStore: useUserStore(),
      links: [
        {
          label: 'Home',
          to: '/'
        },
        {
          label: 'Timeline',
          to: '/timeline'
        },
        {
          label: 'Postar',
          to: '/post'
        }
      ]
    };
  },
  methods: {
    logout() {
      window.location.href = `${API_URL}/logout`;
    }
  }
};
</script>
<style scoped>
#header-container {
  padding: 10px 20px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
}

#nav-container {
  display: flex;
  flex-direction: row;
  gap: 20px;
}

#header-left {
  margin-right: auto;
}

#header-right {
  margin-left: auto;
}
</style>
