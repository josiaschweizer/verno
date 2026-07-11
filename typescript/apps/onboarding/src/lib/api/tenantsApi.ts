import { api } from './client'

export type CreateTenantRequest = {
  tenantKey: string
  tenantName: string
  subdomain: string
  preferredLanguage: string
  adminUsername: string
  adminFirstname: string
  adminLastname: string
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

export const tenantsApi = {
  createTenant(req: CreateTenantRequest) {
    return api.request<CreateTenantResponse>({
      method: 'POST',
      path: '/api/tenants',
      body: req,
    })
  },

  getCountOfTenants() {
    return api.request<any>({ method: 'GET', path: '/api/tenants/count' })
  },

  getTotalMemberCount() {
    return api.request<any>({
      method: 'GET',
      path: '/api/application/memberCount',
    })
  },

  getTotalCourseCount() {
    return api.request<any>({
      method: 'GET',
      path: '/api/application/courseCount',
    })
  },
}
