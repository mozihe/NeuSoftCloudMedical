<script setup>
import {Edit} from "@element-plus/icons-vue";
import {ref} from "vue";
import {getDiaPatientInfo, getDiaMedicineList, patientGetDiagnosticList} from "@/api/diagnostic.js";

const diagnosisList = ref([]);
const nowPatient = ref({});
const dialogVisible = ref(false);
const nowMedicineList = ref([]);
const nowDiagnosis = ref({});

const getDiagnosisList = async () => {
  const res = await patientGetDiagnosticList();
  diagnosisList.value = res.data;
};

const getPatientInfo = async (medicalRecordNumber) => {
  const res = await getDiaPatientInfo({
    medicalRecordNumber: medicalRecordNumber
  });
  nowPatient.value = res.data;
};

const getMedicineList = async (medicalRecordNumber) => {
  const res = await getDiaMedicineList({
    medicalRecordNumber: medicalRecordNumber
  });
  nowMedicineList.value = res.data;
};

const showDialog = (row) => {
  dialogVisible.value = true;
  getPatientInfo(row.medicalRecordNumber);
  nowDiagnosis.value = row;
  getMedicineList(row.medicalRecordNumber);
};

getDiagnosisList()
</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>预约系统</span>
      </div>
    </template>
    <el-table :data="diagnosisList" style="width: 100%">
      <el-table-column label="序号" width="100" type="index"></el-table-column>
      <el-table-column label="病历号" prop="medicalRecordNumber"></el-table-column>
      <el-table-column label="医生" prop="targetName"></el-table-column>
      <el-table-column label="时间" prop="createdAt"></el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据"/>
      </template>
    </el-table>

    <el-dialog v-model="dialogVisible" title="详情信息" width="30%">
      <div class="card-content">
        <el-row :gutter="20">
          <el-col :span="20">
            <el-descriptions label-position="top" :column="1" :border="true">
              <el-descriptions-item label="姓名">{{ nowPatient.name }}</el-descriptions-item>
              <el-descriptions-item label="年龄">{{ nowPatient.age }}</el-descriptions-item>
              <el-descriptions-item label="性别">{{ nowPatient.gender === 'female' ? '女' : '男' }}</el-descriptions-item>
              <el-descriptions-item label="预约原因">{{ nowPatient.reason }}</el-descriptions-item>
              <el-descriptions-item label="联系方式">{{ nowPatient.contactInfo }}</el-descriptions-item>
              <el-descriptions-item label="医生诊断">{{ nowDiagnosis.diagnosis }}</el-descriptions-item>
              <el-descriptions-item label="用药">
                <el-table :data="nowMedicineList" style="width: 100%">
                  <el-table-column label="序号" width="100" type="index"></el-table-column>
                  <el-table-column label="药物名称" prop="medicationName"></el-table-column>
                  <el-table-column label="用量" prop="dosage"></el-table-column>
                </el-table>
              </el-descriptions-item>
            </el-descriptions>
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

  }
}

</style>