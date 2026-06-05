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
import { useTranslation } from 'react-i18next'

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

export default function GetInTouchDialog({ open, onClose }: Props) {
  const { t } = useTranslation('contact')
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

  const buildContactMailSubject = (form: GetInTouchDialogFormData): string => {
    const firstname = form.firstname.trim()
    const lastname = form.lastname.trim()

    return t('getInTouchDialog.mail.subject', {
      firstname,
      lastname,
    })
  }

  const buildContactMailMessage = (form: GetInTouchDialogFormData): string => {
    const firstname = form.firstname.trim()
    const lastname = form.lastname.trim()
    const email = form.email.trim()
    const phone =
      form.phone?.trim() || t('getInTouchDialog.mail.message.notProvided')
    const company =
      form.company?.trim() || t('getInTouchDialog.mail.message.notProvided')
    const message = form.message.trim()

    return [
      t('getInTouchDialog.mail.message.greeting'),
      '',
      t('getInTouchDialog.mail.message.intro'),
      '',
      t('getInTouchDialog.mail.message.contactInformation'),
      '---',
      `${t('getInTouchDialog.mail.message.firstName')}: ${firstname}`,
      `${t('getInTouchDialog.mail.message.lastName')}: ${lastname}`,
      `${t('getInTouchDialog.mail.message.email')}: ${email}`,
      `${t('getInTouchDialog.mail.message.phone')}: ${phone}`,
      `${t('getInTouchDialog.mail.message.company')}: ${company}`,
      '',
      t('getInTouchDialog.mail.message.message'),
      '---',
      message,
      '',
      '---',
      t('getInTouchDialog.mail.message.footer'),
    ].join('\n')
  }

  const resolveSubmitError = (error: unknown): SubmitErrorInfo => {
    if (error instanceof ApiError) {
      const payload = error.details as ApiErrorDetails | undefined

      return {
        title: payload?.title ?? t('getInTouchDialog.errors.failedTitle'),
        message:
          payload?.message ??
          error.message ??
          t('getInTouchDialog.errors.failedMessage'),
        details: Array.isArray(payload?.details) ? payload.details : undefined,
      }
    }

    if (error instanceof Error) {
      return {
        title: t('getInTouchDialog.errors.failedTitle'),
        message: error.message,
      }
    }

    return {
      title: t('getInTouchDialog.errors.failedTitle'),
      message: t('getInTouchDialog.errors.unknown'),
    }
  }

  const CONTACT_EMAIL = import.meta.env.VITE_CONTACT_EMAIL as string | undefined

  const onSubmit = handleSubmit(async (form) => {
    try {
      setSubmitting(true)
      setSubmitError(null)

      if (!CONTACT_EMAIL) {
        throw new Error(t('getInTouchDialog.errors.missingContactEmail'))
      }

      const email = form.email.trim()

      await emailApi.sendMessage({
        from: email,
        to: CONTACT_EMAIL,
        subject: buildContactMailSubject(form),
        message: buildContactMailMessage(form),
      })

      toast.success(t('getInTouchDialog.toast.successTitle'), {
        description: t('getInTouchDialog.toast.successDescription'),
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
      className="relative z-[100]"
    >
      <DialogBackdrop className="fixed inset-0 z-[100] bg-black/60" />

      <div className="fixed inset-0 z-[101] overflow-y-auto">
        <div className="flex min-h-full items-start justify-center p-3 sm:p-4 md:items-center">
          <DialogPanel className="relative w-full max-w-2xl overflow-hidden rounded-2xl bg-verno-surface text-verno-dark shadow-xl max-h-[calc(100dvh-1.5rem)] sm:max-h-[calc(100dvh-2rem)] flex flex-col">
            <div className="shrink-0 p-5 sm:p-6 border-b border-verno-darker/10">
              <div className="flex items-start justify-between gap-4">
                <div>
                  <DialogTitle className="text-lg font-semibold">
                    {t('getInTouchDialog.dialog.title')}
                  </DialogTitle>
                  <p className="mt-1 text-sm text-verno-darker/80">
                    {t('getInTouchDialog.dialog.subtitle')}
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
            </div>

            <form onSubmit={onSubmit} className="flex min-h-0 flex-1 flex-col">
              <div
                ref={dialogContentRef}
                className="min-h-0 flex-1 overflow-y-auto px-5 py-5 sm:px-6 sm:py-6"
              >
                <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                  <div>
                    <label className="mb-1 block text-sm font-medium">
                      {t('getInTouchDialog.form.fields.firstname.label')}
                    </label>
                    <input
                      {...register('firstname', {
                        required: t(
                          'getInTouchDialog.form.validation.firstnameRequired',
                        ),
                      })}
                      className="w-full rounded-xl border border-verno-darker/15 bg-white px-3 py-2.5 outline-none transition focus:border-verno-accent"
                      placeholder={t(
                        'getInTouchDialog.form.fields.firstname.placeholder',
                      )}
                    />
                    {errors.firstname && (
                      <p className="mt-1 text-sm text-red-600">
                        {errors.firstname.message}
                      </p>
                    )}
                  </div>

                  <div>
                    <label className="mb-1 block text-sm font-medium">
                      {t('getInTouchDialog.form.fields.lastname.label')}
                    </label>
                    <input
                      {...register('lastname', {
                        required: t(
                          'getInTouchDialog.form.validation.lastnameRequired',
                        ),
                      })}
                      className="w-full rounded-xl border border-verno-darker/15 bg-white px-3 py-2.5 outline-none transition focus:border-verno-accent"
                      placeholder={t(
                        'getInTouchDialog.form.fields.lastname.placeholder',
                      )}
                    />
                    {errors.lastname && (
                      <p className="mt-1 text-sm text-red-600">
                        {errors.lastname.message}
                      </p>
                    )}
                  </div>

                  <div>
                    <label className="mb-1 block text-sm font-medium">
                      {t('getInTouchDialog.form.fields.email.label')}
                    </label>
                    <input
                      type="email"
                      {...register('email', {
                        required: t(
                          'getInTouchDialog.form.validation.emailRequired',
                        ),
                        pattern: {
                          value: /^\S+@\S+\.\S+$/,
                          message: t(
                            'getInTouchDialog.form.validation.emailInvalid',
                          ),
                        },
                      })}
                      className="w-full rounded-xl border border-verno-darker/15 bg-white px-3 py-2.5 outline-none transition focus:border-verno-accent"
                      placeholder={t(
                        'getInTouchDialog.form.fields.email.placeholder',
                      )}
                    />
                    {errors.email && (
                      <p className="mt-1 text-sm text-red-600">
                        {errors.email.message}
                      </p>
                    )}
                  </div>

                  <div>
                    <label className="mb-1 block text-sm font-medium">
                      {t('getInTouchDialog.form.fields.phone.label')}
                    </label>
                    <input
                      {...register('phone')}
                      className="w-full rounded-xl border border-verno-darker/15 bg-white px-3 py-2.5 outline-none transition focus:border-verno-accent"
                      placeholder={t(
                        'getInTouchDialog.form.fields.phone.placeholder',
                      )}
                    />
                  </div>

                  <div className="md:col-span-2">
                    <label className="mb-1 block text-sm font-medium">
                      {t('getInTouchDialog.form.fields.company.label')}
                    </label>
                    <input
                      {...register('company')}
                      className="w-full rounded-xl border border-verno-darker/15 bg-white px-3 py-2.5 outline-none transition focus:border-verno-accent"
                      placeholder={t(
                        'getInTouchDialog.form.fields.company.placeholder',
                      )}
                    />
                  </div>

                  <div className="md:col-span-2">
                    <div className="mb-1 flex items-center justify-between">
                      <label className="block text-sm font-medium">
                        {t('getInTouchDialog.form.fields.message.label')}
                      </label>
                      <span className="text-xs text-verno-darker/60">
                        {t('getInTouchDialog.form.fields.message.counter', {
                          count: watchedMessage?.length ?? 0,
                        })}
                      </span>
                    </div>

                    <textarea
                      {...register('message', {
                        required: t(
                          'getInTouchDialog.form.validation.messageRequired',
                        ),
                        minLength: {
                          value: 10,
                          message: t(
                            'getInTouchDialog.form.validation.messageMinLength',
                          ),
                        },
                        maxLength: {
                          value: 1000,
                          message: t(
                            'getInTouchDialog.form.validation.messageMaxLength',
                          ),
                        },
                      })}
                      rows={6}
                      className="w-full rounded-xl border border-verno-darker/15 bg-white px-3 py-2.5 outline-none transition focus:border-verno-accent"
                      placeholder={t(
                        'getInTouchDialog.form.fields.message.placeholder',
                      )}
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
              </div>

              <div className="shrink-0 border-t border-verno-darker/10 px-5 py-4 sm:px-6">
                <div className="flex flex-col-reverse gap-3 sm:flex-row sm:items-center sm:justify-between">
                  <Button
                    type="button"
                    variant="outline"
                    onClick={onClose}
                    disabled={submitting}
                    className="w-full sm:w-auto"
                  >
                    <CircleSlash className="h-5 w-5" />
                    {t('getInTouchDialog.form.buttons.cancel')}
                  </Button>

                  <Button
                    type="submit"
                    disabled={!isValid || submitting}
                    className="w-full sm:w-auto"
                  >
                    {submitting
                      ? t('getInTouchDialog.form.buttons.sending')
                      : t('getInTouchDialog.form.buttons.send')}

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
