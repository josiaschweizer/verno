import { api } from './client'

export type SendEmailRequest = {
  from: string
  to: string
  subject: string
  message: string
}

export type SendEmailResponse = {
  success?: boolean
  message?: string
}

export const emailApi = {
  sendMessage(payload: SendEmailRequest): Promise<SendEmailResponse> {
    return api.request<SendEmailResponse>({
      method: 'POST',
      path: '/api/email',
      body: payload,
    })
  },
}
