import request from "@/utils/request.js";

export function getPatientPayment() {
    return request.get('/payment/get')
}