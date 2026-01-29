import { Control, Controller } from 'react-hook-form'
import RegisterDialogFormData from '@/interfaces/register/RegisterDialogFormData'
import { Input } from '@/components/ui/input'
import { Field } from '@headlessui/react'
import {
  InputGroup,
  InputGroupAddon,
  InputGroupInput,
  InputGroupText,
} from '@/components/ui/input-group'
import { InfoIcon } from 'lucide-react'

interface Props {
  baseDomain: string
  control: Control<RegisterDialogFormData>
  getValues: (field: string) => any
  readOnly: boolean
  placeholder?: string
}

export default function DomainInputField({
  baseDomain,
  control,
  getValues,
  readOnly,
  placeholder = 'your-subdomain',
}: Props) {
  return (
    <Controller
      name="tenantSubdomain"
      control={control}
      render={({ field: { onChange, value } }) => (
        <Field>
          <InputGroup>
            <InputGroupInput id="input-group-url" placeholder={placeholder} />
            <InputGroupAddon align="inline-end">
              <InputGroupText>{baseDomain}</InputGroupText>
            </InputGroupAddon>
          </InputGroup>
        </Field>
      )}
    />
  )
}
