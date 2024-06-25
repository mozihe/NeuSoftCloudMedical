import request from "@/utils/request.js";

export function getMedication() {
    return request.get('/medication/list')
}