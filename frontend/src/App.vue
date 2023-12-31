<template>
  <n-config-provider :theme="theme" :locale="locale">
    <n-dialog-provider>
      <n-message-provider>
        <n-back-top />
        <n-layout>
          <n-layout-header bordered>
            <Header />
          </n-layout-header>
          <n-layout-content bordered>
            <router-view :key="$route.path" />
          </n-layout-content>
          <n-layout-footer bordered inverted>
            <Footer />
          </n-layout-footer>
        </n-layout>
      </n-message-provider>
    </n-dialog-provider>
  </n-config-provider>
</template>

<script lang="ts">
import {
  darkTheme,
  NConfigProvider,
  NLayout,
  NLayoutHeader,
  NLayoutFooter,
  NLayoutContent,
  NMessageProvider,
  NDialogProvider,
  NBackTop,
  ptBR,
  enUS
} from 'naive-ui';
import axios from 'axios';

import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import { useUserStore } from '@/stores/userStore';

export default {
  components: {
    NConfigProvider,
    Header,
    NLayout,
    NLayoutHeader,
    NLayoutContent,
    NMessageProvider,
    NDialogProvider,
    NBackTop,
    NLayoutFooter,
    Footer
  },
  setup() {
    const theme = darkTheme;
    document.querySelector('body')!.style.backgroundColor = theme.common.bodyColor;
    const locale = navigator.language.startsWith('pt') ? ptBR : enUS;

    return {
      locale,
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
        .then(async (response) => {
          if (response.status === 200) {
            const response = await axios.get('/user/me');
            this.userStore.setAuthenticated(true);
            this.userStore.setProfile(
              response.data.id,
              response.data.name,
              response.data.username,
              response.data.avatarUrl,
              response.data.miniAvatarUrl
            );
          } else {
            console.error('failed to check if user is authenticated', { response });
          }
        })
        .catch((reason) => {
          if (reason?.response?.status === 401) {
            this.userStore.setAuthenticated(false);
          } else {
            console.error('failed to check if user is authenticated', { reason });
            window.alert('Ocorreu um erro na verificação de autenticação do usuário');
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
