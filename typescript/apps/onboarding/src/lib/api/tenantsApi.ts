import { createApiClient } from '@/lib/apiClient'

export type CreateTenantRequest = {
  tenantKey: string
  tenantName: string
  subdomain: string
  preferredLanguage: string
  adminEmail: string
  adminDisplayName: string
  adminPassword: string
}

export type CreateTenantResponse = {
  tenantId: string
  tenantKey: string
  schemaName: string
  status: string
  dbStatus: string
}

const env = (import.meta as any).env as any

const baseUrl =
  (env.VITE_PROVISIONER_BASE_URL as string | undefined) ||
  'http://localhost:8080'
const apiUser = (env.VITE_PROVISIONER_API_USER as string | undefined) || 'verno'
const apiPass = (env.VITE_PROVISIONER_API_PASS as string | undefined) || 'verno'

console.log('provisioner env', { baseUrl, apiUser, hasPass: Boolean(apiPass) })

const client = createApiClient({
  baseUrl,
  basicAuth: { user: apiUser, pass: apiPass },
})

export const tenantsApi = {
  async createTenant(req: CreateTenantRequest) {
    console.log('createTenant -> sending', req)

    const res = await client.request<CreateTenantResponse>({
      method: 'POST',
      path: '/api/v1/tenants',
      body: req,
    })

    console.log('createTenant -> response', res)
    return res
  },

  async listTenants() {
    return await client.request<any[]>({
      method: 'GET',
      path: '/api/v1/tenants',
    })
  },

  async getTenant(tenantKey: string) {
    return await client.request<any>({
      method: 'GET',
      path: `/api/v1/tenants/${encodeURIComponent(tenantKey)}`,
    })
  },

  async reconcileTenant(tenantKey: string) {
    return await client.request<any>({
      method: 'POST',
      path: `/api/v1/tenants/${encodeURIComponent(tenantKey)}/reconcile`,
    })
  },
}
