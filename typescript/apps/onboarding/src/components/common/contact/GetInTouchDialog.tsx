import { useEffect, useRef, useState } from 'react'
import {
  Dialog,
  DialogBackdrop,
  DialogPanel,
  DialogTitle,
} from '@headlessui/react'
import { useForm } from 'react-hook-form'
import { Button } from '@verno/components/ui/button'
import { ArrowRightIcon, CircleSlash, SendIcon } from 'lucide-react'
import { toast } from 'sonner'
import { ApiError } from '@verno/lib/apiClient'
import { emailApi } from '@/lib/api/emailApi'

interface Props {
  open: boolean
  onClose: () => void
}

type GetInTouchDialogFormData = {
  firstname: string
  lastname: string
  email: string
  phone?: string
  company?: string
  message: string
}

type SubmitErrorInfo = {
  title: string
  message?: string
  details?: string[]
}

type ApiErrorDetails = {
  title?: string
  message?: string
  details?: string[]
}

function buildContactMailSubject(form: GetInTouchDialogFormData): string {
  const firstname = form.firstname.trim()
  const lastname = form.lastname.trim()

  return `New message from ${firstname} ${lastname}`
}

function buildContactMailMessage(form: GetInTouchDialogFormData): string {
  const firstname = form.firstname.trim()
  const lastname = form.lastname.trim()
  const email = form.email.trim()
  const phone = form.phone?.trim() || 'Not provided'
  const company = form.company?.trim() || 'Not provided'
  const message = form.message.trim()

  return [
    'Hello,',
    '',
    'You have received a new message through the Verno contact form.',
    '',
    'Contact information',
    '────────────────────────',
    `First name: ${firstname}`,
    `Last name: ${lastname}`,
    `Email: ${email}`,
    `Phone: ${phone}`,
    `Company: ${company}`,
    '',
    'Message',
    '────────────────────────',
    message,
    '',
    '────────────────────────',
    'Sent from the Verno website contact form.',
  ].join('\n')
}

