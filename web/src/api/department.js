import request from "@/utils/request.js";

export function getDepartmentList() {
    return request.get('/department/list')
}