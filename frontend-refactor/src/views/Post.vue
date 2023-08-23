<template>
  <div style="display: flex; align-items: center; justify-content: center">
    <n-card style="max-width: 800px; margin-top: 20px">
      <n-form ref="formRef" label-placement="top" :rules="rules" :model="model">
        <n-grid :x-gap="24" :cols="1">
          <n-form-item-gi label="Título" path="title" required>
            <n-input v-model:value="model.title" placeholder="Título" />
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
            />
          </n-form-item-gi>
          <n-form-item-gi label="Imagem" path="image" required>
            <n-upload
              directory-dnd
              :default-upload="false"
              @change="handleChange"
              :multiple="false"
              :max="1"
              :file-list="fileList"
              accept=".png,.jpg,.jpeg,.webp"
            >
              <n-upload-dragger>
                <n-text style="font-size: 16px">
                  Clique ou arraste nessa área para fazer o upload
                </n-text>
                <n-p depth="3" style="margin: 8px 0 0 0">
                  Formatos aceitos: PNG, JPEG, JPG e WEBP
                </n-p>
              </n-upload-dragger>
            </n-upload>
          </n-form-item-gi>
          <n-form-item-gi v-if="model.image" label="Preview">
            <img :src="imageFileUrl" style="max-width: 100%; max-height: 400px; margin: auto" />
          </n-form-item-gi>
          <n-form-item-gi label="Jogo" path="game" required>
            <n-auto-complete
              v-model:value="model.game"
              :input-props="{
                autocomplete: 'disabled'
              }"
              :options="gameOptions"
              placeholder="Counter Strike: 2"
              @update-value="handleGameInput"
              @select="handleGameSelect"
            />
          </n-form-item-gi>
          <n-grid-item :span="24">
            <div style="display: flex; justify-content: flex-end">
              <n-button round type="primary" @click.prevent="submit"> Enviar </n-button>
            </div>
          </n-grid-item>
        </n-grid>
      </n-form>
    </n-card>
  </div>
</template>
<script lang="ts">
import {
  NCard,
  NForm,
  NGrid,
  NFormItemGi,
  NAutoComplete,
  NP,
  NUpload,
  NUploadDragger,
  NInput,
  NText,
  NGridItem,
  NButton,
  type AutoCompleteOption,
  type UploadFileInfo,
  type FormInst,
  type FormItemRule
} from 'naive-ui';
import { ref, computed, type Ref } from 'vue';
import axios from 'axios';

import type { FetchGame, GamesRequest } from '@/interface/indexGamesRequest';

export default {
  components: {
    NCard,
    NForm,
    NGrid,
    NFormItemGi,
    NAutoComplete,
    NP,
    NUpload,
    NUploadDragger,
    NInput,
    NText,
    NGridItem,
    NButton
  },
  setup() {
    const formRef = ref<FormInst | null>(null);
    const gameInputRef = ref(null) as Ref<string | null>;
    const imageInputRef = ref(null) as Ref<File | null>;
    const gameList = ref([]) as Ref<AutoCompleteOption[]>;
    const selectedGameIdRef = ref(null) as Ref<string | null>;

    return {
      formRef,
      selectedGameId: selectedGameIdRef,
      gameOptions: computed(() => {
        return gameList.value;
      }),
      model: ref({
        title: null,
        description: null,
        game: gameInputRef,
        image: imageInputRef
      }),
      fileList: ref<UploadFileInfo[]>([]),
      imageFileUrl: ref(),
      gameList,
      rules: {
        title: {
          required: true,
          trigger: ['blur', 'input'],
          message: 'Insira um título'
        },
        description: {
          required: true,
          trigger: ['blur', 'input'],
          message: 'Insira uma descrição'
        },
        image: {
          required: true,
          trigger: ['blur', 'change'],
          message: 'Selecione a imagem',
          validator() {
            return imageInputRef.value != null;
          }
        },
        game: {
          required: true,
          trigger: ['blur', 'change'],
          message: 'Selecione o jogo',
          validator(rule: FormItemRule, value: string) {
            const id = selectedGameIdRef.value ?? null;

            const game = gameList.value.filter((game) => {
              return game?.label === value;
            })[0];

            return Boolean(id != null && game);
          }
        }
      }
    };
  },
  methods: {
    handleChange(data: { fileList: UploadFileInfo[] }) {
      if (data.fileList[0]?.file && data.fileList[0].file.type.startsWith('image')) {
        this.model.image = data.fileList[0].file;
        this.imageFileUrl = URL.createObjectURL(this.model.image);
      }
    },
    async fetchGames(name: string): Promise<FetchGame[]> {
      try {
        const response = await axios.get<GamesRequest>(`/game?page=0&size=10&name=${name}`);
        return response.data.content;
      } catch (error) {
        console.error({ error });
        // display alert
        return [];
      }
    },
    async handleGameInput(text: string) {
      if (text.trim().length !== 0) {
        this.gameList = [];

        const games = await this.fetchGames(text.trim());

        this.gameList = games.map((game) => {
          return {
            label: `${game.name}`,
            value: game.id.toString()
          } as AutoCompleteOption;
        });
      }
    },
    handleGameSelect(id: string) {
      this.selectedGameId = id;
    },
    async validateForm() {
      if (this.formRef) {
        this.formRef.validate((errors) => {
          if (!errors) {
            console.log('Valid');
          } else {
            console.log('Invalid', errors);
          }
        });
      }
    },
    async submit() {
      await this.validateForm();
      console.log({ model: this.model });

      const form = new FormData();
      form.append('image', this.model.image!);
      form.append('gameId', this.selectedGameId!);
      form.append('description', this.model.description!);
      console.log({ form });

      axios
        .post('/post', form)
        .then((response) => {
          if (response.status === 200) {
            // redirect to post
          }
          console.log({ response });
        })
        .catch((reason) => {
          console.log({ reason });
        });
    }
  }
};
</script>
<style lang=""></style>
