<template>
  <div id="container">
    <PostContainer v-if="post" :post="post" :enable-show-post-link="false" show-comments />
  </div>
</template>
<script lang="ts">
import { useMessage } from 'naive-ui';
import axios from 'axios';
import { defineComponent, type PropType, type Ref, ref } from 'vue';
import PostContainer from '@/components/post/PostContainer.vue';
import type Post from '@/interface/post';
import type ShowPostRequest from '@/interface/response/showPostResponse';

export default defineComponent({
  components: {
    PostContainer
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
      message: useMessage()
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
              imageUrl: post.authorImageUrl
            },
            userVote: post.userVote,
            createdAt: new Date(post.createdAt),
            modifiedAt: new Date(post.modifiedAt)
          };
        })
        .catch((reason) => {
          // failed to fetch post
          this.message.error('Ocorreu um erro na busca por postagens do usuário');
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
</style>
