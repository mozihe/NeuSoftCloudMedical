<script setup>

import {ref} from "vue";
import {getDepartmentList} from "@/api/department.js";
import {getDepartmentDoctorList} from "@/api/doctor.js";
import {submitRegistration, getPreRegistration} from "@/api/registration.js";
import {ElMessage} from "element-plus";

const departments = ref([]);
const form = ref(null);
const registerInfoPre = ref(
    {
      ifAppointment: false,
      doctorName: '',
      departmentName: '',
      medicalRecord: '',
      waitPeople: 0,
    }
);

const getDepartmentsData = async () => {
  const res = await getDepartmentList();
  departments.value = res.data;
};

const getRegisterInfoPre = async () => {
  const res = await getPreRegistration();
  registerInfoPre.value = res.data;
};

const registerInfo = ref({
  departmentId: '',
  doctorId: '',
  reason: '',
  isAppointment: false,
  appointId: ''
});

const clearRegisterInfo = () => {
  registerInfo.value = {
    departmentId: '',
    doctorId: '',
    reason: '',
    isAppointment: false,
    appointId: ''
  };
};

const doctors = ref([]);
const getDoctorsData = async () => {
  if (!registerInfo.value.departmentId) return;
  registerInfo.value.doctorId = '';
  const res = await getDepartmentDoctorList({
    id: registerInfo.value.departmentId
  });
  doctors.value = res.data;
};

const submit = async () => {
  form.value.validate(async (valid) => {
    if (valid) {
      const res = await submitRegistration(registerInfo.value);
      ElMessage.success('挂号成功');
      clearRegisterInfo();
      await getRegisterInfoPre();
    }
  });
};

const rules = {
  departmentId: [
    { required: true, message: '请选择科室', trigger: 'change' }
  ],
  doctorId: [
    { required: true, message: '请选择医生', trigger: 'change' }
  ],
  reason: [
    { required: true, message: '请输入挂号原因', trigger: 'blur' },
    { min: 10, max: 50, message: '长度在 10 到 100 个字之间', trigger: 'blur'}
  ],
};

getDepartmentsData()
getRegisterInfoPre()

</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>挂号系统</span>
      </div>
    </template>
    <el-form ref="form" size="large" :rules="rules" :model="registerInfo" v-if="!registerInfoPre.ifAppointment">
      <el-form-item label="科室" prop="departmentId">
        <el-select v-model="registerInfo.departmentId" placeholder="请选择科室" @change="getDoctorsData">
          <el-option
              v-for="dept in departments"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="医生" prop="doctorId">
        <el-select v-model="registerInfo.doctorId" placeholder="请选择医生" :disabled="!registerInfo.departmentId">
          <el-option
              v-for="doc in doctors"
              :key="doc.id"
              :label="doc.name"
              :value="doc.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="挂号原因" prop="reason">
        <el-input v-model="registerInfo.reason"></el-input>
      </el-form-item>
      <el-form-item label="是否预约">
        <el-switch v-model="registerInfo.isAppointment"></el-switch>
      </el-form-item>
      <el-form-item v-if="registerInfo.isAppointment" label="预约号">
        <el-input v-model="registerInfo.appointId"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit">提交</el-button>
      </el-form-item>
    </el-form>
    <div v-if="registerInfoPre.ifAppointment">
      <el-row justify="center" align="middle" class="full-height">
        <el-col :span="12">
          <el-card>
            <el-alert
                title="您已挂号"
                type="success"
                show-icon
                :closable="false"
                class="alert"
            ></el-alert>
            <el-descriptions title="挂号信息" column="1" border>
              <el-descriptions-item label="医生姓名">{{ registerInfoPre.doctorName }}</el-descriptions-item>
              <el-descriptions-item label="科室名称">{{ registerInfoPre.departmentName }}</el-descriptions-item>
              <el-descriptions-item label="病历号">{{ registerInfoPre.medicalRecord }}</el-descriptions-item>
              <el-descriptions-item label="等待人数">{{ registerInfoPre.waitPeople }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
      </el-row>
    </div>
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

  .alert {
    margin-bottom: 20px;
  }
}

</style>