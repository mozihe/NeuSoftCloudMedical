import LoginRegister from "@/components/LoginRegister.vue";
import Layout from "@/components/Layout.vue";
import { useRoleStore } from "@/stores/role.js";
import { createRouter, createWebHistory } from "vue-router";
import Doctor from "@/components/doctor/Doctor.vue";
import Patient from "@/components/patient/Patient.vue";
import Admin from "@/components/doctor/Admin.vue";
import Drug from "@/components/doctor/Drug.vue";
import Expert from "@/components/doctor/Expert.vue";
import WaitVerify from "@/components/doctor/WaitVerify.vue";

const routes = [
    {path: "/login", component: LoginRegister},
    {
        path: "/board",
        component: Layout,
        children: [
            {path: "admin", component: Admin},
            {path: "doctor", component: Doctor},
            {path: "patient", component: Patient},
            {path: "drug", component: Drug},
            {path: "expert", component: Expert},
            {path: "waitverify", component: WaitVerify}
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
            } else if (roleStore.role === 'doctor') {
                next('/board/doctor')
            } else if (roleStore.role === 'patient') {
                next('/board/patient')
            } else if (roleStore.role === 'drug') {
                next('/board/drug')
            } else if (roleStore.role === 'expert') {
                next('/board/expert')
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