import { Field, FieldDescription, FieldLabel } from '@/components/ui/field'
import { Input } from '@/components/ui/input'
import { InputTypes } from '@/types/ui/custom/InputTypes'
import { Combobox } from '@/components/ui/combobox'

type BaseProps = {
  fieldLabel?: string
  fieldDescription?: string
  fieldId?: string
}

type InputProps = BaseProps & {
  type?: InputTypes
  placeholder?: string
}

type ComboboxProps = BaseProps & {
  type: 'combobox'
  options: { value: string; label: string }[]
  value?: string
  onChange?: (value: string) => void
  placeholder?: string
}

type Props = InputProps | ComboboxProps

export function InputField(props: Props) {
  const { fieldLabel, fieldDescription, fieldId } = props

  if (props.type === 'combobox') {
    return (
      <Field>
        <FieldLabel htmlFor={fieldId}>{fieldLabel}</FieldLabel>

        <Combobox
          value={props.value}
          onChange={props.onChange}
          options={props.options}
          placeholder={props.placeholder}
        />

        <FieldDescription>{fieldDescription}</FieldDescription>
      </Field>
    )
  }

  return (
    <Field>
      <FieldLabel htmlFor={fieldId}>{fieldLabel}</FieldLabel>

      <Input id={fieldId} type={props.type} placeholder={props.placeholder} />

      <FieldDescription>{fieldDescription}</FieldDescription>
    </Field>
  )
}
