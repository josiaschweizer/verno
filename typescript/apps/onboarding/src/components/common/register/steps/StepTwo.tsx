import { Input } from '@verno/components/ui/input'
import { Control, Controller } from 'react-hook-form'
import RegisterDialogFormData from '@/interfaces/register/RegisterDialogFormData'
import DomainInputField from '@/components/ui/custom/DomainInputField'
import { Field, FieldLabel } from '@verno/components/ui/field'
import { useTranslation } from 'react-i18next'

interface Props {
  control: Control<RegisterDialogFormData, any, any>
  readOnly: boolean
}

export default function StepTwo({ control, readOnly }: Props) {
  const { t } = useTranslation('register')

  return (
    <div>
      <h3 className="text-base font-medium">{t('stepTwo.title')}</h3>

      <div className="mt-4 space-y-2">
        <Controller
          name="tenantName"
          control={control}
          rules={{ required: t('stepTwo.validation.displayNameRequired') }}
          render={({ field: { onChange, value }, fieldState }) => (
            <Field>
              <FieldLabel htmlFor="tenantName">
                {t('stepTwo.fields.displayName')}{' '}
                <span className="text-red-500">*</span>
              </FieldLabel>
              <div>
                <Input
                  id="tenantName"
                  placeholder={t('stepTwo.placeholders.tenantDisplayName')}
                  onChange={onChange}
                  value={value}
                  disabled={readOnly}
                  className="w-full"
                />
                {fieldState.error && (
                  <p className="mt-1 text-sm text-red-500">
                    {fieldState.error.message}
                  </p>
                )}
              </div>
            </Field>
          )}
        />

        <DomainInputField
          baseDomain=".verno-app.ch"
          control={control}
          readOnly={readOnly}
          rules={{ required: t('stepTwo.validation.subdomainRequired') }}
          label={t('stepTwo.fields.url')}
          required
          fieldId="tenantSubdomain"
        />
      </div>
    </div>
  )
}
