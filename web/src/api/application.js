import request from "@/utils/request.js";

export function submitApplication(data) {
    return request.post('/application/submit', data)
}

export function getApplicationList(data) {
    return request.get('/application/list', data)
}
