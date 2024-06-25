import request from "@/utils/request.js";

export function submitAppointment(data) {
    return request.post('/appointment/submit', data)
}

export function getAppointmentList() {
    return request.get('/appointment/list')
}

export function deleteAppointment(data) {
    return request.post(`/appointment/delete`, data)
}
