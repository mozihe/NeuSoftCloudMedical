import request from "@/utils/request.js";

export function submitApplication(data) {
    return request.post('/application/submit', data)
}

export function getApplicationList(data) {
    return request.get('/application/list', data)
}

export function getAllApplicationList() {
    return request.get('/application/allList')
}

export function getApplicationDoctorInfo(data) {
    return request.post('/application/doctor/info', data)
}

export function updateApplicationStatus(data) {
    return request.post('/application/update/status', data)
}
