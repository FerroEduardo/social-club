<template>
  <n-card
    style="width: 600px"
    title="Editar postagem"
    :bordered="false"
    size="huge"
    role="dialog"
    aria-modal="true"
  >
    <n-form ref="formRef" label-placement="top" :rules="rules" :model="model">
      <n-grid :x-gap="24" :cols="1">
        <n-form-item-gi label="Título" path="title" required>
          <n-input v-model:value="model.title" placeholder="Título" :maxlength="100" />
        </n-form-item-gi>
        <n-form-item-gi label="Descrição" path="description" required>
          <n-input
            v-model:value="model.description"
            placeholder="Descrição"
            type="textarea"
            :autosize="{
              minRows: 1,
              maxRows: 5
            }"
            :maxlength="200"
          />
        </n-form-item-gi>
        <n-grid-item :span="24">
          <div style="display: flex; justify-content: flex-end">
            <n-button
              round
              type="primary"
              @click.prevent="submit"
              :disabled="isRequestRunning"
              :loading="isRequestRunning"
            >
              Enviar
            </n-button>
          </div>
        </n-grid-item>
      </n-grid>
    </n-form>
  </n-card>
</template>
<script lang="ts">
import {
  NCard,
  NForm,
  NGrid,
  NFormItemGi,
  NInput,
  NGridItem,
  NButton,
  type FormInst,
  useMessage
} from 'naive-ui';
import { ref, type PropType } from 'vue';
import axios from 'axios';
import type ShowPostResponse from '@/interface/response/showPostResponse';

export default {
  components: {
    NCard,
    NForm,
    NGrid,
    NFormItemGi,
    NInput,
    NGridItem,
    NButton
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
  setup() {
    const formRef = ref<FormInst | null>(null);

    return {
      formRef,
      model: ref({
        title: null as string | null,
        description: null as string | null
      }),
      rules: {
        title: {
          // required: true,
          trigger: ['blur', 'input'],
          message: 'Insira um título'
        },
        description: {
          // required: true,
          trigger: ['blur', 'input'],
          message: 'Insira uma descrição'
        }
      },
      isRequestRunning: ref(false),
      message: useMessage()
    };
  },
  methods: {
    async validateForm() {
      if (this.formRef) {
        await this.formRef.validate((errors) => {
          if (errors) {
            Promise.reject();
          }
        });
      }
    },
    async submit() {
      try {
        await this.validateForm();
      } catch (error) {
        return;
      }

      this.isRequestRunning = true;

      axios
        .put<ShowPostResponse>(`/post/${this.postId}`, {
          title: this.model.title,
          description: this.model.description
        })
        .then((response) => {
          this.message.success('Mensagem editada com sucesso');
          this.$emit('update-post');
        })
        .catch((error) => {
          this.message.error('Ocorreu um erro na edição da postagem');
          console.error({ error });
        })
        .finally(() => {
          this.isRequestRunning = false;
        });
    }
  }
};
</script>
<style lang=""></style>
