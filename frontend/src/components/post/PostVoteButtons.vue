<template>
  <n-button-group>
    <n-button size="small" :quaternary="isUpVoteGhosted" type="success" @click="upvote">
      <template #icon>
        <n-icon>
          <ArrowUp />
        </n-icon>
      </template>
    </n-button>
    <n-button size="small" type="info">{{ reputation }}</n-button>
    <n-button size="small" :quaternary="isDownVoteGhosted" type="error" @click="downvote">
      <template #icon>
        <n-icon>
          <arrow-down />
        </n-icon>
      </template>
    </n-button>
  </n-button-group>
</template>
<script lang="ts">
import { NButton, NButtonGroup, useMessage, NIcon } from 'naive-ui';
import { ArrowUp, ArrowDown } from '@vicons/ionicons5';
import { type PropType } from 'vue';
import axios from 'axios';
import type PostVoteResponse from '@/interface/response/postVoteResponse';

export default {
  components: {
    NButton,
    NButtonGroup,
    NIcon,
    ArrowUp,
    ArrowDown
  },
  props: {
    reputation: {
      type: Number,
      required: true
    },
    userVote: {
      type: Number as PropType<number | undefined>,
      required: true
    },
    postId: {
      type: Number,
      required: true
    }
  },
  emits: {
    'update-reputation': (newValue: number) => true,
    'update-user-vote': (newValue: number) => true
  },
  setup() {
    return {
      message: useMessage(),
      isVoteRequestRunning: false
    };
  },
  computed: {
    isUpVoteGhosted() {
      return this.userVote === -1 || !this.userVote;
    },
    isDownVoteGhosted() {
      return this.userVote === 1 || !this.userVote;
    }
  },
  methods: {
    upvote() {
      const value = this.userVote === 1 ? 0 : 1;
      this.vote(value);
    },
    downvote() {
      const value = this.userVote === -1 ? 0 : -1;
      this.vote(value);
    },
    vote(value: number) {
      if (this.isVoteRequestRunning) return;

      this.isVoteRequestRunning = true;
      this.updateReputation(this.reputation + value);
      axios
        .post<PostVoteResponse>(`/post/${this.postId}/vote/${value}`)
        .then((response) => {
          this.updateReputation(response.data.reputation);
          this.updateUserVote(value);
        })
        .catch((error) => {
          this.message.error('Ocorreu um erro durante o voto');
          console.error({ error });
        })
        .finally(() => {
          this.isVoteRequestRunning = false;
        });
    },
    updateReputation(newValue: number) {
      this.$emit('update-reputation', newValue);
    },
    updateUserVote(newValue: number) {
      this.$emit('update-user-vote', newValue);
    }
  }
};
</script>
<style></style>
