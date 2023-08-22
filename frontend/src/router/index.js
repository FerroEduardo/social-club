import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    name: 'homepage',
    path: '/',
    component: () => import('../views/Homepage.vue'),
  },
  {
    name: 'login',
    path: '/login',
    component: () => import('../views/Login.vue'),
  },
  {
    name: 'timeline',
    path: '/timeline',
    component: () => import('../views/Timeline.vue'),
  },
  {
    name: 'create-post',
    path: '/post',
    component: () => import('../views/CreatePost.vue'),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
