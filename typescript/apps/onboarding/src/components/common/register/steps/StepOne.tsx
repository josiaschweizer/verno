import { Input } from '@/components/ui/input'
import RegisterDialogFormData from '@/interfaces/register/RegisterDialogFormData'
import { Control, Controller } from 'react-hook-form'
import { ComboBoxItem } from '@/type/ComboBoxItem'

interface Props {
  control: Control<RegisterDialogFormData>
  getValues: (field: string) => any
  readOnly: boolean
}

export default function StepOne({ control, getValues, readOnly }: Props) {
  const validatePasswordMatch = (value: string) => {
    console.log(
      'validating',
      value === getValues('password') || 'Passwords do not match',
    )
    return value === getValues('password') || 'Passwords do not match'
  }

  const languages: ComboBoxItem[] = [
    { label: 'German', value: 'de' },
    { label: 'English', value: 'en' },
    { label: 'French', value: 'fr' },
  ]

  return (
    <div>
      <h3 className="text-base font-medium">
        Schritt 1 — Basic Data for Your Account
      </h3>

      <div className="mt-4 space-y-2">
        <div className="flex w-full gap-2">
          <div className="flex-1 min-w-0">
            <Controller
              name="firstname"
              control={control}
              render={({ field: { onChange, value } }) => (
                <Input
                  placeholder="Firstname"
                  value={value}
                  onChange={onChange}
                  disabled={readOnly}
                  className="w-full"
                />
              )}
            />
          </div>
          <div className="flex-1 min-w-0">
            <Controller
              name="lastname"
              control={control}
              render={({ field: { onChange, value } }) => (
                <Input
                  placeholder="Lastname"
                  value={value}
                  onChange={onChange}
                  disabled={readOnly}
                  className="w-full"
                />
              )}
            />
          </div>
        </div>

        <div className="flex w-full gap-2">
          <div className="flex-1 min-w-0">
            <Controller
              name="email"
              control={control}
              render={({ field: { onChange, value } }) => (
                <Input
                  placeholder="E-Mail"
                  value={value}
                  onChange={onChange}
                  disabled={readOnly}
                  className="w-full"
                />
              )}
            />
          </div>
          <div className="flex-1 min-w-0">
            <Controller
              name="phone"
              control={control}
              render={({ field: { onChange, value } }) => (
                <Input
                  placeholder="Phone"
                  value={value}
                  onChange={onChange}
                  disabled={readOnly}
                  className="w-full"
                />
              )}
            />
          </div>
        </div>

        {/*<Controller*/}
        {/*  name="preferredLanguage"*/}
        {/*  control={control}*/}
        {/*  render={({ field: { onChange, value } }) => (*/}
        {/*    <ComboBox*/}
        {/*      placeholder="Preferred Language"*/}
        {/*      value={value}*/}
        {/*      onChange={onChange}*/}
        {/*      items={languages}*/}
        {/*      className="w-full"*/}
        {/*    />*/}
        {/*  )}*/}
        {/*/>*/}

        <Controller
          name="password"
          control={control}
          rules={{
            required: 'Password is required',
            minLength: {
              value: 8,
              message: 'Password must be at least 8 characters',
            },
          }}
          render={({ field, fieldState }) => (
            <div>
              <Input
                {...field}
                type="password"
                placeholder="Enter your password"
                className={`w-full ${fieldState.invalid ? 'border-red-500' : ''}`}
              />
              {fieldState.error && (
                <p className="mt-1 text-sm text-red-500">
                  {fieldState.error?.message}
                </p>
              )}
            </div>
          )}
        />

        <Controller
          name="confirmPassword"
          control={control}
          rules={{
            required: 'Please confirm your password',
            minLength: {
              value: 8,
              message: 'Password must be at least 8 characters',
            },
          }}
          render={({ field, fieldState }) => (
            <div>
              <Input
                {...field}
                type="password"
                placeholder="Confirm your password"
                className={`w-full ${fieldState.invalid ? 'border-red-500' : ''}`}
              />
              {fieldState.error && (
                <p className="mt-1 text-sm text-red-500">
                  {fieldState.error?.message}
                </p>
              )}
            </div>
          )}
        />
      </div>
    </div>
  )
}
