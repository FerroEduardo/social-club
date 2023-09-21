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
      <ProfileData />
      <n-divider />
      <UserPosts />
      <n-divider />
      <UserVotes />
    </n-card>
  </div>
</template>

<script lang="ts">
import { NCard, NDivider } from 'naive-ui';
import ProfileData from '@/components/profile/ProfileData.vue';
import UserPosts from '@/components/profile/UserPosts.vue';
import UserVotes from '@/components/profile/UserVotes.vue';

export default {
  components: {
    NCard,
    NDivider,
    ProfileData,
    UserPosts,
    UserVotes
  }
};
</script>

<style scoped>
.card {
  max-width: 600px;
}
</style>
