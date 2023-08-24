<template>
  <n-thing :title="post.title" :description="post.description">
    <template #header-extra>
      <n-button-group>
        <n-button size="small" type="success"> ⬆️ </n-button>
        <n-button size="small" type="info">
          {{ post.reputation }}
        </n-button>
        <n-button size="small" type="error"> ⬇️ </n-button>
      </n-button-group>
    </template>
    <img
      loading="lazy"
      :src="post.imageUrl"
      class="post-image"
      @click="handlePostImageClick"
      :style="{ cursor: enableShowPostLink ? 'pointer' : 'auto' }"
    />
    <div style="display: flex; flex-direction: row">
      <n-popover trigger="hover" raw :show-arrow="false">
        <template #trigger>
          <div style="margin-right: auto">
            {{ post.author.name }}
          </div>
        </template>
        <div>
          <img loading="lazy" :src="post.author.imageUrl" />
        </div>
      </n-popover>
      <n-popover trigger="hover" raw :show-arrow="false">
        <template #trigger>
          <div style="margin-left: auto">{{ post.game.name }} - {{ post.game.studio }}</div>
        </template>
        <div>
          <img loading="lazy" :src="post.game.imageUrl" />
        </div>
      </n-popover>
    </div>
  </n-thing>
</template>
<script lang="ts">
import { NThing, NButton, NButtonGroup, NPopover } from 'naive-ui';
import { defineComponent, type PropType } from 'vue';
import type Post from '@/interface/post';

export default defineComponent({
  components: {
    NThing,
    NButton,
    NButtonGroup,
    NPopover
  },
  props: {
    post: {
      type: Object as PropType<Post>,
      required: true
    },
    enableShowPostLink: {
      type: Boolean,
      required: false,
      default: true
    }
  },
  methods: {
    handlePostImageClick() {
      if (this.enableShowPostLink) {
        this.$router.push(`/post/${this.post.id}`);
      }
    }
  }
});
</script>
<style scoped>
.post-image {
  width: 100%;
  max-width: 800px;
}
</style>
