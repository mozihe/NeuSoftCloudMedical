import request from "@/utils/request.js";

export function registerDoctor(data) {
    return request.post('/doctor/register', data)
}

export function loginDoctor(data) {
    return request.post('/doctor/login', data)
}

export function getDoctorInfo() {
    return request.get('/doctor/info')
}

export function updateDoctorAvatar(data) {
    return request.post('/doctor/update/avatar', data)
}

export function updateDoctorPassword(data) {
    return request.post('/doctor/update/password', data)
}

export function getDepartmentDoctorList(data) {
    return request.post('/doctor/department/list', data)
}

export function getAllVerifiedDoctorList() {
    return request.get('/doctor/look/list')
}