<template>
  <div id="container">
    <div id="background" ref="background"></div>
    <div id="content">
      <h1 id="title">Social Club</h1>
      <h2 id="subtitle">Conecte-se, Compartilhe, Viva o Jogo</h2>
      <p id="description">
        Uma plataforma de mídia social voltada para apaixonados por jogos, oferecendo uma
        experiência de compartilhamento de <i>screenshots</i> e artes.
      </p>
      <div id="button-container">
        <router-link to="/timeline">
          <n-button type="primary" size="large">Timeline</n-button>
        </router-link>
        <router-link to="/login">
          <n-button type="info" size="large">Fazer Login</n-button>
        </router-link>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { NButton } from 'naive-ui';

export default {
  components: {
    NButton
  },
  data() {
    return {
      imageIndex: 1,
      imageCount: 5,
      backgroundImage: `url(/landing/screenshot_1.webp)`,
      interval: undefined as number | undefined
    };
  },
  methods: {
    setupImages() {
      for (let i = 1; i <= this.imageCount; i++) {
        const link = document.createElement('img');
        link.src = `/landing/screenshot_${i}.webp`;
        if (i == 1) {
          link.loading = 'eager';
        }
      }

      this.interval = setInterval(() => {
        this.imageIndex = (this.imageIndex % this.imageCount) + 1;
        this.backgroundImage = `url(/landing/screenshot_${this.imageIndex}.webp)`;
      }, 5000);
    }
  },
  mounted() {
    this.setupImages();
  },
  unmounted() {
    if (this.interval) {
      clearInterval(this.interval);
    }
  }
};
</script>
<style scoped>
#container {
  height: calc(100vh - var(--header-height));
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}
#container::before {
  content: '';
  background-image: v-bind(backgroundImage);
  background-size: cover;
  background-position: center center;
  background-attachment: fixed;
  filter: blur(10px);
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
#content {
  padding: 10px;
  display: flex;
  flex-direction: column;
  text-align: center;
  max-width: 600px;
  z-index: 1;
  text-shadow: 2px 1px black;
}
#title {
  margin: 0;
  font-size: 4rem;
}
#subtitle {
  margin: 0;
  font-size: 1.5rem;
}
#description {
  font-size: 1rem;
}
#button-container {
  display: flex;
  flex-direction: row;
  margin: auto;
  gap: 10px;
}
</style>
