import request from "@/utils/request.js";

export function registerPatient(data) {
    return request.post('/patient/register', data)
}

export function loginPatient(data) {
    return request.post('/patient/login', data)
}

export function getPatientInfo() {
    return request.get('/patient/info')
}

export function updatePatientAvatar(data) {
    return request.post('/patient/update/avatar', data)
}

export function updatePatientPassword(data) {
    return request.post('/patient/update/password', data)
}