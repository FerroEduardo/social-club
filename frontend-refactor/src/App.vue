<template>
  <n-config-provider :theme="theme">
    <n-layout>
      <n-layout-header bordered>
        <Header />
      </n-layout-header>
      <n-layout-content bordered>
        <router-view />
      </n-layout-content>
    </n-layout>
  </n-config-provider>
</template>

<script lang="ts">
import { darkTheme, NConfigProvider, NLayout, NLayoutHeader, NLayoutContent } from 'naive-ui';
import axios from 'axios';

import Header from '@/components/Header.vue';
import { useUserStore } from '@/stores/userStore';

export default {
  components: {
    NConfigProvider,
    Header,
    NLayout,
    NLayoutHeader,
    NLayoutContent
  },
  setup() {
    const theme = darkTheme;
    document.querySelector('body').style.backgroundColor = theme.common.bodyColor;

    return {
      theme,
      userStore: useUserStore()
    };
  },
  methods: {
    handleFirstLoad(callback: Function) {
      const isFirstLoad = this.$route.name === undefined;
      if (isFirstLoad) {
        const urlParams = new URLSearchParams(window.location.search);
        const login = urlParams.get('login');
        const reason = urlParams.get('reason');

        if (login === 'failed') {
          this.$router.replace(`/login?reason=${reason}`);
          return;
        }
      }
      callback();
    },
    checkIsAuthenticated() {
      axios
        .get('/ping')
        .then((response) => {
          if (response.status === 200) {
            this.userStore.setAuthenticated(true);
            console.log('authenticated');
          } else {
            console.error('failed to check if user is authenticated', { response });
          }
        })
        .catch((reason) => {
          if (reason?.response?.status === 401) {
            this.userStore.setAuthenticated(false);
            console.log('unauthenticated');
          } else {
            console.error('failed to check if user is authenticated', { reason });
          }
        });
    }
  },
  beforeMount() {
    this.handleFirstLoad(this.checkIsAuthenticated);
  }
};
</script>

<style scoped></style>
