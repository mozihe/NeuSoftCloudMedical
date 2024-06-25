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

export function doctorAppointmentList() {
    return request.get('/appointment/doctor/list')
}

export function getAppointmentPatient(data) {
    return request.post('/appointment/doctor/getpatient', data)
}

export function updateAppointment(data) {
    return request.post('/appointment/doctor/update', data)
}
