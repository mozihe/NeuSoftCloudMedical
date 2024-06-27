import request from "@/utils/request.js";

export function getMedication() {
    return request.get('/medication/list')
}

export function deleteMedication(data) {
    return request.post('/medication/delete', data)
}

export function addMedication(data) {
    return request.post('/medication/add', data)
}

export function updateMedication(data) {
    return request.post('/medication/update', data)
}