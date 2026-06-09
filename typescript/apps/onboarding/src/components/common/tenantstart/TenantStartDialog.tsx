'use client'

import { useEffect, useState } from 'react'
import {
  Dialog,
  DialogBackdrop,
  DialogPanel,
  DialogTitle,
} from '@headlessui/react'
import { Button } from '@verno/components/ui/button'
import { Controller, useForm } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import { CircleSlash, DoorOpenIcon } from 'lucide-react'
import { InputField } from '@/components/ui/custom/InputField'
import { workspaceApi } from '@/lib/api/workspaceApi'

interface Props {
  open: boolean
  onClose: () => void
}

type StartWorkspaceFormData = {
  tenantName: string
}

type SubmitErrorInfo = {
  title: string
  message?: string
}

export default function StartWorkspaceDialog({ open, onClose }: Props) {
  const { t } = useTranslation('workspace')

  const [submitting, setSubmitting] = useState(false)
  const [submitError, setSubmitError] = useState<SubmitErrorInfo | null>(null)

  const {
    control,
    handleSubmit,
    reset,
    formState: { isValid },
  } = useForm<StartWorkspaceFormData>({
    mode: 'onChange',
    reValidateMode: 'onChange',
    defaultValues: {
      tenantName: '',
    },
  })

  useEffect(() => {
    if (open) {
      setSubmitError(null)
    } else {
      reset()
    }
  }, [open, reset])

  const onSubmit = handleSubmit((form) => {
    setSubmitting(true)
    setSubmitError(null)

    const tenantName = workspaceApi.normalizeTenantName(form.tenantName)

    if (!tenantName) {
      setSubmitError({
        title: t('dialog.errors.tenantNotFound.title'),
        message: t('dialog.errors.tenantNotFound.message'),
      })

      setSubmitting(false)
      return
    }

    window.location.href = `/workspace-starting?tenant=${encodeURIComponent(tenantName)}`
  })

  return (
    <Dialog
      open={open}
      onClose={submitting ? () => {} : onClose}
      className="relative z-[100]"
    >
      <DialogBackdrop className="fixed inset-0 z-[100] bg-black/60" />

      <div className="fixed inset-0 z-[101] overflow-y-auto overscroll-contain">
        <div className="flex min-h-full items-start justify-center p-2 sm:p-4 md:items-center">
          <DialogPanel className="relative flex w-full max-w-md flex-col overflow-hidden rounded-2xl bg-verno-surface text-verno-dark shadow-xl max-h-[calc(100dvh-1rem)] sm:max-h-[calc(100dvh-2rem)]">
            <div className="shrink-0 border-b border-verno-darker/10 px-4 py-4 sm:px-6 sm:py-5">
              <div className="min-w-0">
                <DialogTitle className="text-lg font-semibold">
                  {t('dialog.title')}
                </DialogTitle>

                <p className="mt-1 text-sm text-verno-darker/80">
                  {t('dialog.subtitle')}
                </p>
              </div>
            </div>

            <form onSubmit={onSubmit} className="min-h-0 flex-1">
              <div className="px-4 py-4 sm:px-6 sm:py-6">
                <Controller
                  name="tenantName"
                  control={control}
                  rules={{
                    required: t('fields.tenantName.errors.required'),
                  }}
                  render={({ field, fieldState }) => (
                    <div className="w-full">
                      <InputField
                        fieldLabel={t('fields.tenantName.label')}
                        placeholder={t('fields.tenantName.placeholder')}
                        {...field}
                        disabled={submitting}
                        className="w-full"
                        required
                      />

                      {fieldState.error && (
                        <p className="mt-1.5 text-sm text-red-500">
                          {fieldState.error.message}
                        </p>
                      )}
                    </div>
                  )}
                />

                {submitError && (
                  <div className="mt-4 rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">
                    <p className="font-medium">{submitError.title}</p>

                    {submitError.message && (
                      <p className="mt-1">{submitError.message}</p>
                    )}
                  </div>
                )}
              </div>

              <div
                className="shrink-0 border-t border-verno-darker/10 px-4 py-4 sm:px-6"
                style={{
                  paddingBottom: 'max(1rem, env(safe-area-inset-bottom))',
                }}
              >
                <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                  <Button
                    type="button"
                    variant="outline"
                    onClick={onClose}
                    disabled={submitting}
                    className="w-full sm:w-auto"
                  >
                    <CircleSlash className="h-5 w-5" />
                    {t('dialog.buttons.cancel')}
                  </Button>

                  <Button
                    type="submit"
                    disabled={submitting || !isValid}
                    className="w-full sm:w-auto"
                  >
                    {submitting
                      ? t('dialog.buttons.submitting')
                      : t('dialog.buttons.open')}

                    <DoorOpenIcon className="h-5 w-5" />
                  </Button>
                </div>
              </div>
            </form>
          </DialogPanel>
        </div>
      </div>
    </Dialog>
  )
}
