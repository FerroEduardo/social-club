<template>
  <n-popover trigger="click" placement="bottom" display-directive="show">
    <template #trigger>
      <n-button strong secondary circle>
        <template #icon>
          <PersonIcon />
        </template>
      </n-button>
    </template>
    <!-- <n-button type="default" @click="logout"> Log out </n-button> -->
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
const API_URL = import.meta.env.VITE_API_URL;

function renderIcon(icon: Component) {
  return () => h(NIcon, null, { default: () => h(icon) });
}

export default {
  components: {
    NButton,
    NPopover,
    NMenu,
    PersonIcon
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
      ] as MenuOption[]
    };
  },
  methods: {
    onValueUpdated(key: string) {
      console.log({ key });
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
