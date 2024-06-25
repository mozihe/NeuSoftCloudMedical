import request from "@/utils/request.js";

export function submitRegistration(data) {
    return request.post('/registration/submit', data)
}

export function getPreRegistration() {
    return request.get('/registration/pre')
}