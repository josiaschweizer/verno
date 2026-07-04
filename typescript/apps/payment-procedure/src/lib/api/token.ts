import { createApiClient } from '@verno/lib/apiClient'
import type { ResolveBillingAccessTokenResponse } from '@/type/api/token/ResolveBillingAccessTokenResponse.ts'
import type { ResolveBillingAccessTokenRequest } from '@/type/api/token/ResolveBillingAccessTokenRequest.ts'

const env = (import.meta as any).env as any

const baseUrl =
  (env.VITE_API_BASE_URL as string | undefined) || 'http://localhost:8082'

const client = createApiClient({
  baseUrl,
})

export const billingTokensApi = {
  async resolveEntryToken(token: string) {
    return await client.request<ResolveBillingAccessTokenResponse>({
      method: 'POST',
      path: 'public/api/v1/billing/access-token/resolve',
      body: {
        token,
      } as ResolveBillingAccessTokenRequest,
    })
  },
}
