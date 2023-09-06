<template>
  <div style="display: flex; flex-direction: row; gap: 10px">
    <img loading="lazy" :src="game.imageUrl" class="post-image" />
    <div style="display: flex; flex-direction: row; gap: 10px">
      <div style="width: 100%; display: flex; flex-direction: column">
        <span style="font-weight: bold">Nome:</span> {{ game.name }}
        <span style="font-weight: bold">Studio/Publicador/Desenvolvedor:</span>
        {{ game.studio }}
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { type PropType } from 'vue';
import type Game from '@/interface/game';

export default {
  props: {
    game: {
      type: Object as PropType<Game>,
      required: true
    }
  }
};
</script>
<style></style>
