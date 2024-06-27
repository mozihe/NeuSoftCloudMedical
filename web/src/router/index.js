import LoginRegister from "@/components/LoginRegister.vue";
import Layout from "@/components/Layout.vue";
import { useRoleStore } from "@/stores/role.js";
import { createRouter, createWebHistory } from "vue-router";
import Doctor from "@/components/doctor/Doctor.vue";
import Patient from "@/components/patient/Patient.vue";
import Admin from "@/components/doctor/Admin.vue";
import Drug from "@/components/doctor/Drug.vue";
import WaitVerify from "@/components/doctor/WaitVerify.vue";
import Submission from "@/components/doctor/wait_verify/WaitVerifySubmission.vue";
import MySubbmision from "@/components/doctor/wait_verify/MySubbmision.vue";
import Registered from "@/components/patient/Registered.vue";
import Appointment from "@/components/patient/Appointment.vue";
import PatientInfo from "@/components/patient/PatientInfo.vue";
import LookDepartment from "@/components/patient/LookDepartment.vue";
import LookDoctor from "@/components/patient/LookDoctor.vue";
import DocRegistered from "@/components/doctor/doc/DocRegistered.vue";
import DocAppointment from "@/components/doctor/doc/DocAppointment.vue";
import DocCheck from "@/components/doctor/doc/DocCheck.vue";
import DocInfo from "@/components/doctor/doc/DocInfo.vue";
import PatientReport from "@/components/patient/PatientReport.vue";
import Payment from "@/components/patient/Payment.vue";
import DrugPayment from "@/components/doctor/drug/DrugPayment.vue";
import DrugInfo from "@/components/doctor/drug/DrugInfo.vue";
import AdminDepartment from "@/components/doctor/admin/AdminDepartment.vue";
import AdminMedication from "@/components/doctor/admin/AdminMedication.vue";
import AdminDoctor from "@/components/doctor/admin/AdminDoctor.vue";
import AdminReport from "@/components/doctor/admin/AdminReport.vue";
import AdminPayment from "@/components/doctor/admin/AdminPayment.vue";

const routes = [
    {path: "/login", component: LoginRegister},
    {
        path: "/board",
        component: Layout,
        children: [
            {
                path: "admin",
                component: Admin,
                redirect: '/board/admin/department',
                children: [
                    {path: "department", component: AdminDepartment},
                    {path: "drug", component: AdminMedication},
                    {path: "doctor", component: AdminDoctor},
                    {path: "report", component: AdminReport},
                    {path: "payment", component: AdminPayment}
                ]
            },
            {
                path: "doctor",
                component: Doctor,
                redirect: '/board/doctor/docregistered',
                children: [
                    {path: "docregistered", component: DocRegistered},
                    {path: "docappoinment", component: DocAppointment},
                    {path: "check", component: DocCheck},
                    {path: "info", component: DocInfo},
                ]
            },
            {
                path: "patient",
                component: Patient,
                redirect: '/board/patient/registered',
                children: [
                    {path: "registered", component: Registered},
                    {path: "reserve", component: Appointment},
                    {path: "info", component: PatientInfo},
                    {path: "report", component: PatientReport},
                    {path: "payment", component: Payment},
                    {path: "lookdepartment", component: LookDepartment},
                    {path: "lookdoctor", component: LookDoctor}
                ]
            },
            {
                path: "drug",
                component: Drug,
                redirect: '/board/drug/payment',
                children: [
                    {path: "payment", component: DrugPayment},
                    {path: "info", component: DrugInfo}
                ]
            },
            {
                path: "waitverify",
                component: WaitVerify,
                redirect: '/board/waitverify/submission',
                children: [
                    {path: "submission", component: Submission},
                    {path: "my", component: MySubbmision}
              ]
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

router.beforeEach((to, from, next) => {
    const roleStore = useRoleStore();
    if (to.path === '/') {
        if (roleStore.role) {
            if (roleStore.role === 'admin') {
                next('/board/admin')
            } else if (roleStore.role === 'doctor' || roleStore.role === 'expert') {
                next('/board/doctor')
            } else if (roleStore.role === 'patient') {
                next('/board/patient')
            } else if (roleStore.role === 'drug') {
                next('/board/drug')
            } else {
                next('/board/waitverify')
            }
        } else {
            next('/login')
        }
    } else {
        next()
    }
})

export default router;