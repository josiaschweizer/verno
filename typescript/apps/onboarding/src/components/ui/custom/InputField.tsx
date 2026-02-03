import React, { ChangeEventHandler } from 'react'
import { Field, FieldDescription, FieldLabel } from '@/components/ui/field'
import { Input } from '@/components/ui/input'
import { InputTypes } from '@/types/ui/custom/InputTypes'

type Props = {
  fieldLabel?: string
  fieldDescription?: string
  fieldId?: string
  type?: InputTypes
  placeholder?: string
  value?: string | readonly string[] | number | undefined
  onChange?: ChangeEventHandler<HTMLInputElement> | undefined
  disabled?: boolean | undefined
  className?: string
  required?: boolean
}

export const InputField = React.forwardRef<HTMLInputElement, Props>(
  (
    {
      fieldLabel,
      fieldDescription,
      fieldId,
      type,
      placeholder,
      value,
      onChange,
      disabled,
      className,
      required,
    }: Props,
    ref,
  ) => {
    const localClass = `${className ?? ''} [&>[data-slot=field]]:p-0 [&>[data-slot=field]]:gap-0 [&>[data-slot=field-content]]:gap-1 [&>[data-slot=field-label]]:mb-1 [&>[data-slot=field-label]]:py-0`

    return (
      <Field className={localClass}>
        {fieldLabel && (
          <FieldLabel htmlFor={fieldId}>
            {fieldLabel}
            {required && (
              <span className="text-red-500 ml-1" aria-hidden="true">
                *
              </span>
            )}
          </FieldLabel>
        )}

        <Input
          id={fieldId}
          ref={ref}
          type={type}
          placeholder={placeholder}
          readOnly={disabled}
          value={value ?? ''}
          onChange={onChange}
          required={required}
          aria-required={required}
        />

        {fieldDescription && (
          <FieldDescription>{fieldDescription}</FieldDescription>
        )}
      </Field>
    )
  },
)

InputField.displayName = 'InputField'
