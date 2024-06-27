<script setup>

import {ref} from "vue";
import {View} from "@element-plus/icons-vue";
import {getAllApplicationList, getApplicationDoctorInfo, updateApplicationStatus} from "@/api/application.js";
import {ElMessage} from "element-plus";

const dialogVisible = ref(false);
const targetStatus = ref('');
const applications = ref([]);
const nowApplication = ref({});
const nowDoctor = ref({});
const form = ref(null);


const getApplications = async () => {
  const res = await getAllApplicationList();
  applications.value = res.data;
};

const getDoctor = async (id) => {
  console.log(id);
  const res = await getApplicationDoctorInfo({
    id: id
  });
  nowDoctor.value = res.data;
};


const showDialog = (row) => {
  dialogVisible.value = true;
  nowApplication.value = row;
  getDoctor(row.id);
};

const updateStatus = async (id) => {
  const res = await updateApplicationStatus({
    id: id,
    status: targetStatus.value
  });
  await getApplications();
};

const updateStatusSuccess = (id) => {
  targetStatus.value = 'SUC';
  updateStatus(id);
  ElMessage.success('操作成功');
  dialogVisible.value = false;
};


const updateStatusFail = (id) => {
  targetStatus.value = 'FAL';
  updateStatus(id);
  ElMessage.success('操作成功');
  dialogVisible.value = false;
};

getApplications()

</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>医生认证</span>
      </div>
    </template>
    <el-table :data="applications" style="width: 100%">
      <el-table-column label="序号" width="100" type="index"></el-table-column>
      <el-table-column label="姓名" prop="name"></el-table-column>
      <el-table-column label="目标职位" prop="appliedRole"></el-table-column>
      <el-table-column label="当前状态">
        <template #default="{ row }">
          <el-tag v-if="row.status === 'ING'" type="info">待通过</el-tag>
          <el-tag v-else-if="row.status === 'SUC'" type="success">已通过</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button :icon="View" circle plain type="primary" @click="showDialog(row)"></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据"/>
      </template>
    </el-table>

    <el-dialog v-model="dialogVisible" title="详情信息" width="30%">
      <div class="card-content">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-descriptions label-position="top" :column="1" :border="true">
              <el-descriptions-item label="姓名">{{ nowDoctor.name }}</el-descriptions-item>
              <el-descriptions-item label="身份证号">{{ nowDoctor.idNumber }}</el-descriptions-item>
              <el-descriptions-item label="科室">{{ nowDoctor.departmentName }}</el-descriptions-item>
              <el-descriptions-item label="介绍">{{ nowDoctor.introduction }}</el-descriptions-item>
              <el-descriptions-item label="联系方式">{{ nowDoctor.contactInfo }}</el-descriptions-item>
            </el-descriptions>
            <el-row class="button-row">
              <el-col :span="12">
                <el-button type="primary" @click="updateStatusSuccess(nowApplication.id)">通过</el-button>
              </el-col>
              <el-col :span="12">
                <el-button type="danger" @click="updateStatusFail(nowApplication.id)">拒绝</el-button>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </el-card>
</template>

<style scoped lang="scss">
.page-container {
  min-height: 100%;
  box-sizing: border-box;

  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .card-content {
    padding: 20px;

    .el-descriptions-item__label {
      font-weight: bold;
      color: #606266;
    }

    .button-row {
      margin-top: 20px;
    }
  }
}



</style>