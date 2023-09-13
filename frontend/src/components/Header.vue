<template>
  <header id="header-container">
    <div id="header-left">
      <img src="/logo.png" width="30" />
    </div>
    <nav id="nav-container">
      <router-link
        v-for="link in links"
        :key="link.label"
        :to="link.to"
        #="{ navigate, href, isActive }"
        custom
      >
        <n-a
          class="no-decoration"
          :style="{ fontWeight: isActive ? 'bold' : 'unset' }"
          :href="href"
          @click="navigate"
        >
          {{ link.label }}
        </n-a>
      </router-link>
    </nav>
    <div id="header-right">
      <HeaderProfilePopover v-if="userStore.isAuthenticated" />
      <router-link v-else to="/login">
        <n-button type="info"> Fazer login </n-button>
      </router-link>
    </div>
  </header>
</template>
<script lang="ts">
import { NButton, NA } from 'naive-ui';

import { useUserStore } from '@/stores/userStore';
import HeaderProfilePopover from '@/components/header/HeaderProfilePopover.vue';

export default {
  components: {
    NButton,
    NA,
    HeaderProfilePopover
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
          label: 'Jogos',
          to: '/game'
        }
      ]
    };
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
  max-height: --header-height;
  height: 100%;
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
