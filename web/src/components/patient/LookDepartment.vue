<script setup>

import {Edit} from "@element-plus/icons-vue";
import {ref} from "vue";
import {getDepartmentList} from "@/api/department.js";

const dialogVisible = ref(false);
const currentDepartment = ref({});
const departments = ref([]);

const showDialog = (row) => {
  currentDepartment.value = row;
  dialogVisible.value = true;
};

const getDepartmentListData = async () => {
  const res = await getDepartmentList();
  departments.value = res.data;
};

getDepartmentListData()

</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>科室概览</span>
      </div>
    </template>
    <el-table :data="departments" style="width: 100%">
      <el-table-column label="序号" width="100" type="index"></el-table-column>
      <el-table-column label="科室名称" prop="name"></el-table-column>
      <el-table-column label="查看详情" width="100">
        <template #default="{ row }">
          <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据"/>
      </template>
    </el-table>

    <el-dialog v-model="dialogVisible" title="科室详情" width="30%">
      <el-form label-width="80px">
        <el-form-item label="科室名称">
          <el-input v-model="currentDepartment.name" disabled/>
        </el-form-item>
        <el-form-item label="科室描述">
          <el-input v-model="currentDepartment.description" type="textarea" disabled/>
        </el-form-item>
      </el-form>
    </el-dialog>
  </el-card>
</template>

<style lang="scss" scoped>
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