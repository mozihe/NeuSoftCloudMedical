import request from "@/utils/request.js";

export function submitDiagnostic(data) {
    return request.post('/diagnosticreport/submit', data)
}

export function docGetDiagnosticList() {
    return request.get('/diagnosticreport/doctor/list')
}

export function getDiaPatientInfo(data) {
    return request.post('/diagnosticreport/doctor/getpatient', data)
}

export function getDiaMedicineList(data) {
    return request.post('/diagnosticreport/medicine/list', data)
}

export function patientGetDiagnosticList() {
    return request.get('/diagnosticreport/patient/list')
}

export function adminGetDiagnosticList() {
    return request.get('/diagnosticreport/admin/list')
}