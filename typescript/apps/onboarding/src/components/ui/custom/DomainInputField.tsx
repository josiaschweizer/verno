import { Control, Controller } from 'react-hook-form'
import RegisterDialogFormData from '@/interfaces/register/RegisterDialogFormData'
import { Field } from '@headlessui/react'
import {
  InputGroup,
  InputGroupAddon,
  InputGroupInput,
  InputGroupText,
} from '@/components/ui/input-group'

interface Props {
  baseDomain: string
  control: Control<RegisterDialogFormData>
  readOnly: boolean
  placeholder?: string
}

export default function DomainInputField({
  baseDomain,
  control,
  readOnly,
  placeholder = 'your-subdomain',
}: Props) {
  return (
    <Controller
      name="tenantSubdomain"
      control={control}
      render={({ field: { onChange, value } }) => (
        <Field onChange={onChange}>
          <InputGroup>
            <InputGroupInput
              readOnly={readOnly}
              value={value}
              id="input-group-url"
              placeholder={placeholder}
            />
            <InputGroupAddon align="inline-end">
              <InputGroupText>{baseDomain}</InputGroupText>
            </InputGroupAddon>
          </InputGroup>
        </Field>
      )}
    />
  )
}
