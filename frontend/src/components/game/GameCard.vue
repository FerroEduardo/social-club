<template>
  <div id="container">
    <img id="cover" loading="lazy" :src="game.imageUrl" />
    <div id="field-container">
      <span class="field-name" style="">Nome:</span> {{ game.name }}
      <span class="field-name">Studio/Publicador/Desenvolvedor:</span>
      {{ game.studio }}
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
<style scoped>
#container {
  display: flex;
  flex-direction: row;
  gap: 10px;
  cursor: pointer;
}
#cover {
  max-width: 120px;
  width: 100%;
}
#field-container {
  display: flex;
  flex-direction: column;
}
.field-name {
  font-weight: bold;
}
</style>
