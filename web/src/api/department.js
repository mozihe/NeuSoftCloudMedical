import request from "@/utils/request.js";

export function getDepartmentList() {
    return request.get('/department/list')
}

export function deleteDepartment(data) {
    return request.post('/department/delete', data)
}

export function addDepartment(data) {
    return request.post('/department/add', data)
}

export function updateDepartment(data) {
    return request.post('/department/update', data)
}