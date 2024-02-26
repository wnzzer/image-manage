import useUserStore from "@/store/module/user.ts";
import { computed } from "vue";
import img from "@/assets/meiyangyang.jpg" 




export const avatarSrc = computed(() => {
  const userStore = useUserStore();
  // 如果 thumbnailAvatar 存在，则返回完整的 base64 URL
  if (userStore.user.thumbnailAvatar) {
    return `data:image/png;base64,${userStore.user.thumbnailAvatar}`;
  }
  // 否则，返回空字符串
  return img;
});

