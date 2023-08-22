<template>
  <div class="mx-auto px-3 pt-3 pb-6 md:px-0 md:max-w-xl">
    <form>
      <div class="space-y-12">
        <div class="border-b border-gray-900/10 pb-12">
          <h2 class="text-base font-semibold leading-7 text-gray-900">
            Criar postagem
          </h2>
          <p class="mt-1 text-sm leading-6 text-gray-600">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore
            magna aliqua.
          </p>

          <div class="mt-10 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
            <div class="col-span-full">
              <label for="cover-photo" class="block text-sm font-medium leading-6 text-gray-900">
                Imagem<span class="text-red-600">*</span>
              </label>
              <div ref="file-upload-container"
                class="mt-2 flex justify-center rounded-lg border border-dashed border-gray-900/25 px-6 py-10"
                :class="[isDraggingFile ? 'bg-gray-200' : '']" @dragover.prevent="onDragOver"
                @dragleave.prevent="onDragLeave" @drop.prevent="onFileDrop">
                <div class="text-center">
                  <svg class="mx-auto h-12 w-12 text-gray-300" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
                    <path fill-rule="evenodd"
                      d="M1.5 6a2.25 2.25 0 012.25-2.25h16.5A2.25 2.25 0 0122.5 6v12a2.25 2.25 0 01-2.25 2.25H3.75A2.25 2.25 0 011.5 18V6zM3 16.06V18c0 .414.336.75.75.75h16.5A.75.75 0 0021 18v-1.94l-2.69-2.689a1.5 1.5 0 00-2.12 0l-.88.879.97.97a.75.75 0 11-1.06 1.06l-5.16-5.159a1.5 1.5 0 00-2.12 0L3 16.061zm10.125-7.81a1.125 1.125 0 112.25 0 1.125 1.125 0 01-2.25 0z"
                      clip-rule="evenodd" />
                  </svg>
                  <div class="mt-4 flex text-sm leading-6 text-gray-600">
                    <label for="file-upload"
                      class="relative cursor-pointer rounded-md font-semibold text-indigo-600 focus-within:outline-none focus-within:ring-2 focus-within:ring-indigo-600 focus-within:ring-offset-2 hover:text-indigo-500">
                      <span>Upload a file</span>
                      <input @change="onFileChange" id="file-upload" ref="file-upload" name="file-upload" type="file"
                        class="sr-only" accept=".jpg,.jpeg,.png,.webp">
                    </label>
                    <p class="pl-1">or drag and drop</p>
                  </div>
                  <p class="text-xs leading-5 text-gray-600">PNG, JPG, JPEG, WEBP até 10MB</p>
                </div>
              </div>
            </div>

            <div class="col-span-full" v-if="previewImageUrl">
              <label for="cover-photo" class="block text-sm font-medium leading-6 text-gray-900">Preview</label>
              <img class="mx-auto border border-stone-200" :src="previewImageUrl" />
            </div>

            <div class="col-span-full">
              <label for="about" class="block text-sm font-medium leading-6 text-gray-900">Descrição</label>
              <div class="mt-2">
                <textarea id="about" name="about" rows="3"
                  class="px-3 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"></textarea>
              </div>
            </div>

            <div class="sm:col-span-3">
              <label for="country" class="block text-sm font-medium leading-6 text-gray-900">Country</label>
              <div class="mt-2">
                <select id="country" name="country" autocomplete="country-name"
                  class="px-2 bg-white block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:max-w-xs sm:text-sm sm:leading-6">
                  <option value="" selected>Escolha um jogo</option>
                  <option value="1">Counter Strike</option>
                </select>
              </div>
            </div>

          </div>
        </div>
      </div>

      <div class="mt-6 flex items-center justify-end gap-x-6">
        <button type="button" class="text-sm font-semibold leading-6 text-gray-900">Cancel</button>
        <button type="button" @click="submit"
          class="rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
          Save
        </button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      isDraggingFile: false,
      previewImageUrl: null,
      file: null,
      description: null,
      gameId: 4,
    };
  },
  methods: {
    onFileDrop(event) {
      const file = event.dataTransfer.files[0] ?? null;
      if (file && file.type.startsWith('image')) {
        this.previewImageUrl = URL.createObjectURL(file);
        this.file = file;
      } else {
        this.previewImageUrl = null;
      }
      this.isDraggingFile = false;
    },
    onDragOver() {
      this.isDraggingFile = true;
    },
    onDragLeave() {
      this.isDraggingFile = false;
    },
    onFileChange(event) {
      const file = event.target.files[0] ?? null;
      if (file && file.type.startsWith('image')) {
        this.previewImageUrl = URL.createObjectURL(file);
        this.file = file;
      } else {
        this.previewImageUrl = null;
      }
    },
    submit() {
      const form = new FormData();
      form.append('image', this.file);
      form.append('gameId', this.gameId);
      form.append('description', this.description);

      axios.post('/post', form)
        .then((response) => {
          if (response.status === 200) {
            // redirect to post
          }
          console.log({ response });
        })
        .catch((reason) => {
          console.log({ reason });
        });
    },
  },
};
</script>

<style></style>
