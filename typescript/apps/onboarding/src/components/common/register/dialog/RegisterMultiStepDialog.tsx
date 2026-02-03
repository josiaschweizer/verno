import { useEffect, useRef, useState } from 'react'
import {
  Dialog,
  DialogBackdrop,
  DialogPanel,
  DialogTitle,
} from '@headlessui/react'
import { Button } from '@/components/ui/button'
import StepOne from '../steps/StepOne'
import StepTwo from '../steps/StepTwo'
import StepThree from '../steps/StepThree'
import {
  ArrowLeftIcon,
  ArrowRightIcon,
  CircleSlash,
  FlagIcon,
} from 'lucide-react'
import RegisterDialogFormData from '@/interfaces/register/RegisterDialogFormData'
import { useForm, useWatch } from 'react-hook-form'
import { tenantsApi } from '@/lib/api/tenantsApi'
import { ApiError } from '@/lib/apiClient'

interface Props {
  open: boolean
  onClose: () => void
}

export default function RegisterMultiStepDialog({ open, onClose }: Props) {
  const [step, setStep] = useState<number>(0)
  const dialogContentRef = useRef<HTMLDivElement>(null)

  const [submitting, setSubmitting] = useState(false)
  const [submitError, setSubmitError] = useState<string | null>(null)
  const [validatingNext, setValidatingNext] = useState(false)

  const { control, handleSubmit, getValues, trigger, formState } =
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
  const watchedTenantName = useWatch({ control, name: 'tenantName' })
  const watchedTenantSubdomain = useWatch({ control, name: 'tenantSubdomain' })

  useEffect(() => {
    if (open) setStep(0)
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
  const back = () => setStep((s) => Math.max(0, s - 1))

  const onSubmit = handleSubmit(async (form) => {
    try {
      setSubmitting(true)
      setSubmitError(null)

      const preferredLanguage =
        typeof form.preferredLanguage === 'string'
          ? form.preferredLanguage
          : (form.preferredLanguage?.value ?? '')

      const tenantKey = form.tenantKey?.trim() || form.tenantSubdomain.trim()
      const subdomain = form.tenantSubdomain.trim()

      const result = await tenantsApi.createTenant({
        tenantKey,
        tenantName: form.tenantName,
        subdomain,
        preferredLanguage,
        adminEmail: form.email,
        adminDisplayName: `${form.firstname} ${form.lastname}`.trim(),
        adminPassword: form.password,
      })

      console.debug('createTenant response', result)

      onClose()
      window.location.reload()
    } catch (e) {
      if (e instanceof ApiError) {
        setSubmitError(`${e.status}: ${e.message}`)
      } else if (e instanceof Error) {
        setSubmitError(e.message)
      } else {
        setSubmitError('Unknown error')
      }
    } finally {
      setSubmitting(false)
    }
  })

  let canContinue = true
  if (step === 0) {
    canContinue =
      Boolean(watchedEmail && watchedPassword) &&
      !errors.email &&
      !errors.password
  } else if (step === 1) {
    canContinue =
      Boolean(watchedTenantName && watchedTenantSubdomain) &&
      !errors.tenantName &&
      !errors.tenantSubdomain
  }

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
              {step === 2 && <StepThree getValues={getValues} />}
            </div>

            <div className="mt-6 flex items-center justify-between">
              <div>
                <Button variant="outline" onClick={back} disabled={step === 0}>
                  <ArrowLeftIcon className="h-5 w-5" /> Back
                </Button>
                <Button variant="outline" className="ml-2" onClick={onClose}>
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
                  <div className="flex items-center gap-3">
                    {submitError && (
                      <p className="text-sm text-red-500">{submitError}</p>
                    )}
                    <Button onClick={onSubmit} disabled={submitting}>
                      {submitting ? 'Creating...' : 'Finish'}{' '}
                      <FlagIcon className="h-5 w-5" />
                    </Button>
                  </div>
                )}
              </div>
            </div>
          </DialogPanel>
        </div>
      </div>
    </Dialog>
  )
}
