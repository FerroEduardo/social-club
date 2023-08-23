<template>
  <div style="display: flex; align-items: center; justify-content: center">
    <n-card style="max-width: 800px; margin-top: 20px">
      <n-form ref="formRef" label-placement="top">
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
                minRows: 3,
                maxRows: 5
              }"
            />
          </n-form-item-gi>
          <n-form-item-gi label="Imagem" path="inputUpload" required>
            <n-upload
              multiple
              directory-dnd
              action="https://www.mocky.io/v2/5e4bafc63100007100d8b70f"
              :max="5"
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
          <n-form-item-gi label="Jogo" path="inputGame" required>
            <n-auto-complete
              v-model:value="gameInput"
              :input-props="{
                autocomplete: 'disabled'
              }"
              :options="gameOptions"
              placeholder="Counter Strike: 2"
            />
          </n-form-item-gi>
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
  NText
} from 'naive-ui';
import { ref, computed } from 'vue';

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
    NText
  },
  setup() {
    const gameInputRef = ref('');
    return {
      gameInput: gameInputRef,
      gameOptions: computed(() => {
        return [gameInputRef.value, gameInputRef.value + '1', gameInputRef.value + '2'];
      }),
      model: ref({
        title: null,
        description: null,
        selectValue: null,
        multipleSelectValue: null,
        datetimeValue: null,
        nestedValue: {
          path1: null,
          path2: null
        },
        switchValue: false,
        checkboxGroupValue: null,
        radioGroupValue: null,
        radioButtonGroupValue: null,
        inputNumberValue: null,
        timePickerValue: null,
        sliderValue: 0,
        transferValue: null
      })
    };
  }
};
</script>
<style lang=""></style>
