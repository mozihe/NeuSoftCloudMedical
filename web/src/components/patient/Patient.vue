<script setup>
import ChatWidget from "@/components/patient/ChatWidget.vue";
import {Bell, Document, Memo, Notebook, Promotion, User, Wallet} from "@element-plus/icons-vue";

</script>

<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <div class="el-aside__logo"></div>
      <el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff"
               router>
        <el-menu-item index="/board/patient/registered">
          <el-icon>
            <Bell/>
          </el-icon>
          <span>我的挂号</span>
        </el-menu-item>
        <el-menu-item index="/board/patient/reserve">
          <el-icon>
            <Document/>
          </el-icon>
          <span>我的预约</span>
        </el-menu-item>
        <el-menu-item index="/board/patient/info">
          <el-icon>
            <User/>
          </el-icon>
          <span>个人信息</span>
        </el-menu-item>
        <el-menu-item index="/board/patient/report">
          <el-icon>
            <Notebook/>
          </el-icon>
          <span>我的报告</span>
        </el-menu-item>
        <el-menu-item index="/board/patient/payment">
          <el-icon>
            <Wallet/>
          </el-icon>
          <span>我的缴费</span>
        </el-menu-item>
        <el-sub-menu>
          <template #title>
            <el-icon>
              <Promotion/>
            </el-icon>
            <span>了解信息</span>
          </template>
          <el-menu-item index="/board/patient/lookdoctor">
            <el-icon>
              <User/>
            </el-icon>
            <span>了解医生</span>
          </el-menu-item>
          <el-menu-item index="/board/patient/lookdepartment">
            <el-icon>
              <Memo/>
            </el-icon>
            <span>了解科室</span>
          </el-menu-item>

        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container class="content-container">
      <router-view style="width: 100%"/>
    </el-container>

  </el-container>

  <ChatWidget />
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
  width: 100%;
}

</style>