import {defineStore} from "pinia";
import {ref} from "vue";

export const useMailStore = defineStore('mailCode',
    () => {
        const mailCode = ref('')

        const setMail = (newMailCode) => {
            mailCode.value = newMailCode
        }

        const removeMail = () => {
            mailCode.value = ''
        }

        return {
            mailCode, setMail, removeMail
        }
    },
    {
        persist: true
    });