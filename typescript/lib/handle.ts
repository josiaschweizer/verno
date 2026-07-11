import type { ProxyRequest } from '@verno/lib/type/ProxyRequest'
import type { ProxyResponse } from '@verno/lib/type/ProxyResponse'
import { routes } from '@verno/lib/routes/gatewayRoutes'

interface Props {
  req: ProxyRequest
}

export async function handleGatewayRequest({ req }: Props): Promise<ProxyResponse> {
  const route = routes.find(({ method, path }) => method === req.method && path.test(req.path))

  if (!route) {
    return json(404, {
      message: 'Not found',
    })
  }

  const pathMatch = req.path.match(route.path)

  if (!pathMatch) {
    return json(404, {
      message: 'Not found',
    })
  }

  const hasRequestBody = req.body != null && req.method !== 'GET' && req.method !== 'HEAD'

  const targetUrl = `${gatewayUrl()}${route.target(pathMatch)}`
  const upstreamResponse = await fetch(targetUrl, {
    method: req.method,
    headers: {
      Accept: 'application/json',
      Authorization: gatewayCredential(),
      ...(hasRequestBody && {
        'Content-Type': 'application/json',
      }),
    },
    body: hasRequestBody ? JSON.stringify(req.body) : undefined,
  })

  return {
    status: upstreamResponse.status,
    contentType: upstreamResponse.headers.get('content-type') ?? undefined,
    body: await upstreamResponse.text(),
  }
}

function gatewayUrl(): string {
  return process.env.GATEWAY_BASE_URL ?? 'http://localhost:8082'
}

function gatewayCredential(): string {
  const { PROVISIONER_API_USER: user, PROVISIONER_API_PASS: password } = process.env

  if (!user || !password) {
    throw new Error('Gateway credentials are not configured')
  }

  const credentials = Buffer.from(`${user}:${password}`).toString('base64')

  return `Basic ${credentials}`
}

function json(status: number, body: unknown): ProxyResponse {
  return {
    status,
    contentType: 'application/json',
    body: JSON.stringify(body),
  }
}
