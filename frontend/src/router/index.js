import { createRouter, createWebHashHistory } from 'vue-router';
import Homepage from '../views/Homepage.vue';
import Login from '../views/Login.vue';
import Timeline from '../views/Timeline.vue';

const routes = [
  {
    name: 'homepage',
    path: '/',
    component: Homepage,
  },
  {
    name: 'login',
    path: '/login',
    component: Login,
  },
  {
    name: 'timeline',
    path: '/timeline',
    component: Timeline,
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
