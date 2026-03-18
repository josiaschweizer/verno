import { useEffect, useRef, useState } from 'react'
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
import { PostReloadToast } from '@/types/ui/toast/PostReloadToast'
import { toast } from 'sonner'

interface Props {
  open: boolean
  onClose: () => void
}

type SubmitErrorInfo = {
  title: string
  message?: string
  details?: string[]
}

const PHASES = [
  { label: 'Creating tenant...', sub: 'Setting up your workspace' },
  { label: 'Provisioning...', sub: 'Configuring your database' },
  { label: 'Almost there...', sub: 'Setting up your subdomain' },
  {
    label: 'Finalizing...',
    sub: 'Applying last configurations',
  },
] as const

export default function RegisterMultiStepDialog({ open, onClose }: Props) {
  const [step, setStep] = useState<number>(0)
  const dialogContentRef = useRef<HTMLDivElement>(null)

  const [submitting, setSubmitting] = useState(false)
  const [submitPhase, setSubmitPhase] = useState<number | null>(null)
  const [submitError, setSubmitError] = useState<SubmitErrorInfo | null>(null)
  const [validatingNext, setValidatingNext] = useState(false)

  const { control, handleSubmit, getValues, trigger, reset, formState } =
    useForm<RegisterDialogFormData>({
      mode: 'onChange',
      reValidateMode: 'onChange',
      defaultValues: {
        firstname: '',
        lastname: '',
        email: '',
        phone: '',
        preferredLanguage: { label: 'German', value: 'de' },
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
  }, [open])

  const next = async () => {
    setValidatingNext(true)
    try {
      let ok = true

      if (step === 0) {
        ok = await trigger(['email', 'password'])
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
          title: 'Tenant key already exists',
          message:
            'The tenant key/subdomain already exists. Please choose a different value.',
        }
      }
      if (code === 'VALIDATION_FAILED') {
        return {
          title: 'Invalid input',
          message: 'Please review your input and try again.',
          details: Array.isArray(details) ? details : undefined,
        }
      }
      if (code === 'DATA_INTEGRITY_VIOLATION') {
        return {
          title: 'Conflict while saving',
          message:
            'A database constraint was violated. Please review your input and try again.',
          details: details ? [details] : message ? [message] : undefined,
        }
      }
      if (code === 'TENANT_PROVISION_FAILED') {
        return {
          title: 'Failed to create tenant',
          message:
            message ??
            'An error occurred while creating the tenant. Please try again later.',
        }
      }

      return {
        title: status ? `Error ${status}` : 'Error',
        message: message ?? 'Unknown error',
        details: details
          ? Array.isArray(details)
            ? details
            : [details]
          : undefined,
      }
    }

    if (e instanceof Error) return { title: 'Error', message: e.message }
    return { title: 'Error', message: 'Unknown error' }
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

      toast.success('Tenant successfully created!', {
        duration: 10000,
        description: (
          <span className="flex flex-col gap-1">
            <span>Your tenant is ready.</span>
            <a
              href={`https://${subdomain}.verno-app.ch`}
              target="_blank"
              rel="noopener noreferrer"
              className="underline font-medium"
            >
              {'Open now'} →
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
      !errors.password
  } else if (step === 1) {
    canContinue =
      Boolean(watchedTenantName && watchedTenantSubdomain) &&
      !errors.tenantName &&
      !errors.tenantSubdomain
  }

  const isAnimating = submitting && submitPhase !== null

  return (
    <Dialog open={open} onClose={onClose} className="relative z-100">
      <div className="fixed inset-0 z-100">
        <DialogBackdrop className="fixed inset-0 bg-black/60" />

        <div className="fixed inset-0 flex items-center justify-center p-4">
          <DialogPanel className="relative z-101 w-full max-w-2xl rounded-2xl bg-verno-surface p-6 text-verno-dark shadow-xl">
            <div className="flex items-start justify-between gap-4">
              <div>
                <DialogTitle className="text-lg font-semibold">
                  Get Started
                </DialogTitle>
                <p className="mt-1 text-sm text-verno-darker/80">
                  Follow the steps to create your tenant.
                </p>
              </div>

              <div className="ml-auto flex items-center gap-2">
                <div className="flex items-center gap-1 p-2">
                  {[0, 1, 2].map((i) => (
                    <span
                      key={i}
                      className={`h-2 w-8 rounded-full transition-colors ${
                        i === step ? 'bg-verno-accent' : 'bg-verno-darker/30'
                      }`}
                      aria-hidden
                    />
                  ))}
                </div>
              </div>
            </div>

            <div ref={dialogContentRef} className="mt-6 min-h-100">
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
              {step === 2 && isAnimating && (
                <div className="flex flex-col items-center justify-center gap-6 py-10">
                  <div className="h-12 w-12 rounded-full border-2 border-verno-accent border-t-transparent animate-spin" />
                  <div className="text-center">
                    <p className="text-base font-medium">
                      {PHASES[submitPhase!].label}
                    </p>
                    <p className="mt-1 text-sm text-verno-dark/60">
                      {PHASES[submitPhase!].sub}
                    </p>
                  </div>
                  <div className="flex w-full max-w-xs flex-col gap-2.5">
                    {PHASES.map((phase, i) => (
                      <div
                        key={i}
                        className={`flex items-center gap-3 text-sm transition-colors duration-300 ${
                          i < submitPhase!
                            ? 'text-verno-dark/40'
                            : i === submitPhase!
                              ? 'font-medium text-verno-dark'
                              : 'text-verno-dark/20'
                        }`}
                      >
                        <span
                          className={`flex h-5 w-5 shrink-0 items-center justify-center rounded-full border text-xs transition-all duration-300 ${
                            i < submitPhase!
                              ? 'border-verno-accent bg-verno-accent text-white'
                              : i === submitPhase!
                                ? 'border-verno-accent bg-verno-accent/15'
                                : 'border-verno-dark/20'
                          }`}
                        >
                          {i < submitPhase! ? '✓' : null}
                        </span>
                        {phase.label.replace('...', '')}
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>

            <div className="mt-6">
              {step === 2 && !isAnimating && submitError && (
                <div className="mb-4">
                  <ErrorDisplay
                    title={submitError.title}
                    message={submitError.message}
                    details={submitError.details}
                    onDismiss={() => setSubmitError(null)}
                  />
                </div>
              )}

              <div className="flex items-center justify-between">
                <div>
                  <Button
                    variant="outline"
                    onClick={back}
                    disabled={step === 0 || isAnimating}
                  >
                    <ArrowLeftIcon className="h-5 w-5" /> Back
                  </Button>
                  <Button
                    variant="outline"
                    className="ml-2"
                    onClick={onClose}
                    disabled={isAnimating}
                  >
                    <CircleSlash className="h-5 w-5" /> Cancel
                  </Button>
                </div>

                <div className="flex items-center gap-2">
                  {step < 2 ? (
                    <Button
                      onClick={next}
                      disabled={validatingNext || !canContinue}
                    >
                      {validatingNext ? 'Validating...' : 'Continue'}{' '}
                      <ArrowRightIcon className="h-5 w-5" />
                    </Button>
                  ) : (
                    <Button onClick={onSubmit} disabled={submitting}>
                      {isAnimating ? PHASES[submitPhase!].label : 'Finish'}{' '}
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
