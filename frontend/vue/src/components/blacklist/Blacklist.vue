<template>
  <div class="container mx-auto max-w-screen-lg">
    <!-- 이모티콘 리스트 -->
    <div class="grid grid-cols-1 gap-4 pt-4 px-4">
      <BlacklistedEmoticonDiv
        v-for="emoticon in displayedEmoticonList"
        :key="emoticon.id"
        :emoticon="emoticon"
      />
    </div>
    <div v-if="loading" class="flex justify-center items-center">
      <Loading />
    </div>
    <div
      v-if="displayedEmoticonList.length === 0"
      class="flex justify-center items-center h-24 bg-gray-200 rounded-lg mt-4"
    >
      <p class="text-gray-500">표시할 내용이 없습니다.</p>
    </div>
    <div
      v-if="errorOccurred"
      class="flex flex-col justify-center items-center bg-gray-200 rounded-lg mt-4 gap-4 p-4"
    >
      <p class="text-gray-500">이모티콘을 불러오는 중 오류가 발생했습니다.</p>
      <button
        @click="loadMoreEmoticons"
        class="rounded-full border border-gray-300 px-4 py-2 bg-white hover:bg-gray-100 active:bg-gray-300"
      >
        다시시도
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import { RouterLink } from "vue-router";
import { useInfiniteScroll } from "@vueuse/core";
import Loading from "../common/loading/Loading.vue";
import { useObjectionStore } from "@/stores/objection";
import type { BlacklistedEmoticon } from "@/types/blacklistResult";
import BlacklistedEmoticonDiv from "./BlacklistedEmoticonDiv.vue";

const blockedEmoticonList = ref<BlacklistedEmoticon[]>([]);
const currentPage = ref(0);
const loading = ref(false);
const hasMore = ref(true);
const errorOccurred = ref(false);

const props = defineProps<{
  displayedState: "PENDING" | "RECEIVED" | "APPROVED" | "REJECTED";
}>();

const displayedEmoticonList = computed(() => {
  return blockedEmoticonList.value.filter(
    (emoticon) => emoticon.state === props.displayedState
  );
});

const objectionStore = useObjectionStore();

const loadMoreEmoticons = async () => {
  if (loading.value || !hasMore.value) return;

  loading.value = true;
  try {
    const newEmoticonsResult = await objectionStore.getBlockedEmoticonPackList(
      currentPage.value++,
      10
    );
    if (newEmoticonsResult.last) {
      hasMore.value = false;
    }
    blockedEmoticonList.value.push(
      ...newEmoticonsResult.content.map((item) => item)
    );
    errorOccurred.value = false;
  } catch (error) {
    errorOccurred.value = true;
    console.error("이모티콘을 불러오는 중 오류가 발생했습니다:", error);
  } finally {
    loading.value = false;
  }
};

useInfiniteScroll(
  window,
  async () => {
    if (!errorOccurred.value) {
      await loadMoreEmoticons();
    }
  },
  { distance: 400 }
);

onMounted(async () => {
  await loadMoreEmoticons();
});
</script>