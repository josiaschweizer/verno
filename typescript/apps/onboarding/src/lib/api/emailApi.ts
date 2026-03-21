import { createApiClient } from '@verno/lib/apiClient'

export type SendEmailRequest = {}

export type SendEmailResponse = {}

const env = (import.meta as any).env as any

const baseUrl =
  (env.VITE_API_BASE_URL as string | undefined) || 'http://localhost:8080'
const apiUser = (env.VITE_PROVISIONER_API_USER as string | undefined) || 'verno'
const apiPass = (env.VITE_PROVISIONER_API_PASS as string | undefined) || 'verno'

const client = createApiClient({
  baseUrl,
  basicAuth: { user: apiUser, pass: apiPass },
})

export const emailApi = {
  async sendMessage(req: SendEmailRequest): Promise<SendEmailResponse> {
    return await client.request<SendEmailResponse>({
      method: 'POST',
      path: '/api/v1/email',
      body: req,
    })
  },
}
