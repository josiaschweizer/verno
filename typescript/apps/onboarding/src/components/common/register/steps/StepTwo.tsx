import { Input } from '@/components/ui/input'
import { Control, Controller } from 'react-hook-form'
import RegisterDialogFormData from '@/interfaces/register/RegisterDialogFormData'
import DomainInputField from '@/components/ui/custom/DomainInputField'

interface Props {
  control: Control<RegisterDialogFormData>
  getValues: (field: string) => any
  readOnly: boolean
}

export default function StepTwo({ control, getValues, readOnly }: Props) {
  return (
    <div>
      <h3 className="text-base font-medium">Step 2 — Tenant Configuration</h3>

      <div className="mt-4 space-y-2">
        <Controller
          name="tenantName"
          control={control}
          render={({ field: { onChange, value } }) => (
            <Input
              placeholder="Tenant Display Name"
              onChange={onChange}
              value={value}
              disabled={readOnly}
              className="w-full"
            />
          )}
        />

        <DomainInputField
          baseDomain=".verno.swiss"
          control={control}
          readOnly={readOnly}
        />
      </div>
    </div>
  )
}
