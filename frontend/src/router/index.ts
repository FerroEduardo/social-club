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
      component: () => import('../views/Timeline.vue')
      // meta: { requiresAuth: true }
    },
    {
      path: '/post',
      name: 'post',
      component: () => import('../views/CreatePost.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/post/:postId',
      name: 'show-post',
      component: () => import('../views/ShowPost.vue'),
      // meta: { requiresAuth: true },
      props: true
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
