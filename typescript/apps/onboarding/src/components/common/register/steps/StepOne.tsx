import { ComboBoxField } from '@/components/ui/custom/ComboBoxField'
import { InputField } from '@/components/ui/custom/InputField'
import RegisterDialogFormData from '@/interfaces/register/RegisterDialogFormData'
import { Control, Controller } from 'react-hook-form'
import { ComboBoxItem } from '@/type/ComboBoxItem'
import { RefObject, useMemo } from 'react'
import { useTranslation } from 'react-i18next'

interface Props {
  control: Control<RegisterDialogFormData, any, any>
  getValues: (field: string) => any
  readOnly: boolean
  portalContainerRef?: RefObject<HTMLDivElement | null>
}

export default function StepOne({
  control,
  getValues,
  readOnly,
  portalContainerRef,
}: Props) {
  const { t } = useTranslation('register')

  const validatePasswordMatch = (value: string) =>
    value === getValues('password') ||
    t('stepOne.validation.passwordsDoNotMatch')

  const languages: ComboBoxItem[] = useMemo(
    () => [
      { label: t('stepOne.languages.de'), value: 'de' },
      { label: t('stepOne.languages.en'), value: 'en' },
      { label: t('stepOne.languages.fr'), value: 'fr' },
    ],
    [t],
  )

  return (
    <div>
      <h3 className="text-base font-medium leading-6 sm:text-lg">
        {t('stepOne.title')}
      </h3>

      <div className="mt-5 space-y-4 sm:space-y-5">
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
          <Controller
            name="firstname"
            control={control}
            render={({ field }) => (
              <InputField
                fieldLabel={t('stepOne.fields.firstname')}
                placeholder={t('stepOne.placeholders.firstname')}
                {...field}
                disabled={readOnly}
                className="w-full"
              />
            )}
          />

          <Controller
            name="lastname"
            control={control}
            render={({ field }) => (
              <InputField
                fieldLabel={t('stepOne.fields.lastname')}
                placeholder={t('stepOne.placeholders.lastname')}
                {...field}
                disabled={readOnly}
                className="w-full"
              />
            )}
          />

          <Controller
            name="username"
            control={control}
            render={({ field }) => (
              <InputField
                fieldLabel={t('stepOne.fields.username')}
                placeholder={t('stepOne.placeholders.username')}
                {...field}
                disabled={readOnly}
                className="w-full"
              />
            )}
          />
        </div>

        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
          <Controller
            name="email"
            control={control}
            rules={{
              required: t('stepOne.validation.emailRequired'),
              pattern: {
                value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
                message: t('stepOne.validation.invalidEmail'),
              },
            }}
            render={({ field, fieldState }) => (
              <div className="w-full">
                <InputField
                  fieldLabel={t('stepOne.fields.email')}
                  placeholder={t('stepOne.placeholders.email')}
                  {...field}
                  disabled={readOnly}
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

          <Controller
            name="phone"
            control={control}
            rules={{
              pattern: {
                value: /^\d+$/,
                message: t('stepOne.validation.onlyNumbers'),
              },
            }}
            render={({ field, fieldState }) => (
              <div className="w-full">
                <InputField
                  fieldLabel={t('stepOne.fields.phone')}
                  placeholder={t('stepOne.placeholders.phone')}
                  {...field}
                  disabled={readOnly}
                  className="w-full"
                />
                {fieldState.error && (
                  <p className="mt-1.5 text-sm text-red-500">
                    {fieldState.error.message}
                  </p>
                )}
              </div>
            )}
          />
        </div>

        <Controller
          name="preferredLanguage"
          control={control}
          render={({ field: { onChange, value } }) => (
            <div className="w-full">
              <ComboBoxField
                fieldId="preferredLanguage"
                fieldLabel={t('stepOne.fields.preferredLanguage')}
                options={languages}
                value={(value ?? null) as any}
                onChange={(v) => onChange(v ?? undefined)}
                disabled={readOnly}
                portalContainer={portalContainerRef}
              />
            </div>
          )}
        />

        <Controller
          name="password"
          control={control}
          rules={{
            required: t('stepOne.validation.passwordRequired'),
            minLength: {
              value: 8,
              message: t('stepOne.validation.passwordMinLength'),
            },
          }}
          render={({ field, fieldState }) => (
            <div className="w-full">
              <InputField
                fieldLabel={t('stepOne.fields.password')}
                type="password"
                placeholder={t('stepOne.placeholders.password')}
                {...field}
                disabled={readOnly}
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

        <Controller
          name="confirmPassword"
          control={control}
          rules={{
            required: t('stepOne.validation.confirmPasswordRequired'),
            validate: validatePasswordMatch,
          }}
          render={({ field, fieldState }) => (
            <div className="w-full">
              <InputField
                fieldLabel={t('stepOne.fields.confirmPassword')}
                type="password"
                placeholder={t('stepOne.placeholders.confirmPassword')}
                {...field}
                disabled={readOnly}
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
      </div>
    </div>
  )
}
