import { Input } from '@/components/ui/input'
import { Control, Controller, useFormState } from 'react-hook-form'
import RegisterDialogFormData from '@/interfaces/register/RegisterDialogFormData'
import DomainInputField from '@/components/ui/custom/DomainInputField'
import { Field, FieldLabel } from '@/components/ui/field'

interface Props {
  control: Control<RegisterDialogFormData, any, any>
  readOnly: boolean
}

export default function StepTwo({ control, readOnly }: Props) {
  const { errors } = useFormState({ control })

  return (
    <div>
      <h3 className="text-base font-medium">Step 2 — Tenant Configuration</h3>

      <div className="mt-4 space-y-2">
        <Controller
          name="tenantName"
          control={control}
          rules={{ required: 'Display name is required' }}
          render={({ field: { onChange, value }, fieldState }) => (
            <Field>
              <FieldLabel htmlFor="tenantName">
                Display name <span className="text-red-500">*</span>
              </FieldLabel>
              <div>
                <Input
                  id="tenantName"
                  placeholder="Tenant Display Name"
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
          baseDomain=".verno.swiss"
          control={control}
          readOnly={readOnly}
          rules={{ required: 'Subdomain is required' }}
          label="URL"
          required
          fieldId="tenantSubdomain"
        />
      </div>
    </div>
  )
}
