<script setup>

import {ref} from "vue";
import {Aim, Edit, View} from "@element-plus/icons-vue";
import {doctorAppointmentList, getAppointmentPatient, updateAppointment} from "@/api/appointment.js";
import {submitDiagnostic} from "@/api/diagnostic.js";
import {getMedication} from "@/api/medication.js";
import {ElMessage} from "element-plus";

const dialogVisible = ref(false);
const medicineDialogVisible = ref(false);
const diagnosisDialogVisible = ref(false);
const targetStatus = ref('');
const appointments = ref([]);
const nowAppointment = ref({});
const nowPatient = ref({});
const form = ref(null);
const allMedicine = ref([]);
const currentMedicine = ref({});

const getAllMedicine = async () => {
  const res = await getMedication();
  allMedicine.value = res.data;
};


const getAppointments = async () => {
  const res = await doctorAppointmentList();
  appointments.value = res.data;
};

const getPatient = async (id) => {
  const res = await getAppointmentPatient({
    id: id
  });
  nowPatient.value = res.data;
};

const diagnosisInfo = ref({
  medicalRecordNumber: '',
  diagnosis: '',
  reason: '',
  medicineList: []
});

const clearDiagnosisInfo = () => {
  diagnosisInfo.value = {
    medicalRecordNumber: '',
    diagnosis: '',
    reason: '',
    medicineList: []
  };
};



const showDialog = (row) => {
  dialogVisible.value = true;
  nowAppointment.value = row;
  getPatient(row.id);

};

const submit = () => {
  form.value.validate(async valid => {
    if (valid) {
      diagnosisInfo.value.medicalRecordNumber = nowAppointment.value.medicalRecordNumber;
      diagnosisInfo.value.reason = nowPatient.value.reason;
      const res = await submitDiagnostic(diagnosisInfo.value);
      diagnosisDialogVisible.value = false;
      dialogVisible.value = false;
      ElMessage.success('提交成功');
      await getAppointments()
      clearDiagnosisInfo();
    }
  });
};

const rules = {
  diagnosis: [
    {required: true, message: '请输入诊断结果', trigger: 'blur'},
  ]
};

const syncCurrentMedicine = () => {
  const medicine = allMedicine.value.find(item => item.id === currentMedicine.value.id);
  currentMedicine.value.name = medicine.name;
};

const clearCurrentMedicine = () => {
  currentMedicine.value = {};
};

const addMedicine = () => {
  diagnosisInfo.value.medicineList.push(currentMedicine.value);
  medicineDialogVisible.value = false;
  syncCurrentMedicine();
  clearCurrentMedicine();
};

const updateStatus = async (id) => {
  const res = await updateAppointment({
    id: id,
    status: targetStatus.value
  });
  await getAppointments();
};

const updateStatusSuccess = (id) => {
  targetStatus.value = 'SUCCESS';
  updateStatus(id);
  ElMessage.success('操作成功');
  dialogVisible.value = false;
};


const updateStatusFail = (id) => {
  targetStatus.value = 'FAIL';
  updateStatus(id);
  ElMessage.success('操作成功');
  dialogVisible.value = false;
};

getAllMedicine();

getAppointments()

</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>预约系统</span>
      </div>
    </template>
    <el-table :data="appointments" style="width: 100%">
      <el-table-column label="序号" width="100" type="index"></el-table-column>
      <el-table-column label="预约号" prop="id"></el-table-column>
      <el-table-column label="预约时间" prop="appointmentDate"></el-table-column>
      <el-table-column label="当前状态">
        <template #default="{ row }">
          <el-tag v-if="row.status === 'PENDING'" type="info">待通过</el-tag>
          <el-tag v-else-if="row.status === 'SUCCESS'" type="success">已通过</el-tag>
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
                <el-descriptions-item label="姓名">{{ nowPatient.name }}</el-descriptions-item>
                <el-descriptions-item label="年龄">{{ nowPatient.age }}</el-descriptions-item>
                <el-descriptions-item label="性别">{{ nowPatient.gender === 'female' ? '女' : '男' }}</el-descriptions-item>
                <el-descriptions-item label="预约原因">{{ nowPatient.reason }}</el-descriptions-item>
                <el-descriptions-item label="联系方式">{{ nowPatient.contactInfo }}</el-descriptions-item>
              </el-descriptions>
              <el-row v-if="nowAppointment.status === 'PENDING'" class="button-row">
                <el-col :span="12">
                  <el-button type="primary" @click="updateStatusSuccess(nowAppointment.id)">通过</el-button>
                </el-col>
                <el-col :span="12">
                  <el-button type="danger" @click="updateStatusFail(nowAppointment.id)">拒绝</el-button>
                </el-col>
              </el-row>
              <div v-else class="button-row">
                <el-button type="primary" @click="diagnosisDialogVisible=true" :disabled="!nowAppointment.isRegistered">{{ nowAppointment.isRegistered ? '诊断' : '等待用户挂号' }}</el-button>
              </div>
            </el-col>
          </el-row>
        </div>
      <el-dialog title="诊断" v-model="diagnosisDialogVisible">
        <el-form ref="form" :rules="rules" :model="diagnosisInfo">
          <el-form-item label="病历号">
            <el-input v-model="nowAppointment.medicalRecordNumber" disabled/>
          </el-form-item>
          <el-form-item label="诊断结果" prop="diagnosis">
            <el-input type="textarea" rows="4" v-model="diagnosisInfo.diagnosis"/>
          </el-form-item>
          <el-form-item label="处方">
            <el-button type="primary" @click="medicineDialogVisible=true">添加药物</el-button>
            <el-table :data="diagnosisInfo.medicineList">
              <el-table-column prop="name" label="药物名称"></el-table-column>
              <el-table-column prop="usage" label="用量"></el-table-column>
              <el-table-column label="操作">
                <template #default="{row}">
                  <el-button type="danger" @click="diagnosisInfo.medicineList.splice(diagnosisInfo.medicineList.indexOf(row), 1)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submit">提交</el-button>
          </el-form-item>
        </el-form>
        <el-dialog title="添加药物" v-model="medicineDialogVisible">
          <el-form>
            <el-form-item label="药物名称">
              <el-select v-model="currentMedicine.id" placeholder="请选择药物">
                <el-option v-for="item in allMedicine" :key="item.id" :label="item.name" :value="item.id"/>
              </el-select>
            </el-form-item>
            <el-form-item label="用量">
              <el-input v-model="currentMedicine.usage"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="addMedicine">添加</el-button>
            </el-form-item>
          </el-form>
        </el-dialog>
      </el-dialog>
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