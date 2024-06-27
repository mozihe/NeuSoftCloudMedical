<script setup>

import {Mug, Notebook, Promotion, User, Wallet} from "@element-plus/icons-vue";
</script>

<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <div class="el-aside__logo"></div>
      <el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff"
               router>
        <el-menu-item index="/board/admin/department">
          <el-icon>
            <Promotion/>
          </el-icon>
          <span>管理科室</span>
        </el-menu-item>
        <el-menu-item index="/board/admin/drug">
          <el-icon>
            <Mug />
          </el-icon>
          <span>管理药品</span>
        </el-menu-item>
        <el-menu-item index="/board/admin/doctor">
          <el-icon>
            <User />
          </el-icon>
          <span>认证医生</span>
        </el-menu-item>
        <el-menu-item index="/board/admin/report">
          <el-icon>
            <Notebook/>
          </el-icon>
          <span>查询诊断</span>
        </el-menu-item>
        <el-menu-item index="/board/admin/payment">
          <el-icon>
            <Wallet/>
          </el-icon>
          <span>查询缴费</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container class="content-container">
      <router-view style="width: 100%"/>
    </el-container>

  </el-container>
</template>

<style  lang="scss" scoped>
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