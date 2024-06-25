import request from "@/utils/request.js";

export function submitRegistration(data) {
    return request.post('/registration/submit', data)
}

export function getPreRegistration() {
    return request.get('/registration/pre')
}

export function getNextRegistration() {
    return request.get('/registration/doctor/getnext')
}