import { createApiClient } from '@verno/lib/apiClient'

export type StartWorkspaceRequest = {
  tenantName: string
}

export type StartWorkspaceResponse = {
  tenantSlug: string
  tenantName: string
  startSessionId: string
}

const env = (import.meta as any).env as any

const baseUrl = env.VITE_API_BASE_URL || 'http://localhost:8080'
const apiUser = (env.VITE_PROVISIONER_API_USER as string | undefined) || 'verno'
const apiPass = (env.VITE_PROVISIONER_API_PASS as string | undefined) || 'verno'

const client = createApiClient({
  baseUrl,
  basicAuth: { user: apiUser, pass: apiPass },
})

export const workspaceApi = {
  async startWorkspace(req: StartWorkspaceRequest) {
    return await client.request<StartWorkspaceResponse>({
      method: 'POST',
      path: 'api/v1/workspace/start',
      body: req,
    })
  },
}