export default function GetInTouchDialog({ open, onClose }: Props) {
  const dialogContentRef = useRef<HTMLDivElement>(null)

  const [submitting, setSubmitting] = useState(false)
  const [submitError, setSubmitError] = useState<SubmitErrorInfo | null>(null)

  const {
    register,
    handleSubmit,
    reset,
    watch,
    formState: { errors, isValid },
  } = useForm<GetInTouchDialogFormData>({
    mode: 'onChange',
    reValidateMode: 'onChange',
    defaultValues: {
      firstname: '',
      lastname: '',
      email: '',
      phone: '',
      company: '',
      message: '',
    },
  })

  useEffect(() => {
    if (open) {
      setSubmitError(null)
    } else {
      reset()
    }
  }, [open, reset])

  const resolveSubmitError = (error: unknown): SubmitErrorInfo => {
    if (error instanceof ApiError) {
      const payload = error.details as ApiErrorDetails | undefined

      return {
        title: payload?.title ?? 'Failed to send message',
        message:
          payload?.message ??
          error.message ??
          'Something went wrong while sending your message.',
        details: Array.isArray(payload?.details) ? payload.details : undefined,
      }
    }

    if (error instanceof Error) {
      return {
        title: 'Failed to send message',
        message: error.message,
      }
    }

    return {
      title: 'Failed to send message',
      message: 'Unknown error',
    }
  }

  const CONTACT_EMAIL = import.meta.env.VITE_CONTACT_EMAIL as string | undefined

  const onSubmit = handleSubmit(async (form) => {
    try {
      setSubmitting(true)
      setSubmitError(null)

      if (!CONTACT_EMAIL) {
        throw new Error('Missing VITE_CONTACT_EMAIL configuration')
      }

      const email = form.email.trim()

      await emailApi.sendMessage({
        from: email,
        to: CONTACT_EMAIL,
        subject: buildContactMailSubject(form),
        message: buildContactMailMessage(form),
      })

      toast.success('Request sent successfully!', {
        description: 'We will get back to you shortly.',
      })

      onClose()
    } catch (error) {
      setSubmitError(resolveSubmitError(error))
    } finally {
      setSubmitting(false)
    }
  })

  const watchedMessage = watch('message')

  return (
    <Dialog
      open={open}
      onClose={submitting ? () => {} : onClose}
      className="relative z-100"
    >
      <div className="fixed inset-0 z-100">
        <DialogBackdrop className="fixed inset-0 bg-black/60" />

        <div className="fixed inset-0 flex items-center justify-center p-4">
          <DialogPanel className="relative z-101 w-full max-w-2xl rounded-2xl bg-verno-surface p-6 text-verno-dark shadow-xl">
            <div className="flex items-start justify-between gap-4">
              <div>
                <DialogTitle className="text-lg font-semibold">
                  Get in touch
                </DialogTitle>
                <p className="mt-1 text-sm text-verno-darker/80">
                  Send us your message and we will get back to you shortly.
                </p>
              </div>

              <div className="ml-auto flex items-center gap-2">
                <div className="flex items-center gap-1 p-2">
                  <span
                    className="h-2 w-12 rounded-full bg-verno-accent"
                    aria-hidden
                  />
                </div>
              </div>
            </div>

            <form onSubmit={onSubmit}>
              <div
                ref={dialogContentRef}
                className="mt-6 grid grid-cols-1 gap-4 md:grid-cols-2"
              >
                <div>
                  <label className="mb-1 block text-sm font-medium">
                    First name
                  </label>
                  <input
                    {...register('firstname', {
                      required: 'First name is required',
                    })}
                    className="w-full rounded-xl border border-verno-darker/15 bg-white px-3 py-2.5 outline-none transition focus:border-verno-accent"
                    placeholder="John"
                  />
                  {errors.firstname && (
                    <p className="mt-1 text-sm text-red-600">
                      {errors.firstname.message}
                    </p>
                  )}
                </div>

                <div>
                  <label className="mb-1 block text-sm font-medium">
                    Last name
                  </label>
                  <input
                    {...register('lastname', {
                      required: 'Last name is required',
                    })}
                    className="w-full rounded-xl border border-verno-darker/15 bg-white px-3 py-2.5 outline-none transition focus:border-verno-accent"
                    placeholder="Doe"
                  />
                  {errors.lastname && (
                    <p className="mt-1 text-sm text-red-600">
                      {errors.lastname.message}
                    </p>
                  )}
                </div>

                <div>
                  <label className="mb-1 block text-sm font-medium">
                    Email
                  </label>
                  <input
                    type="email"
                    {...register('email', {
                      required: 'Email is required',
                      pattern: {
                        value: /^\S+@\S+\.\S+$/,
                        message: 'Please enter a valid email address',
                      },
                    })}
                    className="w-full rounded-xl border border-verno-darker/15 bg-white px-3 py-2.5 outline-none transition focus:border-verno-accent"
                    placeholder="john@company.com"
                  />
                  {errors.email && (
                    <p className="mt-1 text-sm text-red-600">
                      {errors.email.message}
                    </p>
                  )}
                </div>

                <div>
                  <label className="mb-1 block text-sm font-medium">
                    Phone
                  </label>
                  <input
                    {...register('phone')}
                    className="w-full rounded-xl border border-verno-darker/15 bg-white px-3 py-2.5 outline-none transition focus:border-verno-accent"
                    placeholder="+41 79 123 45 67"
                  />
                </div>

                <div className="md:col-span-2">
                  <label className="mb-1 block text-sm font-medium">
                    Company
                  </label>
                  <input
                    {...register('company')}
                    className="w-full rounded-xl border border-verno-darker/15 bg-white px-3 py-2.5 outline-none transition focus:border-verno-accent"
                    placeholder="Your company"
                  />
                </div>

                <div className="md:col-span-2">
                  <div className="mb-1 flex items-center justify-between">
                    <label className="block text-sm font-medium">Message</label>
                    <span className="text-xs text-verno-darker/60">
                      {watchedMessage?.length ?? 0}/1000
                    </span>
                  </div>

                  <textarea
                    {...register('message', {
                      required: 'Message is required',
                      minLength: {
                        value: 10,
                        message: 'Message must be at least 10 characters',
                      },
                      maxLength: {
                        value: 1000,
                        message: 'Message must not exceed 1000 characters',
                      },
                    })}
                    rows={6}
                    className="w-full rounded-xl border border-verno-darker/15 bg-white px-3 py-2.5 outline-none transition focus:border-verno-accent"
                    placeholder="Tell us a bit about your request..."
                  />
                  {errors.message && (
                    <p className="mt-1 text-sm text-red-600">
                      {errors.message.message}
                    </p>
                  )}
                </div>
              </div>

              {submitError && (
                <div className="mt-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
                  <p className="font-medium">{submitError.title}</p>
                  {submitError.message && (
                    <p className="mt-1">{submitError.message}</p>
                  )}
                  {submitError.details?.length ? (
                    <ul className="mt-2 list-disc pl-5">
                      {submitError.details.map((detail) => (
                        <li key={detail}>{detail}</li>
                      ))}
                    </ul>
                  ) : null}
                </div>
              )}

              <div className="mt-6 flex items-center justify-between">
                <div>
                  <Button
                    type="button"
                    variant="outline"
                    onClick={onClose}
                    disabled={submitting}
                  >
                    <CircleSlash className="h-5 w-5" /> Cancel
                  </Button>
                </div>

                <div className="flex items-center gap-2">
                  <Button type="submit" disabled={!isValid || submitting}>
                    {submitting ? 'Sending...' : 'Send message'}
                    {submitting ? (
                      <ArrowRightIcon className="h-5 w-5" />
                    ) : (
                      <SendIcon className="h-5 w-5" />
                    )}
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
