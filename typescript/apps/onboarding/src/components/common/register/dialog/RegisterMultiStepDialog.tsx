import { useEffect, useMemo, useRef, useState } from 'react'
import {
  Dialog,
  DialogBackdrop,
  DialogPanel,
  DialogTitle,
} from '@headlessui/react'
import { Button } from '@verno/components/ui/button'
import StepOne from '../steps/StepOne'
import StepTwo from '../steps/StepTwo'
import StepThree from '../steps/StepThree'
import ErrorDisplay from './ErrorDisplay'
import {
  ArrowLeftIcon,
  ArrowRightIcon,
  CircleSlash,
  FlagIcon,
} from 'lucide-react'
import RegisterDialogFormData from '@/interfaces/register/RegisterDialogFormData'
import { useForm, useWatch } from 'react-hook-form'
import { tenantsApi } from '@/lib/api/tenantsApi'
import { ApiError } from '@verno/lib/apiClient'
import resolveUsername from '@/components/common/register/steps/resolveUsername'
import { toast } from 'sonner'
import { useTranslation } from 'react-i18next'

interface Props {
  open: boolean
  onClose: () => void
}

type SubmitErrorInfo = {
  title: string
  message?: string
  details?: string[]
}

export default function RegisterMultiStepDialog({ open, onClose }: Props) {
  const { t } = useTranslation('register')
  const [step, setStep] = useState<number>(0)
  const dialogContentRef = useRef<HTMLDivElement>(null)

  const [submitting, setSubmitting] = useState(false)
  const [submitPhase, setSubmitPhase] = useState<number | null>(null)
  const [submitError, setSubmitError] = useState<SubmitErrorInfo | null>(null)
  const [validatingNext, setValidatingNext] = useState(false)

  const phases = useMemo(
    () =>
      [
        {
          label: t('dialog.phases.creatingTenant.label'),
          sub: t('dialog.phases.creatingTenant.sub'),
        },
        {
          label: t('dialog.phases.provisioning.label'),
          sub: t('dialog.phases.provisioning.sub'),
        },
        {
          label: t('dialog.phases.almostThere.label'),
          sub: t('dialog.phases.almostThere.sub'),
        },
        {
          label: t('dialog.phases.finalizing.label'),
          sub: t('dialog.phases.finalizing.sub'),
        },
      ] as const,
    [t],
  )

  const { control, handleSubmit, getValues, trigger, reset, formState } =
    useForm<RegisterDialogFormData>({
      mode: 'onChange',
      reValidateMode: 'onChange',
      defaultValues: {
        firstname: '',
        lastname: '',
        email: '',
        phone: '',
        preferredLanguage: {
          label: t('stepOne.languages.de'),
          value: 'de',
        },
        password: '',
        confirmPassword: '',
        tenantName: '',
        tenantSubdomain: '',
        tenantKey: '',
      },
    })

  const { errors } = formState

  const watchedEmail = useWatch({ control, name: 'email' })
  const watchedPassword = useWatch({ control, name: 'password' })
  const watchedPasswordConfirm = useWatch({ control, name: 'confirmPassword' })
  const watchedTenantName = useWatch({ control, name: 'tenantName' })
  const watchedTenantSubdomain = useWatch({ control, name: 'tenantSubdomain' })

  useEffect(() => {
    if (open) {
      setStep(0)
      setSubmitError(null)
      setSubmitPhase(null)
    } else {
      reset()
    }
  }, [open, reset])

  const next = async () => {
    setValidatingNext(true)
    try {
      let ok = true

      if (step === 0) {
        ok = await trigger(['email', 'password', 'confirmPassword'])
      } else if (step === 1) {
        ok = await trigger(['tenantName', 'tenantSubdomain'])
      }

      if (ok) setStep((s) => Math.min(2, s + 1))
    } finally {
      setValidatingNext(false)
    }
  }

  const back = () => {
    setSubmitError(null)
    setStep((s) => Math.max(0, s - 1))
  }

  const resolveSubmitError = (e: unknown): SubmitErrorInfo => {
    if (e instanceof ApiError) {
      const status = e.status
      const payload = e.details as any
      const code = payload?.code
      const message = payload?.message ?? e.message
      const details = payload?.details

      if (code === 'TENANT_ALREADY_EXISTS') {
        return {
          title: t('dialog.errors.tenantAlreadyExists.title'),
          message: t('dialog.errors.tenantAlreadyExists.message'),
        }
      }

      if (code === 'VALIDATION_FAILED') {
        return {
          title: t('dialog.errors.validationFailed.title'),
          message: t('dialog.errors.validationFailed.message'),
          details: Array.isArray(details) ? details : undefined,
        }
      }

      if (code === 'DATA_INTEGRITY_VIOLATION') {
        return {
          title: t('dialog.errors.dataIntegrityViolation.title'),
          message: t('dialog.errors.dataIntegrityViolation.message'),
          details: details ? [details] : message ? [message] : undefined,
        }
      }

      if (code === 'TENANT_PROVISION_FAILED') {
        return {
          title: t('dialog.errors.tenantProvisionFailed.title'),
          message: message ?? t('dialog.errors.tenantProvisionFailed.message'),
        }
      }

      return {
        title: status
          ? `${t('dialog.errors.genericTitle')} ${status}`
          : t('dialog.errors.genericTitle'),
        message: message ?? t('dialog.errors.unknown'),
        details: details
          ? Array.isArray(details)
            ? details
            : [details]
          : undefined,
      }
    }

    if (e instanceof Error) {
      return {
        title: t('dialog.errors.genericTitle'),
        message: e.message,
      }
    }

    return {
      title: t('dialog.errors.genericTitle'),
      message: t('dialog.errors.unknown'),
    }
  }

  const onSubmit = handleSubmit(async (form) => {
    try {
      setSubmitting(true)
      setSubmitError(null)
      setSubmitPhase(0)

      const preferredLanguage =
        typeof form.preferredLanguage === 'string'
          ? form.preferredLanguage
          : (form.preferredLanguage?.value ?? '')

      const tenantKey = form.tenantKey?.trim() || form.tenantSubdomain.trim()
      const subdomain = form.tenantSubdomain.trim()

      await tenantsApi.createTenant({
        tenantKey,
        tenantName: form.tenantName,
        subdomain,
        preferredLanguage,
        adminUsername: resolveUsername(form),
        adminFirstname: form.firstname,
        adminLastname: form.lastname,
        adminEmail: form.email,
        adminDisplayName: `${form.firstname} ${form.lastname}`.trim(),
        adminPassword: form.password,
      })

      setSubmitPhase(1)
      await new Promise((r) => setTimeout(r, 700))
      setSubmitPhase(2)
      await new Promise((r) => setTimeout(r, 700))
      setSubmitPhase(3)
      await new Promise((r) => setTimeout(r, 700))

      toast.success(t('dialog.toast.successTitle'), {
        duration: 10000,
        description: (
          <span className="flex flex-col gap-1">
            <span>{t('dialog.toast.successDescription')}</span>
            <a
              href={`https://${subdomain}.verno-app.ch`}
              target="_blank"
              rel="noopener noreferrer"
              className="font-medium underline"
            >
              {t('dialog.toast.openNow')}
            </a>
          </span>
        ),
      })

      onClose()
    } catch (e) {
      setSubmitPhase(null)
      setSubmitError(resolveSubmitError(e))
    } finally {
      setSubmitting(false)
    }
  })

  let canContinue = true
  if (step === 0) {
    canContinue =
      Boolean(watchedEmail && watchedPassword && watchedPasswordConfirm) &&
      !errors.email &&
      !errors.password &&
      !errors.confirmPassword
  } else if (step === 1) {
    canContinue =
      Boolean(watchedTenantName && watchedTenantSubdomain) &&
      !errors.tenantName &&
      !errors.tenantSubdomain
  }

  const isAnimating = submitting && submitPhase !== null

  return (
    <Dialog
      open={open}
      onClose={isAnimating ? () => {} : onClose}
      className="relative z-[100]"
    >
      <DialogBackdrop className="fixed inset-0 z-[100] bg-black/60" />

      <div className="fixed inset-0 z-[101] overflow-y-auto overscroll-contain">
        <div className="flex min-h-full items-start justify-center p-2 sm:p-4 md:items-center">
          <DialogPanel className="relative flex w-full max-w-2xl flex-col overflow-hidden rounded-2xl bg-verno-surface text-verno-dark shadow-xl max-h-[calc(100dvh-1rem)] sm:max-h-[calc(100dvh-2rem)]">
            <div className="shrink-0 border-b border-verno-darker/10 px-4 py-4 sm:px-6 sm:py-5">
              <div className="flex items-start justify-between gap-4">
                <div className="min-w-0">
                  <DialogTitle className="text-lg font-semibold">
                    {t('dialog.title')}
                  </DialogTitle>
                  <p className="mt-1 text-sm text-verno-darker/80">
                    {t('dialog.subtitle')}
                  </p>
                </div>

                <div className="ml-auto flex shrink-0 items-center gap-2">
                  <div className="flex items-center gap-1 p-1 sm:p-2">
                    {[0, 1, 2].map((i) => (
                      <span
                        key={i}
                        className={`h-2 w-6 rounded-full transition-colors sm:w-8 ${
                          i === step ? 'bg-verno-accent' : 'bg-verno-darker/30'
                        }`}
                        aria-hidden
                      />
                    ))}
                  </div>
                </div>
              </div>
            </div>

            <div
              ref={dialogContentRef}
              className="min-h-0 flex-1 overflow-y-auto px-4 py-4 sm:px-6 sm:py-6"
            >
              {step === 0 && (
                <StepOne
                  control={control}
                  getValues={getValues}
                  readOnly={!open}
                  portalContainerRef={dialogContentRef}
                />
              )}

              {step === 1 && <StepTwo control={control} readOnly={!open} />}

              {step === 2 && !isAnimating && (
                <StepThree getValues={getValues} />
              )}

              {step === 2 && isAnimating && submitPhase !== null && (
                <div className="flex flex-col items-center justify-center gap-6 py-8 sm:py-10">
                  <div className="h-12 w-12 animate-spin rounded-full border-2 border-verno-accent border-t-transparent" />

                  <div className="text-center">
                    <p className="text-base font-medium">
                      {phases[submitPhase].label}
                    </p>
                    <p className="mt-1 text-sm text-verno-dark/60">
                      {phases[submitPhase].sub}
                    </p>
                  </div>

                  <div className="flex w-full max-w-xs flex-col gap-2.5">
                    {phases.map((phase, i) => (
                      <div
                        key={i}
                        className={`flex items-center gap-3 text-sm transition-colors duration-300 ${
                          i < submitPhase
                            ? 'text-verno-dark/40'
                            : i === submitPhase
                              ? 'font-medium text-verno-dark'
                              : 'text-verno-dark/20'
                        }`}
                      >
                        <span
                          className={`flex h-5 w-5 shrink-0 items-center justify-center rounded-full border text-xs transition-all duration-300 ${
                            i < submitPhase
                              ? 'border-verno-accent bg-verno-accent text-white'
                              : i === submitPhase
                                ? 'border-verno-accent bg-verno-accent/15'
                                : 'border-verno-dark/20'
                          }`}
                        >
                          {i < submitPhase ? '✓' : null}
                        </span>
                        {phase.label.replace('...', '')}
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {step === 2 && !isAnimating && submitError && (
                <div className="mt-4">
                  <ErrorDisplay
                    title={submitError.title}
                    message={submitError.message}
                    details={submitError.details}
                    onDismiss={() => setSubmitError(null)}
                  />
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
                <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
                  <Button
                    variant="outline"
                    onClick={back}
                    disabled={step === 0 || isAnimating}
                    className="w-full sm:w-auto"
                  >
                    <ArrowLeftIcon className="h-5 w-5" />
                    {t('dialog.buttons.back')}
                  </Button>

                  <Button
                    variant="outline"
                    onClick={onClose}
                    disabled={isAnimating}
                    className="w-full sm:w-auto"
                  >
                    <CircleSlash className="h-5 w-5" />
                    {t('dialog.buttons.cancel')}
                  </Button>
                </div>

                <div className="flex w-full sm:w-auto">
                  {step < 2 ? (
                    <Button
                      onClick={next}
                      disabled={validatingNext || !canContinue}
                      className="w-full sm:w-auto"
                    >
                      {validatingNext
                        ? t('dialog.buttons.validating')
                        : t('dialog.buttons.continue')}
                      <ArrowRightIcon className="h-5 w-5" />
                    </Button>
                  ) : (
                    <Button
                      onClick={onSubmit}
                      disabled={submitting}
                      className="w-full sm:w-auto"
                    >
                      {isAnimating && submitPhase !== null
                        ? phases[submitPhase].label
                        : t('dialog.buttons.finish')}
                      <FlagIcon className="h-5 w-5" />
                    </Button>
                  )}
                </div>
              </div>
            </div>
          </DialogPanel>
        </div>
      </div>
    </Dialog>
  )
}
