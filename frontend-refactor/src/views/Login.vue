<template>
  <div style="display: flex; align-items: center; justify-content: center">
    <n-card
      style="max-width: 600px"
      content-style="margin:auto; max-width: 300px; display: flex; flex-direction: column; gap: 10px"
    >
      <n-button icon-placement="left" @click="loginOauth('google')">
        <template #icon>
          <n-icon>
            <svg
              class="w-4 h-4 mr-2 inline"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              fill="currentColor"
              viewBox="0 0 18 19"
            >
              <path
                fill-rule="evenodd"
                d="M8.842 18.083a8.8 8.8 0 0 1-8.65-8.948 8.841 8.841 0 0 1 8.8-8.652h.153a8.464 8.464 0 0 1 5.7 2.257l-2.193 2.038A5.27 5.27 0 0 0 9.09 3.4a5.882 5.882 0 0 0-.2 11.76h.124a5.091 5.091 0 0 0 5.248-4.057L14.3 11H9V8h8.34c.066.543.095 1.09.088 1.636-.086 5.053-3.463 8.449-8.4 8.449l-.186-.002Z"
                clip-rule="evenodd"
              />
            </svg>
          </n-icon>
        </template>
        Fazer login com Google
      </n-button>
      <n-button icon-placement="left" @click="loginOauth('github')">
        <template #icon>
          <n-icon>
            <svg
              class="w-4 h-4 mr-2 inline"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path
                fill-rule="evenodd"
                d="M10 .333A9.911 9.911 0 0 0 6.866 19.65c.5.092.678-.215.678-.477 0-.237-.01-1.017-.014-1.845-2.757.6-3.338-1.169-3.338-1.169a2.627 2.627 0 0 0-1.1-1.451c-.9-.615.07-.6.07-.6a2.084 2.084 0 0 1 1.518 1.021 2.11 2.11 0 0 0 2.884.823c.044-.503.268-.973.63-1.325-2.2-.25-4.516-1.1-4.516-4.9A3.832 3.832 0 0 1 4.7 7.068a3.56 3.56 0 0 1 .095-2.623s.832-.266 2.726 1.016a9.409 9.409 0 0 1 4.962 0c1.89-1.282 2.717-1.016 2.717-1.016.366.83.402 1.768.1 2.623a3.827 3.827 0 0 1 1.02 2.659c0 3.807-2.319 4.644-4.525 4.889a2.366 2.366 0 0 1 .673 1.834c0 1.326-.012 2.394-.012 2.72 0 .263.18.572.681.475A9.911 9.911 0 0 0 10 .333Z"
                clip-rule="evenodd"
              />
            </svg>
          </n-icon>
        </template>
        Fazer login com GitHub
      </n-button>
    </n-card>
  </div>
</template>

<script lang="ts">
import { NCard, NButton, NIcon } from 'naive-ui'
const API_URL = import.meta.env.VITE_API_URL

export default {
  components: {
    NCard,
    NButton,
    NIcon
  },
  methods: {
    loginOauth(platform: string) {
      window.location.href = `${API_URL}/oauth2/authorize/${platform}`
    }
  }
}
</script>

<style lang=""></style>
