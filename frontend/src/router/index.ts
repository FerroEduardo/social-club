import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '@/stores/userStore';

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
      component: () => import('../views/Timeline.vue'),
      props: true
    },
    {
      path: '/post/:postId',
      name: 'show-post',
      component: () => import('../views/ShowPost.vue'),
      props: true
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('../views/ShowProfile.vue'),
      props: true
    },
    {
      path: '/game',
      name: 'game',
      component: () => import('../views/GameList.vue'),
      props: true
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('../views/NotFound.vue')
    }
  ]
});

router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (userStore.isAuthenticated) {
      next();
      return;
    } else {
      next({ name: 'login', query: { reason: 'unauthenticated' } });
      return;
    }
  }
  next();
});

export default router;
