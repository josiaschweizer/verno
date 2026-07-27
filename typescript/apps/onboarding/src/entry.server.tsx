import { PassThrough } from 'node:stream'
import { createReadableStreamFromReadable } from '@react-router/node'
import type { EntryContext, RouterContextProvider } from 'react-router'
import { ServerRouter } from 'react-router'
import { renderToPipeableStream } from 'react-dom/server'

export const streamTimeout = 5_000

export default function handleRequest(
  request: Request,
  responseStatusCode: number,
  responseHeaders: Headers,
  routerContext: EntryContext,
  _loadContext: RouterContextProvider,
) {
  if (request.method.toUpperCase() === 'HEAD') {
    return new Response(null, {
      status: responseStatusCode,
      headers: responseHeaders,
    })
  }

  return new Promise<Response>((resolve, reject) => {
    let shellRendered = false
    let timeoutId: ReturnType<typeof setTimeout> | undefined

    const { pipe, abort } = renderToPipeableStream(
      <ServerRouter context={routerContext} url={request.url} />,
      {
        // These routes are pre-rendered marketing pages. Waiting for all content
        // avoids deferred stream nodes being parsed outside the document shell.
        onAllReady() {
          shellRendered = true

          const body = new PassThrough({
            final(callback) {
              clearTimeout(timeoutId)
              timeoutId = undefined
              callback()
            },
          })

          responseHeaders.set('Content-Type', 'text/html')
          pipe(body)

          resolve(
            new Response(createReadableStreamFromReadable(body), {
              headers: responseHeaders,
              status: responseStatusCode,
            }),
          )
        },
        onShellError(error: unknown) {
          reject(error)
        },
        onError(error: unknown) {
          responseStatusCode = 500

          if (shellRendered) {
            console.error(error)
          }
        },
      },
    )

    timeoutId = setTimeout(() => abort(), streamTimeout + 1_000)
  })
}
