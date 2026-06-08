'use client'

import { useEffect, useMemo, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import {
  ArrowRight,
  CircleCheck,
  CircleX,
  LoaderCircle,
  Server,
} from 'lucide-react'
import { useTranslation } from 'react-i18next'
import { Button } from '@verno/components/ui/button'
import { workspaceApi, type WorkspaceStartStatus } from '@/lib/api/workspaceApi'

export default function WorkspaceStartPage() {
  const { t } = useTranslation('workspace')
  const [searchParams] = useSearchParams()

  const session = searchParams.get('session')
  const tenant = searchParams.get('tenant')

  const [status, setStatus] = useState<WorkspaceStartStatus>('STARTING')
  const [statusMessage, setStatusMessage] = useState<string | null>(null)

  const tenantUrl = useMemo(() => {
    if (!tenant) {
      return null
    }

    return `https://${tenant}.verno-app.ch`
  }, [tenant])

  useEffect(() => {
    if (!session) {
      return
    }

    const controller = new AbortController()

    workspaceApi.subscribeWorkspaceStartStatus({
      startSessionId: session,
      signal: controller.signal,
      onStatus: (event) => {
        setStatus(event.status)
        setStatusMessage(event.message ?? null)

        if (event.status === 'READY') {
          controller.abort()
          window.location.href = event.redirectUrl
          return
        }

        if (event.status === 'FAILED' || event.status === 'EXPIRED') {
          controller.abort()
        }
      },
      onError: () => {
        controller.abort()
        setStatus('FAILED')
        setStatusMessage(t('startPage.errors.statusFailed.message'))
      },
    })

    return () => {
      controller.abort()
    }
  }, [session, t])

  const openWorkspace = () => {
    if (!tenantUrl) {
      return
    }

    window.location.href = tenantUrl
  }

  const statusIcon = (() => {
    if (status === 'READY') {
      return <CircleCheck className="h-7 w-7 text-verno-darker/70" />
    }

    if (status === 'FAILED' || status === 'EXPIRED') {
      return <CircleX className="h-7 w-7 text-red-600" />
    }

    return (
      <LoaderCircle className="h-7 w-7 animate-spin text-verno-darker/70" />
    )
  })()

  const title = (() => {
    if (status === 'READY') {
      return t('startPage.readyTitle')
    }

    if (status === 'FAILED' || status === 'EXPIRED') {
      return t('startPage.failedTitle')
    }

    return t('startPage.title')
  })()

  const statusText = (() => {
    if (status === 'READY') {
      return t('startPage.readyStatusText')
    }

    if (status === 'FAILED' || status === 'EXPIRED') {
      return t('startPage.failedStatusText')
    }

    return t('startPage.statusText')
  })()

  return (
    <main className="flex min-h-screen items-center justify-center bg-verno-bg px-4 py-10 text-verno-darker">
      <section className="w-full max-w-6xl overflow-hidden rounded-3xl border border-verno-darker/10 bg-verno-surface shadow-sm">
        <div className="grid grid-cols-1 lg:grid-cols-[1fr_1fr]">
          <div className="flex min-w-0 flex-col justify-between border-b border-verno-darker/10 bg-verno-darker/[0.03] p-8 md:p-10 lg:border-b-0 lg:border-r lg:p-12">
            <div>
              <div className="flex h-14 w-14 items-center justify-center rounded-2xl border border-verno-darker/10 bg-verno-surface">
                {statusIcon}
              </div>

              <h1 className="mt-8 max-w-lg text-3xl font-semibold tracking-tight sm:text-4xl lg:text-5xl">
                {title}
              </h1>

              <p className="mt-5 max-w-lg text-base leading-7 text-verno-darker/70">
                {statusMessage ?? t('startPage.description')}
              </p>
            </div>

            <div className="mt-12 flex items-center gap-3 text-sm text-verno-darker/55">
              <Server className="h-4 w-4" />
              <span>{statusText}</span>
            </div>
          </div>

          <div className="min-w-0 p-8 md:p-10 lg:p-12">
            <div className="space-y-6">
              <div>
                <p className="text-sm font-medium text-verno-darker/55">
                  {t('startPage.tenantLabel')}
                </p>

                <div className="mt-2 rounded-2xl border border-verno-darker/10 bg-verno-bg px-5 py-4">
                  <p className="break-all text-lg font-medium text-verno-darker">
                    {tenant || t('startPage.emptyValue')}
                  </p>
                </div>
              </div>

              {session && (
                <div>
                  <p className="text-sm font-medium text-verno-darker/55">
                    {t('startPage.sessionLabel')}
                  </p>

                  <div className="mt-2 rounded-2xl border border-verno-darker/10 bg-verno-bg px-5 py-4">
                    <p className="break-all font-mono text-sm text-verno-darker/70">
                      {session}
                    </p>
                  </div>
                </div>
              )}

              <div>
                <p className="text-sm font-medium text-verno-darker/55">
                  {t('startPage.statusLabel')}
                </p>

                <div className="mt-2 rounded-2xl border border-verno-darker/10 bg-verno-bg px-5 py-4">
                  <p className="text-sm font-medium text-verno-darker">
                    {t(`startPage.status.${status}`)}
                  </p>
                </div>
              </div>
            </div>

            {!tenant && (
              <div className="mt-6 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
                <p className="font-medium">
                  {t('startPage.errors.missingTenant.title')}
                </p>
                <p className="mt-1">
                  {t('startPage.errors.missingTenant.message')}
                </p>
              </div>
            )}

            <div className="mt-12 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
              <Button
                type="button"
                variant="outline"
                onClick={() => {
                  window.location.href = '/'
                }}
                className="w-full sm:w-auto"
              >
                {t('startPage.buttons.backHome')}
              </Button>

              <Button
                type="button"
                onClick={openWorkspace}
                disabled={!tenantUrl || status === 'STARTING'}
                className="w-full whitespace-nowrap sm:w-auto"
              >
                {status === 'STARTING'
                  ? t('startPage.buttons.waiting')
                  : t('startPage.buttons.openWorkspace')}

                <ArrowRight className="h-4 w-4" />
              </Button>
            </div>
          </div>
        </div>
      </section>
    </main>
  )
}
