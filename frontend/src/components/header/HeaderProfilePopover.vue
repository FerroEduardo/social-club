<template>
  <n-popover trigger="click" placement="bottom" display-directive="show">
    <template #trigger>
      <n-button strong secondary circle>
        <template #icon>
          <img
            :src="userStore.profile?.miniAvatarUrl"
            width="32"
            height="32"
            style="border-radius: 50%"
          />
        </template>
      </n-button>
    </template>
    <n-menu
      id="menu"
      v-model:value="activeKey"
      mode="vertical"
      :options="menuOptions"
      :on-update:value="onValueUpdated"
    />
  </n-popover>
</template>
<script lang="ts">
import { PersonOutline as PersonIcon, LogOutOutline as LogOutOutIcon } from '@vicons/ionicons5';
import { NButton, NPopover, NMenu, NIcon, type MenuOption } from 'naive-ui';
import { ref, h, type Component } from 'vue';
import { useUserStore } from '@/stores/userStore';

const API_URL = import.meta.env.VITE_API_URL;

function renderIcon(icon: Component) {
  return () => h(NIcon, null, { default: () => h(icon) });
}

export default {
  components: {
    NButton,
    NPopover,
    NMenu
  },
  setup() {
    return {
      activeKey: ref<string | null>(null),
      menuOptions: [
        {
          label: 'Perfil',
          key: 'profile',
          icon: renderIcon(PersonIcon)
        },
        {
          label: 'Log out',
          key: 'logout',
          icon: renderIcon(LogOutOutIcon)
        }
      ] as MenuOption[],
      userStore: useUserStore()
    };
  },
  methods: {
    onValueUpdated(key: string) {
      if (key === 'logout') {
        this.logout();
      } else if (key === 'profile') {
        this.showProfile();
      }
    },
    logout() {
      window.location.href = `${API_URL}/logout`;
    },
    showProfile() {
      this.$router.push('/profile');
    }
  }
};
</script>
<style>
#menu .n-menu-item-content {
  padding-left: 18px !important;
  padding-right: 18px !important;
}
</style>
