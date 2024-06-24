<script setup>
import { ref } from 'vue';
import { getApplicationList } from '@/api/application';

const lists = ref([]);
const getLists = async () => {
  const res = await getApplicationList();
  lists.value = res.data;
};

getLists();
</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>我的申请</span>
      </div>
    </template>
    <el-table :data="lists" style="width: 100%">
      <el-table-column label="序号" width="100" type="index"></el-table-column>
      <el-table-column label="提交时间" prop="applicationTime"></el-table-column>
      <el-table-column label="当前状态">
        <template #default="{ row }">
          <el-tag v-if="row.status === 'ING'" type="warning">待审核</el-tag>
          <el-tag v-else-if="row.status === 'SUC'" type="success">已通过</el-tag>
          <el-tag v-else-if="row.status === 'FAL'" type="danger">已拒绝</el-tag>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据"/>
      </template>
    </el-table>
  </el-card>
</template>

<style scoped>
.page-container {
  min-height: 100%;
  box-sizing: border-box;

  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}

</style>