<script setup>

import {ref} from "vue";
import {updateDoctorInfo} from "@/api/doctor.js";
import {ElMessage} from "element-plus";
import {useUserInfoStore} from "@/stores/userInfo.js";
import {getDoctorInfo} from "@/api/doctor.js";

const userInfoStore = useUserInfoStore()

const doctorInfo = ref({
  name: '',
  contactInfo: '',
  introduction: ''
})
const form = ref(null)
const rules = {
  name: [
    {required: true, message: '请输入姓名', trigger: 'blur'},
    {min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur'}
  ],
  contactInfo: [
    {required: true, message: '请输入联系方式', trigger: 'blur'},
    {pattern: /^1[3456789]\d{9}$/, message: '请输入正确的电话号码', trigger: 'blur'}
  ],
  introduction: [
    {required: true, message: '请输入个人简介', trigger: 'blur'},
    {min: 5, max: 100, message: '长度在 5 到 100 个字符', trigger: 'blur'}
  ]
}

const updateDoctorData = async () => {
  form.value.validate(async (valid) => {
    if (valid) {
      await updateDoctorInfo(doctorInfo.value)
      ElMessage.success('修改成功')
      const res = await getDoctorInfo()
      userInfoStore.setUserInfo(res.data)
      await initDoctorInfo()
    }

  })
}

const initDoctorInfo = async () => {
  doctorInfo.value.contactInfo = userInfoStore.userInfo.contactInfo
  doctorInfo.value.name = userInfoStore.userInfo.name
  doctorInfo.value.introduction = userInfoStore.userInfo.introduction
}

initDoctorInfo()

</script>

<template>
  <div class="container">
    <el-card class="auto-size-card">
      <template #header>
        <div class="header">
          <span>个人信息</span>
        </div>
      </template>
      <el-form ref="form" size="large" :rules="rules" :model="doctorInfo">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="doctorInfo.name"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="contactInfo">
          <el-input v-model="doctorInfo.contactInfo"></el-input>
        </el-form-item>
        <el-form-item label="个人简介" prop="age">
          <el-input v-model="doctorInfo.introduction" type="textarea" rows="4"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="updateDoctorData">修改信息</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
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