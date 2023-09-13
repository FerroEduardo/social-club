import './assets/main.css';
import 'vfonts/Roboto.css';

import { createApp } from 'vue';
import { createPinia } from 'pinia';

import App from './App.vue';
import router from './router';
import './config/axios';

const app = createApp(App);

app.use(createPinia()); // always first
app.use(router);

app.mount('#app');
