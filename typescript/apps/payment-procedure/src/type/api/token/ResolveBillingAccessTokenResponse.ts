export type ResolveBillingAccessTokenResponse = {
  tenantId: number
  userId: number
  purpose: string
  expiresAt: string | null
}
