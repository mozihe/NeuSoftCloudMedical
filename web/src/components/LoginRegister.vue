<script setup>
import {User, Lock, Message, Bell} from '@element-plus/icons-vue'
import { ref } from 'vue'
import { sendEmail } from "@/api/email.js";
import { useMailStore } from "@/stores/mail.js";
import { useTokenStore } from "@/stores/token.js";
import { useRoleStore } from "@/stores/role.js";
import { ElMessage } from "element-plus";
import { registerPatient, loginPatient} from "@/api/patient.js";
import { registerDoctor, loginDoctor } from "@/api/doctor.js";
import { useRouter} from "vue-router";


const isRegister = ref(false)
const timer = ref(0)
const form = ref(null)
const router = useRouter()


const checkRePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次确认密码'))
  } else if (value !== registerData.value.password) {
    callback(new Error('两次密码不一样'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  password2: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: checkRePassword, trigger: 'blur' }
  ]
}


const registerData = ref({
  username: '',
  email: '',
  code: '',
  password: '',
  password2: '',
  role: 'patient'
})

const loginData = ref({
  username: '',
  password: '',
  role: 'patient'
})


const sendCode = async () => {
  timer.value = 60;
  const interval = setInterval(() => {
    timer.value--;
    if (timer.value <= 0) {
      clearInterval(interval);
    }
  }, 1000);


  form.value.validateField('email', async (valid) => {
    if (valid) {
      const result = await sendEmail({
        email: registerData.value.email,
      });

      ElMessage.success(result.message ? result.message : '验证码已发送，请注意查收');
      useMailStore().setMail(result.data);
    }
  });
};

const register = () => {
  form.value.validate(async (valid) => {
    if (valid) {
      if (registerData.value.role === 'patient') {
        const result = await registerPatient({
          username: registerData.value.username,
          email: registerData.value.email,
          code: registerData.value.code,
          password: registerData.value.password,
        });
        ElMessage.success('注册成功');
        clearRegisterData();
        isRegister.value = false;
      } else {
        const result = await registerDoctor({
          username: registerData.value.username,
          email: registerData.value.email,
          code: registerData.value.code,
          password: registerData.value.password,
        });
        ElMessage.success('注册成功');
        clearRegisterData();
        isRegister.value = false;
      }

    }
  });
}

const login = () => {
  form.value.validate(async (valid) => {
    if (valid) {
      if (loginData.value.role === 'patient') {
        const result = await loginPatient({
          username: loginData.value.username,
          password: loginData.value.password,
        });
        ElMessage.success('登录成功');
        useTokenStore().setToken(result.data);
        useRoleStore().setRole('patient');
        router.push('/');
        clearLoginData();
      } else {
        const result = await loginDoctor({
          username: loginData.value.username,
          password: loginData.value.password,
        });
        ElMessage.success('登录成功');
        useTokenStore().setToken(result.data.token);
        useRoleStore().setRole(result.data.role);
        router.push('/');
        clearLoginData();
      }
    }
  });
}

const clearRegisterData = () => {
  registerData.value = {
    username: '',
    email: '',
    code: '',
    password: '',
    password2: '',
    role: 'patient'
  }
}

const clearLoginData = () => {
  loginData.value = {
    username: '',
    password: ''
  }
}


</script>

<template>
  <el-row class="login-page">
    <el-col :span="12" class="bg"/>
    <el-col :span="6" :offset="3" class="form">

      <h1><strong>东软云医疗系统</strong></h1>

      <el-form ref="form" size="large" autocomplete="on" v-if="isRegister" :model="registerData" :rules="rules">
        <el-form-item>
          <h2>注册</h2>
        </el-form-item>
        <el-form-item prop="username">
          <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input :prefix-icon="Message" placeholder="请输入邮箱" v-model="registerData.email"></el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-row style="width: 100%">
            <el-col :span="16">
              <el-input :prefix-icon="Bell" placeholder="请输入验证码" v-model="registerData.code"></el-input>
            </el-col>
            <el-col :span="8">
              <el-button type="primary" @click="sendCode" :disabled="timer > 0">{{ timer > 0 ? `${timer}s 后重新获取` : '获取验证码' }}</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item prop="password">
          <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="registerData.password"></el-input>
        </el-form-item>
        <el-form-item prop="password2">
          <el-input :prefix-icon="Lock" type="password" placeholder="请输入再次密码" v-model="registerData.password2"></el-input>
        </el-form-item>
        <el-form-item>
          <el-radio-group v-model="registerData.role">
            <el-radio-button value="doctor">医生</el-radio-button>
            <el-radio-button value="patient">患者</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button class="button" type="primary" auto-insert-space @click="register">
            注册
          </el-button>
        </el-form-item>
        <el-form-item class="flex">
          <el-link type="info" :underline="false" @click="isRegister = false">
            ← 已经有账号？去登录
          </el-link>
        </el-form-item>
      </el-form>
      <el-form ref="form" size="large" autocomplete="off" v-else :model="loginData" :rules="rules">
        <el-form-item>
          <h2>登录</h2>
        </el-form-item>
        <el-form-item prop="username">
          <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="loginData.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="loginData.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-radio-group v-model="loginData.role">
            <el-radio-button value="doctor">医生</el-radio-button>
            <el-radio-button value="patient">患者</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button class="button" type="primary" auto-insert-space @click="login">登录</el-button>
        </el-form-item>
        <el-form-item class="flex">
          <el-link type="info" :underline="false" @click="isRegister = true">
            没有账号？去注册 →
          </el-link>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<style lang="scss" scoped>

.login-page {
  height: 100vh;
  background-color: #fff;

  .bg {

    background:
        url('@/assets/logo.svg') no-repeat 5% 5% / 240px auto,
        url('@/assets/background.jpg') no-repeat center / cover;
    border-radius: 0 20px 20px 0;
  }

  .form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    user-select: none;

    .title {
      margin: 0 auto;
    }

    .button {
      width: 100%;
    }

    .flex {
      width: 100%;
      display: flex;
      justify-content: space-between;
    }

  }
}
</style>