<template>
  <div>
    <div id="header">
      <div class="left">
        <n-skeleton height="32px" width="32px" :sharp="false" />
        <n-skeleton text style="margin-left: 5px; width: 8em" />
      </div>
      <div class="right">
        <n-skeleton text style="width: 8em" />
      </div>
    </div>
    <div id="content">
      <n-skeleton style="width: 100%; height: 400px" />
    </div>
    <div id="footer">
      <div class="left">
        <n-skeleton height="32px" width="96px" :sharp="false" />
      </div>
      <div class="right">
        <n-skeleton text style="width: 10em" />
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { NSkeleton } from 'naive-ui';

export default {
  components: {
    NSkeleton
  }
};
</script>
<style scoped>
#header {
  display: flex;
  flex-direction: row;
  align-items: center;
  /* justify-content: center; */
  .left,
  .right {
    display: flex;
    flex-direction: row;
    align-items: center;
    width: 50%;
  }
  .left {
  }
  .right {
    justify-content: right;
  }
}
#content {
  padding: 10px 0;
}
#footer {
  display: flex;
  flex-direction: row;
  align-items: center;
  /* justify-content: center; */
  .left,
  .right {
    display: flex;
    flex-direction: row;
    align-items: center;
    width: 50%;
  }
  .left {
  }
  .right {
    justify-content: right;
  }
}
</style>
