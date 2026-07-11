export type ApiErrorPayload = {
  status: number
  code?: string
  message: string
  details?: unknown
}

export class ApiError extends Error {
  status: number
  code?: string
  details?: unknown

  constructor(payload: ApiErrorPayload) {
    super(payload.message)
    this.name = 'ApiError'
    this.status = payload.status
    this.code = payload.code
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

export function createApiClient(config: { baseUrl: string; credentials?: RequestCredentials }) {
  const baseUrl = config.baseUrl.replace(/\/+$/, '')

  async function request<T>(opts: RequestOptions): Promise<T> {
    const url = `${baseUrl}${opts.path.startsWith('/') ? '' : '/'}${opts.path}`

    const headers: Record<string, string> = {
      Accept: 'application/json',
      ...(opts.body != null ? { 'Content-Type': 'application/json' } : {}),
      ...(opts.headers ?? {}),
    }

    const res = await fetch(url, {
      method: opts.method ?? 'GET',
      headers,
      body: opts.body != null ? JSON.stringify(opts.body) : undefined,
      credentials: config.credentials ?? 'same-origin',
      signal: opts.signal,
    })

    const contentType = res.headers.get('content-type') ?? ''
    const isJson = contentType.includes('application/json')

    let payload: any
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
      const msg = payload?.message || payload?.error || res.statusText || 'Request failed'

      throw new ApiError({
        status: res.status,
        code: payload?.code,
        message: msg,
        details: payload,
      })
    }

    return payload as T
  }

  return { request }
}
