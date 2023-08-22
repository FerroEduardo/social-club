<template>
  <router-view></router-view>
</template>

<script>
import axios from 'axios';

import { useUserStore } from './store/userStore';

export default {
  setup() {
    return {
      userStore: useUserStore(),
    };
  },
  mounted() {
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
};
</script>

<style scoped>
</style>
