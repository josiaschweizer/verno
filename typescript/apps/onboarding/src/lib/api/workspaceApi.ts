export type WorkspaceStartStatus = 'STARTING' | 'READY' | 'FAILED' | 'EXPIRED'

const env = (import.meta as any).env as any

const workspaceBaseUrl =
  (env.VITE_WORKSPACE_BASE_URL as string | undefined) || 'http://localhost:8080'

export const workspaceApi = {
  normalizeTenantName(tenantName: string) {
    return tenantName.trim().toLowerCase()
  },

  buildWorkspaceUrl(tenantName: string) {
    const normalizedTenantName = this.normalizeTenantName(tenantName)
    const url = new URL(workspaceBaseUrl)

    return `${url.protocol}//${normalizedTenantName}.${url.host}`
  },

  async pingWorkspace(workspaceUrl: string, timeoutMs = 5000) {
    const controller = new AbortController()

    const timeoutId = window.setTimeout(() => {
      controller.abort()
    }, timeoutMs)

    try {
      await fetch(workspaceUrl, {
        method: 'GET',
        mode: 'no-cors',
        cache: 'no-store',
        signal: controller.signal,
      })

      return true
    } catch {
      return false
    } finally {
      window.clearTimeout(timeoutId)
    }
  },
}
