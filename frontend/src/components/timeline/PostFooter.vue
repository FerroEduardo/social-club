<template>
  <p class="mt-3 line-clamp-3 text-sm leading-6 text-gray-600">
    {{ description }}
  </p>
  <div class="flex">
    <div class="flex-[50%] mt-5 flex items-center gap-x-4">
      <img :src="authorImageUrl" :alt="authorName" class="h-10 w-10 rounded-full bg-gray-50">
      <div class="text-sm leading-6">
        <p class="font-semibold text-gray-900">
          <a href="#">
            {{ authorName }}
          </a>
        </p>
      </div>
    </div>
    <div class="flex-[50%] flex">
      <div class="ml-auto flex text-right mt-5 gap-x-4">
        <img :src="authorImageUrl" :alt="gameName" class="h-10 w-10 rounded-full bg-gray-50">
        <div class="text-sm leading-6">
          <p class="font-semibold text-gray-900">
            <a href="#">
              {{ gameName }}
            </a>
          </p>
          <p class="text-gray-600">
            {{ gameStudio }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script>
export default {
  props: {
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