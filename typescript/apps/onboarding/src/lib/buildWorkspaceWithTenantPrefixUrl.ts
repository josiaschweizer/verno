const env = (import.meta as any).env as any

const workspaceBaseUrl: string =
  env.VITE_WORKSPACE_BASE_URL || 'http://localhost:8080'

export function buildWorkspaceWithTenantPrefixUrl(subdomain: string): string {
  const trimmed = workspaceBaseUrl.replace(/\/+$/, '')

  try {
    const url = new URL(trimmed)
    url.hostname = `${subdomain}.${url.hostname}`
    return url.toString().replace(/\/+$/, '')
  } catch {
    return `${trimmed}/${subdomain}`
  }
}
