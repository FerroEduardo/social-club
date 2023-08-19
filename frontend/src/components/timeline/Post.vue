<template>
  <div class="flex w-full flex-col justify-between border-t mt-2 pt-2">
    <div class="flex flex-row gap-x-1">
      <span class="max-h-[24px] max-w-[24px] rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10">
        {{ reputation }}
      </span>
      <div class="w-full">
        <img :src="imageUrl" class="w-full" />
        <Footer 
          :description="description"
          :authorImageUrl="authorImageUrl"
          :authorName="authorName"
          :gameImageUrl="gameImageUrl"
          :gameName="gameName"
          :gameStudio="gameStudio"
        />
      </div>
    </div>
  </div>
</template>

<script>
import Footer from './PostFooter.vue';

export default {
  components: {
    Footer
  },
  props: {
    reputation: Number,
    imageUrl: String,
    description: String,
    authorImageUrl: String,
    authorName: String,
    gameImageUrl: String,
    gameName: String,
    gameStudio: String
  },
}
</script>

<style scoped>
</style>