<script setup>

import {Delete, Edit} from "@element-plus/icons-vue";
import {getMedication, addMedication, updateMedication, deleteMedication} from "@/api/medication.js";
import {ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";

const dialogVisible = ref(false);
const medications = ref([]);
const form = ref(null);
const updateDialogVisible = ref(false);
const updateInfo = ref({
  name: '',
  price: ''
});

const medicationInfo = ref({
  name: '',
  price: ''
});
const rules = {
  name: [
    { required: true, message: '请输入药品名', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' },
  ],
};

const showDialog = (row) => {
  updateInfo.value = row;
  updateDialogVisible.value = true;
};

const getMedications = async () => {
  const res = await getMedication();
  medications.value = res.data;
};

const deleteById = (row) => {
  ElMessageBox.confirm('确定删除该药品吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteMedication({
      id: row.id});
    await getMedications();
    ElMessage.success('删除成功');
  }).catch(() => {
  });
};

const submit = async () => {
  form.value.validate(async (valid) => {
    if (valid) {
      await addMedication(medicationInfo.value);
      dialogVisible.value = false;
      await getMedications();
      ElMessage.success('添加成功');
    }
  });
};

const update = async () => {
  form.value.validate(async (valid) => {
    if (valid) {
      await updateMedication(updateInfo.value);
      updateDialogVisible.value = false;
      await getMedications();
      ElMessage.success('修改成功');
    }
  });
};

getMedications()

</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>管理药品</span>
        <div class="extra">
          <el-button type="primary" @click="dialogVisible = true">添加药品</el-button>
        </div>
      </div>
    </template>
    <el-table :data="medications" style="width: 100%">
      <el-table-column label="序号" width="100" type="index"></el-table-column>
      <el-table-column label="药品名" prop="name"></el-table-column>
      <el-table-column label="单价" prop="price"></el-table-column>
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
      <el-form ref="form" size="large" :rules="rules" :model="medicationInfo">
        <el-form-item label="药品名" prop="name">
          <el-input v-model="medicationInfo.name"></el-input>
        </el-form-item>
        <el-form-item label="药品单价" prop="price">
          <el-input v-model="medicationInfo.price"></el-input>
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
        <el-form-item label="药品名" prop="name">
          <el-input v-model="updateInfo.name"></el-input>
        </el-form-item>
        <el-form-item label="药品单价" prop="price">
          <el-input v-model="updateInfo.price"></el-input>
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