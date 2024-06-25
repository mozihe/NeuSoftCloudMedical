<script setup>
import { ref } from 'vue';
import { getDepartmentList } from '@/api/department';
import { submitApplication} from "@/api/application.js";
import {ElMessage} from "element-plus";

const departments = ref([]);
const form = ref(null);
const getDepartmentsData = async () => {
  const res = await getDepartmentList();
  departments.value = res.data;
};
const applyInfo = ref({
  name: '',
  idNumber: '',
  contactInfo: '',
  departmentId: '',
  targetRole: '',
  introduction: '',
});

const clearApplyInfo = () => {
  applyInfo.value = {
    name: '',
    idNumber: '',
    contactInfo: '',
    departmentId: '',
    targetRole: '',
    introduction: '',
  };
};

const submitApply = () => {
  form.value.validate(async (valid) => {
    if (valid) {
      await submitApplication(applyInfo.value);
      ElMessage.success('提交成功，请等待管理员审批');
      clearApplyInfo();
    }
  });
};

const changeDrug = () => {
  if (applyInfo.value.targetRole === 'drug') {
    applyInfo.value.departmentId = 1;
  } else {
    applyInfo.value.departmentId = '';
  }
};

const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在 2 到 20 个字符', trigger: 'blur'}
  ],
  idNumber: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { min: 15, max: 18, message: '请输入正确的身份证号', trigger: 'blur'}
  ],
  contactInfo: [
    { required: true, message: '请输入联系方式', trigger: 'blur' },
    { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur'}
  ],
  departmentId: [
    { required: true, message: '请选择科室', trigger: 'change' },
  ],
  targetRole: [
    { required: true, message: '请选择职位', trigger: 'change' },
  ],
  introduction: [
    { required: true, message: '请输入简介', trigger: 'blur' },
    { min: 10, max: 1000, message: '长度在 10 到 1000 个字符', trigger: 'blur'}
  ],
};

const ifIsDrug = () => {
  return applyInfo.value.targetRole === 'drug';
};

getDepartmentsData();
</script>

<template>
  <div class="container">
    <h1 class="centered-text">您的账号未得到管理员认证，请提交信息并联系管理员审批</h1>
    <el-card class="auto-size-card">
      <el-form ref="form" size="large" :rules="rules" :model="applyInfo">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="applyInfo.name"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="idNumber">
          <el-input v-model="applyInfo.idNumber"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="contactInfo">
          <el-input v-model="applyInfo.contactInfo"></el-input>
        </el-form-item>
        <el-form-item label="科室" prop="departmentId" v-if="!ifIsDrug()">
          <el-select v-model="applyInfo.departmentId" placeholder="请选择科室">
            <el-option
                v-for="dept in departments"
                :key="dept.id"
                :label="dept.name"
                :value="dept.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="职位" prop="targetRole">
          <el-select v-model="applyInfo.targetRole" placeholder="请选择职位" @change="changeDrug">
            <el-option label="医生" value="doctor"></el-option>
            <el-option label="专家" value="expert"></el-option>
            <el-option label="药房医生" value="drug"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="简介" prop="introduction">
          <el-input type="textarea" :rows="4" v-model="applyInfo.introduction"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitApply">提交</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.centered-text {
  text-align: center;
}

.auto-size-card {
  width: 100%;
  max-width: 600px;
  margin-top: 20px;

}


</style>
