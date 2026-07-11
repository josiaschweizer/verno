import { createApiClient } from '@verno/lib/apiClient'
import type { StartBillingSessionRequest } from '@/type/api/billingsession/StartBillingSessionRequest.ts'
import type { StartBillingSessionResponse } from '@/type/api/billingsession/StartBillingSessionResponse.ts'

const env = (import.meta as any).env as any

const baseUrl =
  (env.VITE_API_BASE_URL as string | undefined) || 'http://localhost:8082'

const client = createApiClient({ baseUrl })

export const billingSessionApi = {
  async startSession(token: string) {
    console.log(token)
    return await client.request<StartBillingSessionResponse>({
      method: 'POST',
      path: '/public/api/v1/billing/session/start',
      body: {
        token,
      } as StartBillingSessionRequest,
    })
  },
}
