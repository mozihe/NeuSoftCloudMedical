<script setup>
import {
  Promotion,
} from '@element-plus/icons-vue'
</script>

<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <div class="el-aside__logo"></div>
      <el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff"
               router>
        <el-menu-item index="/board/waitverify/submission">
          <el-icon>
            <Promotion/>
          </el-icon>
          <span>提交申请</span>
        </el-menu-item>
        <el-menu-item index="/board/waitverify/my">
          <el-icon>
            <Promotion/>
          </el-icon>
          <span>我的申请</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container class="content-container">
      <router-view style="width: 100%"/>
    </el-container>

  </el-container>

</template>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;

  .el-aside {
    background-color: #232323;
    user-select: none;

    &__logo {
      height: 100px;
      background: url('@/assets/logo.svg') no-repeat center / 100px auto;
    }

    .el-menu {
      border-right: none;
    }
  }
}
.content-container {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

</style>