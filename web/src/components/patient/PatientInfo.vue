<script setup>
import {ref} from "vue";
import {useUserInfoStore} from "@/stores/userInfo.js";
import {getPatientInfo} from "@/api/patient.js";
import {ElMessage} from "element-plus";
import {updatePatientInfo} from "@/api/patient.js";

const patientInfo = ref(
    {
      name: '',
      contactInfo: '',
      gender: '',
      age: '',
      idNumber: '',
    }
)

const userInfoStore = useUserInfoStore();
const isProfileComplete = ref(false);
const form = ref(null);

const getUserInfo = async () => {
  const result = await getPatientInfo()
  userInfoStore.setUserInfo(result.data)
  isProfileComplete.value = result.data.isProfileComplete
  if (result.data.isProfileComplete) {
    patientInfo.value.name = result.data.name;
    patientInfo.value.contactInfo = result.data.contactInfo;
    patientInfo.value.gender = result.data.gender;
    patientInfo.value.age = result.data.age;
    patientInfo.value.idNumber = result.data.idNumber;
  }
}

const rules = {
  name: [
    {required: true, message: '请输入姓名', trigger: 'blur'},
    {min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur'}
  ],
  idNumber: [
    {required: true, message: '请输入身份证号', trigger: 'blur'},
    {min: 15, max: 18, message: '请输入正确的身份证号', trigger: 'blur'}
  ],
  contactInfo: [
    {required: true, message: '请输入联系方式', trigger: 'blur'},
    {min: 5, max: 15, message: '请输入正确的电话号码', trigger: 'blur'}
  ],
  age: [
    {required: true, message: '请输入年龄', trigger: 'blur'},
    {min: 1, max: 3, message: '请输入正确的年龄', trigger: 'blur'}
  ],
  gender: [
    {required: true, message: '请选择性别', trigger: 'change'}
  ]
}

const updatePatientData = async () => {
  form.value.validate(async (valid) => {
    if (valid) {
      const res = await updatePatientInfo(patientInfo.value)
      ElMessage.success('修改成功')
      await getUserInfo()
    }
  })

}

getUserInfo()



</script>

<template>
  <div class="container">
    <el-card class="auto-size-card">
      <template #header>
        <div class="header">
          <span>{{isProfileComplete?'修改信息':'完善信息'}}</span>
        </div>
      </template>
      <el-form ref="form" size="large" :rules="rules" :model="patientInfo">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="patientInfo.name"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="idNumber">
          <el-input v-model="patientInfo.idNumber"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="contactInfo">
          <el-input v-model="patientInfo.contactInfo"></el-input>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="patientInfo.age"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="patientInfo.gender" placeholder="请选择">
            <el-option label="男" value="male"></el-option>
            <el-option label="女" value="female"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="updatePatientData">{{isProfileComplete?'修改信息':'完善信息'}}</el-button>
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

  .auto-size-card {
    width: 100%;
    max-width: 600px;
    margin-top: 20px;

    .header {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

  }

}

</style>