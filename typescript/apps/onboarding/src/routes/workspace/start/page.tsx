'use client'

import { useMemo } from 'react'
import { useSearchParams } from 'react-router-dom'
import { LoaderCircle, Server, ArrowRight } from 'lucide-react'
import { useTranslation } from 'react-i18next'
import { Button } from '@verno/components/ui/button'

export default function WorkspaceStartPage() {
  const { t } = useTranslation('workspace')
  const [searchParams] = useSearchParams()

  const session = searchParams.get('session')
  const tenant = searchParams.get('tenant')

  const tenantUrl = useMemo(() => {
    if (!tenant) {
      return null
    }

    return `https://${tenant}.verno-app.ch`
  }, [tenant])

  const openWorkspace = () => {
    if (!tenantUrl) {
      return
    }

    window.location.href = tenantUrl
  }

  return (
    <main className="flex min-h-screen items-center justify-center bg-verno-bg px-4 py-10 text-verno-darker">
      <section className="w-full max-w-4xl overflow-hidden rounded-3xl border border-verno-darker/10 bg-verno-surface shadow-sm">
        <div className="grid grid-cols-1 md:grid-cols-[1fr_1.15fr]">
          <div className="flex flex-col justify-between border-b border-verno-darker/10 bg-verno-darker/[0.03] p-8 md:border-b-0 md:border-r md:p-10">
            <div>
              <div className="flex h-14 w-14 items-center justify-center rounded-2xl border border-verno-darker/10 bg-verno-surface">
                <LoaderCircle className="h-7 w-7 animate-spin text-verno-darker/70" />
              </div>

              <h1 className="mt-8 text-3xl font-semibold tracking-tight sm:text-4xl">
                {t('startPage.title')}
              </h1>

              <p className="mt-4 max-w-md text-base leading-7 text-verno-darker/70">
                {t('startPage.description')}
              </p>
            </div>

            <div className="mt-10 flex items-center gap-3 text-sm text-verno-darker/55">
              <Server className="h-4 w-4" />
              <span>{t('startPage.statusText')}</span>
            </div>
          </div>

          <div className="p-8 md:p-10">
            <div className="space-y-5">
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

            <div className="mt-10 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
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
                disabled={!tenantUrl}
                className="w-full sm:w-auto"
              >
                {t('startPage.buttons.openWorkspace')}
                <ArrowRight className="h-4 w-4" />
              </Button>
            </div>
          </div>
        </div>
      </section>
    </main>
  )
}
