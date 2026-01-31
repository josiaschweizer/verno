import { Field, FieldDescription, FieldLabel } from '@/components/ui/field'
import { Input } from '@/components/ui/input'
import { InputTypes } from '@/types/ui/custom/InputTypes'

type Props = {
  fieldLabel?: string
  fieldDescription?: string
  fieldId?: string
  type?: InputTypes
  placeholder?: string
}

export function InputField({
  fieldLabel,
  fieldDescription,
  fieldId,
  type,
  placeholder,
}: Props) {
  return (
    <Field>
      <FieldLabel htmlFor={fieldId}>{fieldLabel}</FieldLabel>
      <Input id={fieldId} type={type} placeholder={placeholder} />
      <FieldDescription>{fieldDescription}</FieldDescription>
    </Field>
  )
}
