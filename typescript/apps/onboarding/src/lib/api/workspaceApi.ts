import { createApiClient } from '@verno/lib/apiClient'
import { fetchEventSource } from '@microsoft/fetch-event-source'

export type StartWorkspaceRequest = {
  tenantName: string
}

export type StartWorkspaceResponse = {
  tenantSlug: string
  tenantName: string
  startSessionId: string
}

export type WorkspaceStartStatus = 'STARTING' | 'READY' | 'FAILED' | 'EXPIRED'

export type WorkspaceStartStatusEvent = {
  startSessionId: string
  tenantName: string
  tenantSlug: string
  redirectUrl: string
  status: WorkspaceStartStatus
  message?: string
}

type SubscribeWorkspaceStartStatusOptions = {
  startSessionId: string
  signal: AbortSignal
  onStatus: (event: WorkspaceStartStatusEvent) => void
  onError: (error: unknown) => void
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
      path: '/api/v1/workspace/start',
      body: req,
    })
  },

  async subscribeWorkspaceStartStatus({
    startSessionId,
    signal,
    onStatus,
    onError,
  }: SubscribeWorkspaceStartStatusOptions) {
    await fetchEventSource(
      `${baseUrl}/api/v1/workspace/start/${encodeURIComponent(startSessionId)}/events`,
      {
        method: 'GET',
        signal,
        headers: {
          Authorization: `Basic ${btoa(`${apiUser}:${apiPass}`)}`,
          Accept: 'text/event-stream',
        },
        async onopen(response) {
          const contentType = response.headers.get('content-type')

          if (response.ok && contentType?.includes('text/event-stream')) {
            return
          }

          throw new Error(
            `SSE connection failed. Status: ${response.status}, Content-Type: ${contentType}`,
          )
        },
        onmessage(event) {
          if (event.event !== 'workspace-status') {
            return
          }

          onStatus(JSON.parse(event.data) as WorkspaceStartStatusEvent)
        },
        onerror(error) {
          onError(error)
          throw error
        },
      },
    )
  },
}
