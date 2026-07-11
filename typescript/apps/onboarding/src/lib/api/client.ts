import { createApiClient } from '@verno/lib/apiClient'

const env = (import.meta as any).env as any

// Same-origin BFF (Vercel function). It holds the gateway credential server-side.
// Override only for local `vercel dev`.
const baseUrl = (env.VITE_BFF_BASE_URL as string | undefined) || ''

export const api = createApiClient({ baseUrl })
