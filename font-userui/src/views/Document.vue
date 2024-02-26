<template>
  <el-card
    class="box-card"
    direction="vertical"
    style="height: 60vh; width: 80%; margin-left: 12%"
  >
    <template #header>
      <el-steps :active="stepSum" :align-center="true">
        <el-step title="Step 1" :icon="Download" description="获取token" />
        <el-step
          title="Step 2"
          :icon="Setting"
          description="设置图床存储目录"
        />
        <el-step title="Step 3" :icon="Pointer" description="安装到图床" />
      </el-steps>
    </template>
    <component :is="appearStep"></component>
    <el-divider />
    <div>
      <span style="margin-left: 13%"
        ><el-button :disabled="disabledPreButton" @click="pre()"
          >上一步</el-button
        ></span
      >
      <span style="margin-left: 3%"
        ><el-button
          :disabled="disabledNextButton"
          @click="next()"
          type="primary"
          >下一步</el-button
        ></span
      >
      <!-- 放置一些内容在这里 -->
    </div>
  </el-card>
</template>

<script lang="ts" setup>
import { Pointer, Setting, Download } from "@element-plus/icons-vue";
import { computed, ref } from "vue";
import Step1 from "@/components/Steps/Step1.vue";
import Step2 from "@/components/Steps/Step2.vue";
import Step3 from "@/components/Steps/Step3.vue";

//步骤条按钮方法
const stepSum = ref(1);
const disabledPreButton = ref(true);
const disabledNextButton = ref(false);
const next = () => {
  stepSum.value++;
  console.log(stepSum.value);
  if (stepSum.value === 3) {
    disabledNextButton.value = true;
  }
  if (stepSum.value > 1 && disabledPreButton.value === true) {
    disabledPreButton.value = false;
  }
};

const pre = () => {
  stepSum.value--;
  if (stepSum.value === 1) {
    disabledPreButton.value = true;
  }
  if (stepSum.value < 3 && disabledNextButton.value === true) {
    disabledNextButton.value = false;
  }
};
const appearStep = computed(() => {
  switch (stepSum.value) {
    case 1:
      return Step1;
    case 2:
      return Step2;
    case 3:
      return Step3;
  }
});
</script>

<style lang="less" scoped></style>
