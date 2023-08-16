<template>
  <div class="bg-white py-24 sm:py-32">
    <div class="mx-auto max-w-7xl px-6 lg:px-8">
      <div class="mx-auto max-w-2xl lg:mx-0">
        <h2 class="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
          Lorem ipsum dolor
        </h2>
        <p class="mt-2 text-lg leading-8 text-gray-600">
          Lorem ipsum dolor sit amet, consectetur adipiscing elit
        </p>
      </div>
      <div class="mx-auto mt-10 max-w-2xl gap-x-8 gap-y-16 border-t border-gray-200 pt-10 sm:mt-16 sm:pt-16 lg:mx-0 lg:max-w-none">
        <div v-for="post in posts" :key="post.id" class="flex w-full flex-col justify-between border-t mt-2 pt-2">
          <div class="group relative">
            <div class="flex">
              <h3 class="flex-[50%] text-lg font-semibold leading-6 text-gray-900 group-hover:text-gray-600">
                <a href="#">
                  Boost your conversion rate (title)
                </a>
              </h3>
              <div class="flex-[50%] text-right self-center">
                <span class="rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10">
                  {{ post.reputation }}
                </span>
              </div>
            </div>
            <img :src="post.imageUrl" class="mt-5 w-full" />
            <p class="mt-3 line-clamp-3 text-sm leading-6 text-gray-600">
              {{ post.description }}
            </p>
            <div class="flex">
              <div class="flex-[50%] mt-5 flex items-center gap-x-4">
                <img :src="post.authorImageUrl" :alt="post.authorName" class="h-10 w-10 rounded-full bg-gray-50">
                <div class="text-sm leading-6">
                  <p class="font-semibold text-gray-900">
                    <a href="#">
                      {{ post.authorName }}
                    </a>
                  </p>
                </div>
              </div>
              <div class="flex-[50%] flex">
                <div class="ml-auto flex text-right mt-5 gap-x-4">
                  <img :src="post.authorImageUrl" :alt="post.authorName" class="h-10 w-10 rounded-full bg-gray-50">
                  <div class="text-sm leading-6">
                    <p class="font-semibold text-gray-900">
                      <a href="#">
                        {{ post.gameName }}
                      </a>
                    </p>
                    <p class="text-gray-600">
                      {{ post.gameStudio }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
const API_URL = import.meta.env.VITE_API_URL

export default {
  data() {
    return {
      posts: []
    }
  },
  methods: {
    getPosts() {
      axios.get(`${API_URL}/post`, {
        withCredentials: true
      })
        .then(request => {
          this.posts = request.data.content
        })
        .catch(reason => {
          this.$router.push('/')
        })
    }
  },
  mounted() {
    this.getPosts()
  }
}
</script>

<style>

</style>