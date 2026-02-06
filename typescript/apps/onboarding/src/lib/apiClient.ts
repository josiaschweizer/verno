export type ApiErrorPayload = {
  status: number
  message: string
  details?: unknown
}

export class ApiError extends Error {
  status: number
  details?: unknown

  constructor(payload: ApiErrorPayload) {
    super(payload.message)
    this.name = 'ApiError'
    this.status = payload.status
    this.details = payload.details
  }
}

type RequestOptions = {
  method?: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE'
  path: string
  body?: unknown
  headers?: Record<string, string>
  signal?: AbortSignal
}

function buildBasicAuthHeader(user: string, pass: string): string {
  const token = btoa(`${user}:${pass}`)
  return `Basic ${token}`
}

export function createApiClient(config: {
  baseUrl: string
  basicAuth?: { user: string; pass: string }
}) {
  const baseUrl = config.baseUrl.replace(/\/+$/, '')

  async function request<T>(opts: RequestOptions): Promise<T> {
    const url = `${baseUrl}${opts.path.startsWith('/') ? '' : '/'}${opts.path}`

    const headers: Record<string, string> = {
      Accept: 'application/json',
      ...(opts.body != null ? { 'Content-Type': 'application/json' } : {}),
      ...(opts.headers ?? {}),
    }

    if (config.basicAuth) {
      headers.Authorization = buildBasicAuthHeader(
        config.basicAuth.user,
        config.basicAuth.pass,
      )
    }

    const res = await fetch(url, {
      method: opts.method ?? 'GET',
      headers,
      body: opts.body != null ? JSON.stringify(opts.body) : undefined,
      signal: opts.signal,
    })

    const contentType = res.headers.get('content-type') ?? ''
    const isJson = contentType.includes('application/json')

    let payload: any = null
    if (isJson) {
      try {
        payload = await res.json()
      } catch {
        payload = null
      }
    } else {
      payload = null
    }

    if (!res.ok) {
      const msg =
        payload?.message || payload?.error || res.statusText || 'Request failed'

      throw new ApiError({
        status: res.status,
        message: msg,
        details: payload,
      })
    }

    return payload as T
  }

  return { request }
}
