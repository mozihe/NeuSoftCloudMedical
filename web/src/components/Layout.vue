<script setup>

import {
  CaretBottom,
  Crop,
  EditPen,
  Plus,
  SwitchButton, Upload,
} from '@element-plus/icons-vue'
import {useTokenStore} from "@/stores/token.js";
import {ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {useRouter} from "vue-router";
import avatar from "@/assets/default.png";
import { useRoleStore } from "@/stores/role.js";
import {updatePatientAvatar, updatePatientPassword, getPatientInfo} from "@/api/patient.js";
import {updateDoctorAvatar, updateDoctorPassword, getDoctorInfo} from "@/api/doctor.js";
import { useUserInfoStore} from "@/stores/userInfo.js";


const uploadRef = ref()
const roleStore = useRoleStore()
const tokenStore = useTokenStore()
const userInfoStore = useUserInfoStore()
const uploadVisible = ref(false)
const rePasswordVisible = ref(false)
const imgUrl = ref(userInfoStore.userInfo.avatarUrl)
const router = useRouter()

const getUserInfo = async () => {
  if (roleStore.role === 'patient') {
    const result = await getPatientInfo()
    userInfoStore.setUserInfo(result.data)
  } else {
    const result = await getDoctorInfo()
    roleStore.setRole(result.data.role)
    userInfoStore.setUserInfo(result.data)
  }
}

getUserInfo()

const uploadSuccess = (result) => {
  if (result.code === 401) {
    ElMessage.error("请先登录")
    router.push("/login")
    return
  } else if (result.code !== 200) {
    ElMessage.error("图片上传失败")
    return
  }
  imgUrl.value = result.data;
  ElMessage.success("图片上传成功")
}

const updateAvatar = async () => {
  if (imgUrl.value === '') {
    ElMessage.error("请先上传图片")
    return
  }
  if (roleStore.role === 'patient') {
    await updatePatientAvatar({avatar: imgUrl.value})
  } else {
    await updateDoctorAvatar({avatar: imgUrl.value})
  }
  ElMessage.success("头像更换成功")
  uploadVisible.value = false
}

const logout = () => {
    ElMessageBox.confirm(
        '你确认要退出吗？',
        '温馨提示',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        }
    ).then(
        async () => {
          tokenStore.removeToken()
          roleStore.removeRole()
          userInfoStore.removeUserInfo()
          ElMessage.success("退出成功")
          await router.push('/login')
        }
    )
}

const pwdData = ref({
  old_pwd: '',
  new_pwd: '',
  re_pwd: ''
})

const updatePwdData = async () => {
  if (roleStore.role === 'patient') {
    await updatePatientPassword(
        {
          oldPwd: pwdData.value.old_pwd,
          newPwd: pwdData.value.new_pwd
        }
    )
  } else {
    await updateDoctorPassword(
        {
          oldPwd: pwdData.value.old_pwd,
          newPwd: pwdData.value.new_pwd
        }
    )
  }
  ElMessage.success("密码修改成功")
  rePasswordVisible.value = false
}
const checkRePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次确认密码'))
  } else if (value !== pwdData.value.new_pwd) {
    callback(new Error('两次密码不一样'))
  } else {
    callback()
  }
}
const rules = {
  old_pwd: [
    {required: true, message: '请输入旧密码?', trigger: 'blur'},
    {min: 5, max: 16, message: '长度为5-16位', trigger: 'blur'},
  ],
  new_pwd: [
    {required: true, message: '请输入新密码', trigger: 'blur'},
    {min: 5, max: 16, message: '长度为5-16位', trigger: 'blur'},
  ],
  re_pwd: [
    {required: true, message: '请输入确认新密码', trigger: 'blur'},
    {validator: checkRePassword, trigger: 'blur'},
  ]
}

</script>

<template>
    <el-dialog title="更换头像" v-model="uploadVisible" width="30%">
        <el-row>
          <el-col :span="12">
            <el-upload
                ref="uploadRef"
                class="avatar-uploader"
                :show-file-list="false"
                :auto-upload="true"
                action="/api/upload"
                name="file"
                :headers="{'Authorization':tokenStore.token}"
                :on-success="uploadSuccess"
            >
              <el-image v-if="imgUrl" :src="imgUrl" class="avatar"/>
              <el-image v-else :src="avatar" width="278"/>
            </el-upload>
            <br/>
            <el-button type="primary" :icon="Plus" size="large" @click="uploadRef.$el.querySelector('input').click()">
              选择图片
            </el-button>
            <el-button type="success" :icon="Upload" size="large" @click="updateAvatar">
              上传头像
            </el-button>
          </el-col>
        </el-row>
    </el-dialog>
    <el-dialog title="修改密码" v-model="rePasswordVisible">
      <el-row>
        <el-col :span="12">
          <el-form :model="pwdData" :rules="rules" label-width="100px" size="large">
            <el-form-item label="旧密码" prop="old_pwd">
              <el-input v-model="pwdData.old_pwd" type="password" placeholder="请输入旧密码"></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="new_pwd">
              <el-input v-model="pwdData.new_pwd" type="password" placeholder="请输入新密码"></el-input>
            </el-form-item>
            <el-form-item label="确认新密码" prop="re_pwd">
              <el-input v-model="pwdData.re_pwd" type="password" placeholder="请输入再次新密码"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updatePwdData">提交</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-dialog>

  <el-container class="layout-container">
    <el-container>
      <el-header>
        <div><strong>{{ userInfoStore.userInfo.username }}</strong></div>
        <el-dropdown placement="bottom-end">
                    <span class="el-dropdown__box">
                        <el-avatar :src="userInfoStore.userInfo.avatarUrl !== '' ? userInfoStore.userInfo.avatarUrl:avatar"/>
                        <el-icon>
                            <CaretBottom/>
                        </el-icon>
                    </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="uploadVisible = true" :icon="Crop">更换头像</el-dropdown-item>
              <el-dropdown-item @click="rePasswordVisible = true" :icon="EditPen">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main style="padding: 0">
        <router-view style="padding: 0"/>
      </el-main>
      <el-footer>东软云医疗系统 ©2024 Created by MoXin</el-footer>
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;

  .el-header {
    background-color: #5f97e7;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .el-dropdown__box {
      display: flex;
      align-items: center;

      .el-icon {
        color: #999;
        margin-left: 10px;
      }

      &:active,
      &:focus {
        outline: none;
      }
    }
  }

  .el-footer {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: black;
    background: #5f97e7;
  }
}
.avatar-uploader {
    .avatar {
      width: 278px;
      height: 278px;
      display: block;
    }

    .el-upload {
      border: 1px dashed var(--el-border-color);
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: var(--el-transition-duration-fast);
    }

    .el-upload:hover {
      border-color: var(--el-color-primary);
    }

    .el-icon.avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 278px;
      height: 278px;
      text-align: center;
    }
}

</style>
