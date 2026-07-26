import { defineConfig, loadEnv, type Plugin } from 'vite'
import type { IncomingMessage, ServerResponse } from 'node:http'
import { reactRouter } from '@react-router/dev/vite'
import path from 'node:path'

const BFF_ENV_KEYS = [
  'GATEWAY_BASE_URL',
  'PROVISIONER_API_USER',
  'PROVISIONER_API_PASS',
] as const

async function readJsonBody(req: IncomingMessage): Promise<unknown> {
  const chunks: Buffer[] = []
  for await (const chunk of req) chunks.push(chunk as Buffer)
  const raw = Buffer.concat(chunks).toString()
  return raw ? JSON.parse(raw) : undefined
}

function sendJson(res: ServerResponse, status: number, body: unknown): void {
  res.statusCode = status
  res.setHeader('content-type', 'application/json')
  res.end(JSON.stringify(body))
}

/**
 * Dev-only: serves the BFF (`/api/*`) inside the Vite dev server, so `pnpm dev`
 * behaves like production without `vercel dev`. Production is handled by the
 * Vercel function in `api/[...path].ts`.
 */
function bffDev(env: Record<string, string>): Plugin {
  return {
    name: 'bff-dev',
    apply: 'serve',
    configureServer(server) {
      for (const key of BFF_ENV_KEYS) process.env[key] = env[key]

      server.middlewares.use(async (req, res, next) => {
        if (!req.url?.startsWith('/api/')) return next()

        try {
          const { handleGatewayRequest } =
            await server.ssrLoadModule('@verno/lib/handle')

          const result = await handleGatewayRequest({
            req: {
              method: req.method ?? 'GET',
              path: req.url.replace(/^\/api\//, '').split('?')[0],
              body: await readJsonBody(req),
            },
          })

          if (result.contentType) res.setHeader('content-type', result.contentType)
          res.statusCode = result.status
          res.end(result.body)
        } catch (err) {
          console.error('[bff-dev] proxy failed:', err)
          sendJson(res, 502, {
            message: 'BFF dev error',
            detail: String((err as Error)?.message ?? err),
          })
        }
      })
    },
  }
}

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, __dirname, '')

  return {
    plugins: [reactRouter(), bffDev(env)],
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src'),
        '@verno': path.resolve(__dirname, '../../'),
      },
    },
    server: { port: 5173, strictPort: true },
    build: { sourcemap: false },
  }
})
