<template>
  <n-popover trigger="click" placement="bottom" display-directive="show">
    <template #trigger>
      <n-button strong secondary circle size="small" style="margin-left: 4px">
        <template #icon>
          <Cog />
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
  <n-modal v-model:show="showEditModal">
    <n-card
      style="width: 600px"
      title="Modal"
      :bordered="false"
      size="huge"
      role="dialog"
      aria-modal="true"
    >
      <template #header-extra> Oops! </template>
      Content
      <template #footer> Footer </template>
    </n-card>
  </n-modal>
</template>
<script lang="ts">
import {
  useMessage,
  NButton,
  NIcon,
  type MenuOption,
  NPopover,
  NMenu,
  useDialog,
  NModal,
  NCard
} from 'naive-ui';
import { Trash, Pencil, Cog } from '@vicons/ionicons5';
import { type PropType, ref, h, type Component } from 'vue';
import { useUserStore } from '@/stores/userStore';

function renderIcon(icon: Component) {
  return () => h(NIcon, null, { default: () => h(icon) });
}

export default {
  components: {
    NButton,
    NPopover,
    NMenu,
    Cog,
    NModal,
    NCard
  },
  props: {
    postId: {
      type: Number as PropType<number>,
      required: true
    }
  },
  setup(props) {
    const userStore = useUserStore();
    const isUserOwnerOfPost = userStore.profile?.id === props.postId;

    return {
      isUserOwnerOfPost,
      message: useMessage(),
      dialog: useDialog(),
      isDeleting: false,
      activeKey: ref<string | null>(null),
      menuOptions: [
        {
          label: 'Apagar',
          key: 'delete',
          icon: renderIcon(Trash)
        },
        {
          label: 'Editar',
          key: 'edit',
          icon: renderIcon(Pencil)
        }
      ] as MenuOption[],
      showEditModal: ref<boolean>(false)
    };
  },
  methods: {
    deletePost() {
      window.alert('delete');
    },
    onValueUpdated(key: string) {
      if (key === 'delete') {
        this.handleDeletePost();
      } else if (key === 'edit') {
        this.showEditModal = true;
      }
    },
    handleDeletePost() {
      this.dialog.error({
        title: 'Apagar postagem',
        content: 'Você tem certeza disso?',
        positiveText: 'Sim',
        negativeText: 'Não tenho certeza',
        onPositiveClick: () => {
          this.message.success('Sim');
        },
        onNegativeClick: () => {
          this.message.error('Não tenho certeza');
        }
      });
    }
  }
};
</script>
<style lang=""></style>
