import { Control, Controller, RegisterOptions } from 'react-hook-form'
import RegisterDialogFormData from '@/interfaces/register/RegisterDialogFormData'
import {
  InputGroup,
  InputGroupAddon,
  InputGroupInput,
  InputGroupText,
} from '@/components/ui/input-group'
import { Field, FieldLabel, FieldDescription } from '@/components/ui/field'

interface Props {
  baseDomain: string
  control: Control<RegisterDialogFormData, any>
  readOnly: boolean
  placeholder?: string
  rules?: RegisterOptions<RegisterDialogFormData, 'tenantSubdomain'>
  label?: string
  fieldId?: string
  required?: boolean
}

export default function DomainInputField({
  baseDomain,
  control,
  readOnly,
  placeholder = 'your-subdomain',
  rules,
  label,
  fieldId = 'tenantSubdomain',
  required = false,
}: Props) {
  return (
    <Controller
      name="tenantSubdomain"
      control={control}
      rules={rules}
      render={({ field: { onChange, value }, fieldState }) => (
        <Field>
          {label && (
            <FieldLabel htmlFor={fieldId}>
              {label} {required && <span className="text-red-500 ml-1">*</span>}
            </FieldLabel>
          )}

          <div>
            <InputGroup>
              <InputGroupInput
                readOnly={readOnly}
                value={value ?? ''}
                id={fieldId}
                placeholder={placeholder}
                onChange={(e) => onChange(e.target.value)}
                aria-invalid={fieldState?.error ? true : undefined}
              />
              <InputGroupAddon align="inline-end">
                <InputGroupText>{baseDomain}</InputGroupText>
              </InputGroupAddon>
            </InputGroup>
            {fieldState?.error && (
              <p className="mt-1 text-sm text-red-500">
                {(fieldState.error as any).message}
              </p>
            )}
            {/** optional description slot kept for parity */}
            <FieldDescription />
          </div>
        </Field>
      )}
    />
  )
}
