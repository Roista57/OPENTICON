<template>
    <div class="container mt-4 mx-auto max-w-screen-lg">
      <h2 class="px-4 pt-4">
        <span class="text-2xl font-nnsqneo font-bold pr-2">인기 이모티콘</span>
        <RouterLink
          :to="{ name: 'popularList' }"
          class="text-md text-gray-500 hover:font-bold font-nnsqneo"
          >더보기 >
        </RouterLink>
      </h2>
      <!-- 이모티콘 리스트 -->
      <div class="md:grid md:grid-cols-5 md:gap-4 pt-4 md:px-4">
        <!-- md 크기 이하일 때는 가로 스크롤 -->
        <div class="flex gap-4 overflow-x-auto md:hidden pb-4 px-4">
          <RouterLink
            :to="{ name: 'packDetail', params: { id: emoticon.id } }"
            class="border p-4 emoticon-item-no-hover w-40 flex-shrink-0"
            v-for="emoticon in popularEmoticonList"
            :key="emoticon.id"
          >
            <img :src="emoticon.thumbnailImg" alt="이모티콘" class="w-full object-cover aspect-square" />
            <p class="text-md text-center mt-2 line-">{{ emoticon.title }}</p>
          </RouterLink>
        </div>
        <!-- md 크기 이상일 때는 그리드 -->
        <RouterLink
          :to="{ name: 'packDetail', params: { id: emoticon.id } }"
          class="hidden md:block border p-4 emoticon-item"
          v-for="emoticon in popularEmoticonList"
          :key="emoticon.id"
        >
          <img :src="emoticon.thumbnailImg" alt="이모티콘" class="w-full object-cover aspect-square" />
          <p class="text-md text-center mt-2 line-clamp-2">{{ emoticon.title }}</p>
        </RouterLink>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { useEmoticonPackStore } from "@/stores/emoticonPack";
  import type { EmoticonPackInList } from "@/types/emoticonPackInList";
  import type { EmoticonPackSearchList } from "@/types/emoticonPackSearchList";
  import { onMounted, ref } from "vue";
  import { RouterLink } from "vue-router";
  
  const popularEmoticonList = ref<EmoticonPackInList[]>([]);
  
  const emoticonPackStore = useEmoticonPackStore();
  onMounted(() => {
    emoticonPackStore.getPopularEmoticonPackList(0, 10).then((res: EmoticonPackSearchList) => {
      popularEmoticonList.value = res.content;
    });
  });
  </script>
  
  <style scoped>
  /* 스크롤바 숨기기 */
  .overflow-x-auto {
    -ms-overflow-style: none;
    /* IE, Edge */
    scrollbar-width: none;
    /* Firefox */
  }
  
  .overflow-x-auto::-webkit-scrollbar {
    display: none;
    /* Chrome, Safari, Opera */
  }
  
  /* 호버 효과 추가 */
  .emoticon-item {
    transition: transform 0.05s ease, box-shadow 0.05s ease,
      border-color 0.05s ease, border-radius 0.05s ease;
    cursor: pointer;
    @apply hover:font-bold hover:underline hover:rounded-lg hover:shadow-lg hover:scale-105;
  }
  
  .emoticon-item-no-hover {
    cursor: pointer;
    @apply rounded-lg shadow-lg;
  }
  </style>
  