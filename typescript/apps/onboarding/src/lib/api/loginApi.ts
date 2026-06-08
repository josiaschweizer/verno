import { createApiClient } from '@verno/lib/apiClient'

export type LoginRequest = {
  tenantName: string
  username: string
  password: string
}

export type LoginResponse = {
  loginSessionId: string
  status: 'STARTING' | 'READY'

  tenant: {
    id: string
    name: string
    key: string
    subdomain: string
  }

  user: {
    id: string
    username: string
    displayName: string
    role: 'ADMIN' | 'USER'
  }

  redirectUrl: string

  warmup: {
    status: 'STARTING' | 'READY'
    message: string
    checkUrl: string
  }
}

const env = (import.meta as any).env as any

const baseUrl = env.VITE_API_BASE_URL || 'http://localhost:8080'
const apiUser = (env.VITE_PROVISIONER_API_USER as string | undefined) || 'verno'
const apiPass = (env.VITE_PROVISIONER_API_PASS as string | undefined) || 'verno'

const client = createApiClient({
  baseUrl,
  basicAuth: { user: apiUser, pass: apiPass },
})

export const loginApi = {
  async login(req: LoginRequest) {
    return await client.request<LoginResponse>({
      method: 'POST',
      path: '/api/v1/login',
      body: req,
    })
  },
}
