import request from "@/utils/request.js";

export function getPatientPayment() {
    return request.get('/payment/get')
}

export function getAllPayment() {
    return request.get('/payment/getall')
}

export function finishPayment(data) {
    return request.post('/payment/finish', data)
}

export function getAdminPayment() {
    return request.get('/payment/getadmin')
}
