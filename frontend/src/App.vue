<template>
  <PageHeader />
  <router-view></router-view>
</template>

<script>
import axios from 'axios';

import PageHeader from './components/PageHeader.vue';
import { useUserStore } from './store/userStore';

export default {
  components: {
    PageHeader,
  },
  setup() {
    return {
      userStore: useUserStore(),
    };
  },
  methods: {
    handleFirstLoad(callback) {
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
      axios.get('/ping')
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
    },
  },
  beforeMount() {
    this.handleFirstLoad(this.checkIsAuthenticated);
  },
};
</script>

<style scoped>
</style>
