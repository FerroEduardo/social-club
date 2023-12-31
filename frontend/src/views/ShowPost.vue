<template>
  <div id="container">
    <PostSkeleton id="post" v-if="isLoadingData" />
    <PostContainer
      id="post"
      v-if="post"
      :post="post"
      :enable-open-post-details="false"
      show-comments
      show-management-buttons
      @update-post="fetchPost"
      high-loading-priority
    />
  </div>
</template>
<script lang="ts">
import { useMessage } from 'naive-ui';
import axios from 'axios';
import { defineComponent, type PropType, type Ref, ref } from 'vue';

import PostContainer from '@/components/post/PostContainer.vue';
import type Post from '@/interface/post';
import type ShowPostRequest from '@/interface/response/showPostResponse';
import PostSkeleton from '@/components/post/PostSkeleton.vue';

export default defineComponent({
  components: {
    PostContainer,
    PostSkeleton
  },
  props: {
    postId: {
      type: String as PropType<string>,
      required: true
    }
  },
  setup() {
    return {
      post: ref(null) as Ref<Post | null>,
      message: useMessage(),
      isLoadingData: ref(true) as Ref<boolean>
    };
  },
  methods: {
    fetchPost() {
      const postId = this.postId;

      axios
        .get<ShowPostRequest>(`/post/${postId}`, {
          withCredentials: true
        })
        .then((request) => {
          const post = request.data;
          this.post = {
            id: post.id,
            title: post.title,
            description: post.description,
            reputation: post.reputation,
            imageUrl: post.imageUrl,
            game: {
              id: post.gameId,
              name: post.gameName,
              studio: post.gameStudio,
              imageUrl: post.gameImageUrl
            },
            author: {
              id: post.authorId,
              name: post.authorName,
              avatarUrl: post.authorAvatarUrl,
              miniAvatarUrl: post.authorMiniAvatarUrl
            },
            userVote: post.userVote,
            createdAt: new Date(post.createdAt),
            modifiedAt: new Date(post.modifiedAt)
          };
        })
        .catch((reason) => {
          // failed to fetch post
          this.message.error('Ocorreu um erro na busca por postagens do usuário');
        })
        .finally(() => {
          this.isLoadingData = false;
        });
    }
  },
  mounted() {
    this.fetchPost();
  }
});
</script>
<style scoped>
#container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 5px;
}
#post {
  max-width: 800px;
  width: 100%;
}
</style>
