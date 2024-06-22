import axios from "axios";
import {useTokenStore} from "@/stores/token.js";
import {ElMessage} from "element-plus";
import {useMailStore} from "@/stores/mail.js";
import router from "@/router";

const instance = axios.create({baseURL: '/api'});


instance.interceptors.request.use(
    config => {
        const tokenStore = useTokenStore();
        const mailStore = useMailStore();
        if (tokenStore.token) {
            config.headers.Authorization = tokenStore.token
        }
        if (mailStore.mailCode) {
            config.headers.MailCode = mailStore.mailCode
        }
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

instance.interceptors.response.use(
    result => {
        if (result.data.code === 200) {
            return result.data
        }
        if (result.data.code === 401) {
            ElMessage.error("请先登录")
            router.push('/login')
            return Promise.reject(result.data)
        }
        ElMessage.error(result.data.message ? result.data.message : '服务异常')
        return Promise.reject(result.data)

    },
    error => {
        if (error.response.status === 401) {
            ElMessage.error("请先登录")
            router.push('/login')
        } else {
            ElMessage.error('服务异常')
        }
        return Promise.reject(error)
    }
)

export default instance