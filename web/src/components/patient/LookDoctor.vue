<script setup>

import {Edit} from "@element-plus/icons-vue";
import {ref} from "vue";
import {getDepartmentList} from "@/api/department.js";
import {getAllVerifiedDoctorList} from "@/api/doctor.js";

const dialogVisible = ref(false);
const currentDoctor = ref({});
const departments = ref([]);
const doctors = ref([]);

const showDialog = (row) => {
  currentDoctor.value = row;
  dialogVisible.value = true;
};

const getDepartmentListData = async () => {
  const res = await getDepartmentList();
  departments.value = res.data;
};

const getDepartmentName = (id) => {
  if (id === 1) return '药房';
  const department = departments.value.find(department => department.id === id);
  return department ? department.name : '';
};

const getDoctorListData = async () => {
  const res = await getAllVerifiedDoctorList();
  doctors.value = res.data;
};

getDepartmentListData()
getDoctorListData()

</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>医生概览</span>
      </div>
    </template>
    <el-table :data="doctors" style="width: 100%">
      <el-table-column label="序号" width="100" type="index"></el-table-column>
      <el-table-column label="医生名称" prop="name"></el-table-column>
      <el-table-column label="医生职称" prop="role">
        <template #default="{ row }">
          <el-tag v-if="row.role === 'doctor'" type="info">医生</el-tag>
          <el-tag v-else-if="row.role === 'expert'" type="info">专家</el-tag>
          <el-tag v-else-if="row.role === 'drug'" type="info">药房医生</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="所属科室" prop="role">
        <template #default="{ row }">
          <span>{{ getDepartmentName(row.departmentId) }}</span>
        </template>
      </el-table-column>
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
        <el-form-item label="医生名称">
          <el-input v-model="currentDoctor.name" disabled/>
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="currentDoctor.contactInfo" disabled/>
        </el-form-item>
        <el-form-item label="详细信息">
          <el-input v-model="currentDoctor.introduction" type="textarea" disabled/>
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