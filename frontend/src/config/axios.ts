import axios from 'axios';
import router from '@/router';

const API_URL = import.meta.env.VITE_API_URL;

axios.defaults.baseURL = API_URL;
axios.defaults.timeout = 15_000; // 5000 ms = 5 s
axios.defaults.withCredentials = true;

axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      if (error.response.status === 429) {
        window.alert('Você está indo rápido de mais');
      }
      if (
        !ignoreRoute(error.response.request.responseURL as string) &&
        error.response.status === 401
      ) {
        router.push({ name: 'login', query: { reason: 'unauthenticated' } });
      }
    } else if (error.request) {
      console.log(error.request);
    } else {
      console.error('Error', error.message);
    }

    return Promise.reject(error);
  }
);

function ignoreRoute(responseURL: string): boolean {
  return responseURL.endsWith('/ping');
}
