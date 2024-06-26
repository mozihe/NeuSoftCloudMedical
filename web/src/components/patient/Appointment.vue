<script setup>

import {Delete} from "@element-plus/icons-vue";
import {getDepartmentDoctorList} from "@/api/doctor.js";
import {getDepartmentList} from "@/api/department.js";
import {ref} from "vue";
import {submitAppointment, getAppointmentList, deleteAppointment} from "@/api/appointment.js";
import {ElMessage, ElMessageBox} from "element-plus";

const dialogVisible = ref(false);
const form = ref(null);
const departments = ref([]);
const doctors = ref([]);
const appointments = ref([]);

const getDepartmentsData = async () => {
  const res = await getDepartmentList();
  departments.value = res.data;
};

const getDoctorsData = async () => {
  if (!appointmentInfo.value.departmentId) return;
  appointmentInfo.doctorId = '';
  const res = await getDepartmentDoctorList({
    id: appointmentInfo.value.departmentId
  });
  doctors.value = res.data;
};

const appointmentInfo = ref({
  departmentId: '',
  doctorId: '',
  reason: '',
  appointmentDate: ''
});

const rules = {
  departmentId: [
    { required: true, message: '请选择科室', trigger: 'change' },
  ],
  doctorId: [
    { required: true, message: '请选择医生', trigger: 'change' },
  ],
  reason: [
    { required: true, message: '请输入挂号原因', trigger: 'blur' },
    { min: 10, max: 100, message: '长度在 10 到 100 个字', trigger: 'blur'}
  ],
  appointmentDate: [
    { required: true, message: '请选择预约时间', trigger: 'change' },
  ],
};

const clearAppointmentInfo = () => {
  appointmentInfo.value = {
    departmentId: '',
    doctorId: '',
    reason: '',
    appointmentDate: ''
  };
};

const submit = async () => {
  form.value.validate(async (valid) => {
    if (valid) {
      console.log(appointmentInfo.value);
      const res = await submitAppointment(appointmentInfo.value);
      ElMessage.success('预约成功');
      dialogVisible.value = false;
      await getAppointmentData();
      clearAppointmentInfo();
    }
  });
};

const getAppointmentData = async () => {
  const res = await getAppointmentList();
  appointments.value = res.data;
};

const deleteById = async (row) => {
  ElMessageBox.confirm(
      '你确认要删除该预约吗？',
      '温馨提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }
  ).then(
      async () => {
        await deleteAppointment({
          id: row.id
        })
        ElMessage.success("删除成功")
        await getAppointmentData()
      }
  )
};

getAppointmentData()
getDepartmentsData()

</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>预约系统</span>
        <div class="extra">
          <el-button type="primary" @click="dialogVisible = true">新的预约</el-button>
        </div>
      </div>
    </template>
    <el-table :data="appointments" style="width: 100%">
      <el-table-column label="序号" width="100" type="index"></el-table-column>
      <el-table-column label="预约号" prop="id"></el-table-column>
      <el-table-column label="预约医生" prop="doctorName"></el-table-column>
      <el-table-column label="预约时间" prop="appointmentDate"></el-table-column>
      <el-table-column label="当前状态">
        <template #default="{ row }">
          <el-tag v-if="row.status === 'PENDING'" type="info">待通过</el-tag>
          <el-tag v-else-if="row.status === 'SUCCESS'" type="success">已通过</el-tag>
          <el-tag v-else-if="row.status === 'FAIL'" type="danger">被拒绝</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button :icon="Delete" circle plain type="danger" @click="deleteById(row)"></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据"/>
      </template>
    </el-table>

    <el-dialog v-model="dialogVisible" title="增加预约" width="30%">
      <el-form ref="form" size="large" :rules="rules" :model="appointmentInfo">
        <el-form-item label="科室" prop="departmentId">
          <el-select v-model="appointmentInfo.departmentId" placeholder="请选择科室" @change="getDoctorsData">
            <el-option
                v-for="dept in departments"
                :key="dept.id"
                :label="dept.name"
                :value="dept.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="医生" prop="doctorId">
          <el-select v-model="appointmentInfo.doctorId" placeholder="请选择医生" :disabled="!appointmentInfo.departmentId">
            <el-option
                v-for="doc in doctors"
                :key="doc.id"
                :label="doc.name"
                :value="doc.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="挂号原因" prop="reason">
          <el-input v-model="appointmentInfo.reason"></el-input>
        </el-form-item>
        <el-form-item label="预约时间" prop="appointmentDate">
          <el-date-picker
              v-model="appointmentInfo.appointmentDate"
              type="datetime"
              placeholder="选择日期时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submit"> 确认 </el-button>
        </span>
      </template>
    </el-dialog>
  </el-card>
</template>

<style scoped>
.page-container {
  min-height: 100%;
  box-sizing: border-box;

  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}

</style>