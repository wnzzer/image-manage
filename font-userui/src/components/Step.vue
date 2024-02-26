<template>
    <div>
      <el-steps :active="activeStep" finish-status="success">
        <el-step title="Step 1"></el-step>
        <el-step title="Step 2"></el-step>
        <el-step title="Step 3"></el-step>
      </el-steps>
      <div class="step-buttons">
        <el-button @click="prev" :disabled="activeStep === 0">上一步</el-button>
        <el-button @click="next" :disabled="activeStep === 2">下一步</el-button>
      </div>
    </div>
  </template>
  
  <script lang="ts">
  import { defineComponent, ref } from "vue";
  import { ElSteps, ElStep, ElButton } from 'element-plus';
  
  export default defineComponent({
    components: {
      ElSteps,
      ElStep,
      ElButton
    },
    setup() {
      const activeStep = ref(0);
  
      const next = () => {
        if (activeStep.value < 2) {
          activeStep.value++;
        }
      };
  
      const prev = () => {
        if (activeStep.value > 0) {
          activeStep.value--;
        }
      };
  
      return {
        activeStep,
        next,
        prev,
      };
    }
  });
  </script>
  
  <style>
  .step-buttons {
    margin-top: 20px;
  }
  </style>
  