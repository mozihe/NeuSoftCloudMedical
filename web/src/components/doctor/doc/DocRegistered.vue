<script setup>
import {ref} from "vue";
import {getMedication} from "@/api/medication.js";
import {getNextRegistration} from "@/api/registration.js";
import {submitDiagnostic} from "@/api/diagnostic.js";
import {ElMessage} from "element-plus";

const patientInfo = ref({
  isEmpty: false,
  medicalRecordNumber: '',
  name: '',
  age: '',
  gender: '',
  registrationReason: '',
  contactInfo: ''
});

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

const dialogVisible = ref(false);
const medicineDialogVisible = ref(false);
const currentMedicine = ref({});
const allMedicine = ref([]);
const form = ref(null);

const clearCurrentMedicine = () => {
  currentMedicine.value = {};
};

const addMedicine = () => {
  diagnosisInfo.value.medicineList.push(currentMedicine.value);
  medicineDialogVisible.value = false;
  syncCurrentMedicine();
  clearCurrentMedicine();
};

const syncCurrentMedicine = () => {
  const medicine = allMedicine.value.find(item => item.id === currentMedicine.value.id);
  currentMedicine.value.name = medicine.name;
};

const getAllMedicine = async () => {
  const res = await getMedication();
  allMedicine.value = res.data;
};

const getPatientInfo = async () => {
  const res = await getNextRegistration();
  patientInfo.value = res.data;
};

const submit = () => {
  form.value.validate(async valid => {
    if (valid) {
      diagnosisInfo.value.medicalRecordNumber = patientInfo.value.medicalRecordNumber;
      diagnosisInfo.value.reason = patientInfo.value.registrationReason;
      const res = await submitDiagnostic(diagnosisInfo.value);
        dialogVisible.value = false;
        await getPatientInfo();
        ElMessage.success('提交成功');
        clearDiagnosisInfo();
    }
  });
};

const rules = {
  diagnosis: [
    {required: true, message: '请输入诊断结果', trigger: 'blur'},
  ]
};

getAllMedicine();
getPatientInfo();

</script>

<template>
  <el-card class="page-container">
    <div v-if="!patientInfo.isEmpty">
      <div class="card-header">
        <span class="card-title">病人信息</span>
      </div>
      <div class="card-content">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-descriptions label-position="top" :column="1" :border="true">
              <el-descriptions-item label="病历号">{{ patientInfo.medicalRecordNumber }}</el-descriptions-item>
              <el-descriptions-item label="姓名">{{ patientInfo.name }}</el-descriptions-item>
              <el-descriptions-item label="年龄">{{ patientInfo.age }}</el-descriptions-item>
              <el-descriptions-item label="性别">{{ patientInfo.gender === 'female' ? '女' : '男' }}</el-descriptions-item>
              <el-descriptions-item label="挂号原因">{{ patientInfo.registrationReason }}</el-descriptions-item>
              <el-descriptions-item label="联系方式">{{ patientInfo.contactInfo }}</el-descriptions-item>
            </el-descriptions>
            <el-button type="primary" @click="dialogVisible = true">进行诊断</el-button>
          </el-col>
        </el-row>
      </div>
    </div>
    <el-empty v-else description="暂时没有挂号"/>
    <el-dialog title="诊断" v-model="dialogVisible">
      <el-form ref="form" :rules="rules" :model="diagnosisInfo">
        <el-form-item label="病历号">
          <el-input v-model="patientInfo.medicalRecordNumber" disabled/>
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
  </el-card>
</template>

<style lang="scss" scoped>
.page-container {
  min-height: 100%;
  box-sizing: border-box;

  .card-header {
    background-color: #f5f7fa;
    padding: 15px;
    font-size: 18px;
    font-weight: bold;
    border-bottom: 1px solid #ebeef5;
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