import { createApiClient } from '@verno/lib/apiClient'

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

const env = import.meta.env

const baseUrl = env.VITE_API_BASE_URL || 'http://localhost:8082'
const apiUser = env.VITE_PROVISIONER_API_USER || 'verno'
const apiPass = env.VITE_PROVISIONER_API_PASS || 'verno'

const client = createApiClient({
  baseUrl,
  basicAuth: { user: apiUser, pass: apiPass },
})

export const emailApi = {
  sendMessage(payload: SendEmailRequest): Promise<SendEmailResponse> {
    return client.request<SendEmailResponse>({
      method: 'POST',
      path: '/api/v1/email',
      body: payload,
    })
  },
}
