<script setup>
import {Aim, Edit, View} from "@element-plus/icons-vue";
import {ref} from "vue";
import {getDiaMedicineList} from "@/api/diagnostic.js";
import {getPatientPayment} from "@/api/payment.js";

const paymentList = ref([]);
const dialogVisible = ref(false);
const nowMedicineList = ref([]);
const nowPayment = ref({});

const getPaymentList = async () => {
  const res = await getPatientPayment();
  paymentList.value = res.data;
};


const getMedicineList = async (medicalRecordNumber) => {
  const res = await getDiaMedicineList({
    medicalRecordNumber: medicalRecordNumber
  });
  nowMedicineList.value = res.data;
};

const showDialog = (row) => {
  dialogVisible.value = true;
  nowPayment.value = row;
  getMedicineList(row.medicalRecordNumber);
};

getPaymentList()

</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>预约系统</span>
      </div>
    </template>
    <el-table :data="paymentList" style="width: 100%">
      <el-table-column label="序号" width="100" type="index"></el-table-column>
      <el-table-column label="病历号" prop="medicalRecordNumber"></el-table-column>
      <el-table-column label="时间" prop="createdAt"></el-table-column>
      <el-table-column label="总金额" prop="totalAmountDue"></el-table-column>
      <el-table-column label="状态">
        <template #default="{ row }">
          <el-tag v-if="row.isPaymentComplete === false" type="info">待支付</el-tag>
          <el-tag v-else-if="row.isPaymentComplete === true" type="success">已支付</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button :icon="View" circle plain type="primary" @click="showDialog(row)"></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据"/>
      </template>
    </el-table>

    <el-dialog v-model="dialogVisible" title="详情信息" width="40%">
      <div class="card-content">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-descriptions label-position="top" :column="1" :border="true">
              <el-descriptions-item label="病历号">{{ nowPayment.medicalRecordNumber }}</el-descriptions-item>
              <el-descriptions-item label="挂号费">{{ nowPayment.registrationFee }}</el-descriptions-item>
              <el-descriptions-item label="用药">
                <el-table :data="nowMedicineList" style="width: 100%">
                  <el-table-column label="序号" width="100" type="index"></el-table-column>
                  <el-table-column label="药物名称" prop="medicationName"></el-table-column>
                  <el-table-column label="单价" prop="price"></el-table-column>
                  <el-table-column label="用量" prop="dosage"></el-table-column>
                </el-table>
              </el-descriptions-item>
              <el-descriptions-item label="总金额">{{ nowPayment.totalAmountDue }}</el-descriptions-item>
            </el-descriptions>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </el-card>
</template>

<style scoped lang="scss">
.page-container {
  min-height: 100%;
  box-sizing: border-box;

  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
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