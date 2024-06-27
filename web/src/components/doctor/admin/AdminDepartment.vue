<script setup>

import {Delete, Edit} from "@element-plus/icons-vue";

import {ref} from "vue";
import {addDepartment, deleteDepartment, getDepartmentList, updateDepartment} from "@/api/department.js";
import {ElMessage, ElMessageBox} from "element-plus";

const dialogVisible = ref(false);
const departments = ref([]);
const form = ref(null);
const updateDialogVisible = ref(false);
const updateInfo = ref({
  name: '',
  description: ''
});

const departmentInfo = ref({
  name: '',
  description: ''
});
const rules = {
  name: [
    { required: true, message: '请输入科室名', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入科室简介', trigger: 'blur' },
    { min: 5, max: 100, message: '长度在 5 到 100 个字', trigger: 'blur' }
  ]
};

const showDialog = (row) => {
  updateInfo.value = row;
  updateDialogVisible.value = true;
};

const getDepartments = async () => {
  const res = await getDepartmentList();
  departments.value = res.data;
};

const deleteById = (row) => {
  ElMessageBox.confirm('确定删除该科室吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteDepartment({
      id: row.id});
    await getDepartments();
    ElMessage.success('删除成功');
  }).catch(() => {
  });
};

const submit = async () => {
  form.value.validate(async (valid) => {
    if (valid) {
      await addDepartment(departmentInfo.value);
      dialogVisible.value = false;
      await getDepartments();
      ElMessage.success('添加成功');
    }
  });
};

const update = async () => {
  form.value.validate(async (valid) => {
    if (valid) {
      await updateDepartment(updateInfo.value);
      updateDialogVisible.value = false;
      await getDepartments();
      ElMessage.success('修改成功');
    }
  });
};

getDepartments()

</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>管理科室</span>
        <div class="extra">
          <el-button type="primary" @click="dialogVisible = true">添加科室</el-button>
        </div>
      </div>
    </template>
    <el-table :data="departments" style="width: 100%">
      <el-table-column label="序号" width="100" type="index"></el-table-column>
      <el-table-column label="科室名" prop="name"></el-table-column>
      <el-table-column label="科室简介" prop="description"></el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
          <el-button :icon="Delete" circle plain type="danger" @click="deleteById(row)"></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据"/>
      </template>
    </el-table>

    <el-dialog v-model="dialogVisible" title="增加科室" width="30%">
      <el-form ref="form" size="large" :rules="rules" :model="departmentInfo">
        <el-form-item label="科室名" prop="name">
          <el-input v-model="departmentInfo.name"></el-input>
        </el-form-item>
        <el-form-item label="科室简介" prop="description">
          <el-input v-model="departmentInfo.description" type="textarea" rows="4"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submit"> 确认 </el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="updateDialogVisible" title="修改信息" width="30%">
      <el-form ref="form" size="large" :rules="rules" :model="updateInfo">
        <el-form-item label="科室名" prop="name">
          <el-input v-model="updateInfo.name"></el-input>
        </el-form-item>
        <el-form-item label="科室简介" prop="description">
          <el-input v-model="updateInfo.description" type="textarea" rows="4"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
            <el-button @click="updateDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="update"> 确认 </el-button>
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