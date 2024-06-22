import request from "@/utils/request.js";

export function sendEmail(data) {
    return request.post('/email/send', data)
}