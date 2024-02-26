<template>
    <div class="common-layout">
      <el-container>
        <el-aside width="auto"><CommonAside></CommonAside></el-aside>
        <el-container>
          <el-header><Header></Header></el-header>
          <el-main>
            <CommonTag/>
            <div>
            <router-view></router-view>
            </div>
          </el-main>
        </el-container>
      </el-container>
    </div>


</template>

<script lang ='ts'  setup>
import CommonAside from "@/components/CommonAside.vue";
import Header from "@/components/Header.vue";
import CommonTag from "@/components/CommonTag.vue";

</script>

<style lang="less" scoped>
.el-header{
padding: 0;
}
</style>