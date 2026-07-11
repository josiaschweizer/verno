import type { VercelRequest, VercelResponse } from '@vercel/node'
import { handleGatewayRequest } from '@verno/lib/handle'
import { ProxyRequest } from '@verno/lib/type/ProxyRequest'

export default async function handler(
  req: VercelRequest,
  res: VercelResponse,
): Promise<void> {
  try {
    const proxyRequst: ProxyRequest = {
      method: req.method ?? 'GET',
      path: getRequestPath(req),
      body: req.body,
    }

    const result = await handleGatewayRequest({
      req: proxyRequst,
    })

    if (result.contentType) {
      res.setHeader('content-type', result.contentType)
    }

    res.status(result.status).send(result.body)
  } catch {
    res.status(500).json({
      message: 'Gateway proxy error',
    })
  }
}

function getRequestPath(req: VercelRequest): string {
  const { path } = req.query

  if (Array.isArray(path)) {
    return path.join('/')
  }

  return path ?? ''
}
