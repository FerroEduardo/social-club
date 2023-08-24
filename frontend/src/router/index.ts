import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/Homepage.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue')
    },
    {
      path: '/timeline',
      name: 'timeline',
      component: () => import('../views/Timeline.vue')
    },
    {
      path: '/post',
      name: 'post',
      component: () => import('../views/CreatePost.vue')
    }
  ]
});

export default router;
