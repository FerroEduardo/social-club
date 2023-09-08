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
    <PostEditCard :post-id="postId" @update-post="onPostUpdated" />
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
  NModal
} from 'naive-ui';
import { Trash, Pencil, Cog } from '@vicons/ionicons5';
import { type PropType, ref, h, type Component } from 'vue';
import axios from 'axios';
import { useUserStore } from '@/stores/userStore';
import PostEditCard from './PostEditCard.vue';

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
    PostEditCard
  },
  props: {
    postId: {
      type: Number as PropType<number>,
      required: true
    }
  },
  emits: {
    'update-post': () => true
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
      axios
        .delete(`/post/${this.postId}`)
        .then((response) => {
          this.message.success('Postagem apagada com sucesso');
          this.$router.push('/timeline');
        })
        .catch((error) => {
          this.message.error('Ocorreu um erro ao tentar apagar a postagem');
        });
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
          this.deletePost();
        }
      });
    },
    onPostUpdated() {
      this.showEditModal = false;
      this.$emit('update-post');
    }
  }
};
</script>
<style lang=""></style>
