import { ComboBoxField } from '@/components/ui/custom/ComboBoxField'
import { InputField } from '@/components/ui/custom/InputField'
import RegisterDialogFormData from '@/interfaces/register/RegisterDialogFormData'
import { Control, Controller } from 'react-hook-form'
import { ComboBoxItem } from '@/type/ComboBoxItem'
import { RefObject } from 'react'

interface Props {
  control: Control<RegisterDialogFormData, any, any>
  getValues: (field: string) => any
  readOnly: boolean
  portalContainerRef?: RefObject<HTMLDivElement | null>
}

export default function StepOne({
  control,
  getValues,
  readOnly,
  portalContainerRef,
}: Props) {
  const validatePasswordMatch = (value: string) =>
    value === getValues('password') || 'Passwords do not match'

  const languages: ComboBoxItem[] = [
    { label: 'German', value: 'de' },
    { label: 'English', value: 'en' },
    { label: 'French', value: 'fr' },
  ]

  return (
    <div>
      <h3 className="text-base font-medium">
        Step 1 — Basic Data for Your Account
      </h3>

      <div className="mt-4 space-y-2">
        <div className="flex w-full gap-2">
          <Controller
            name="firstname"
            control={control}
            render={({ field }) => (
              <InputField
                fieldLabel="Firstname"
                placeholder="Firstname"
                {...field}
                disabled={readOnly}
                className="flex-1"
              />
            )}
          />

          <Controller
            name="lastname"
            control={control}
            render={({ field }) => (
              <InputField
                fieldLabel="Lastname"
                placeholder="Lastname"
                {...field}
                disabled={readOnly}
                className="flex-1"
              />
            )}
          />

          <Controller
            name="username"
            control={control}
            render={({ field }) => (
              <InputField
                fieldLabel="Username"
                placeholder="Username"
                {...field}
                disabled={readOnly}
                className="flex-1"
              />
            )}
          />
        </div>

        <div className="flex w-full gap-2">
          <Controller
            name="email"
            control={control}
            rules={{
              required: 'E-Mail is required',
              pattern: {
                value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
                message: 'Invalid e-mail address',
              },
            }}
            render={({ field, fieldState }) => (
              <div className="flex-1">
                <InputField
                  fieldLabel="E-Mail"
                  placeholder="E-Mail"
                  {...field}
                  disabled={readOnly}
                  className="w-full"
                  required
                />
                {fieldState.error && (
                  <p className="mt-1 text-sm text-red-500">
                    {fieldState.error.message}
                  </p>
                )}
              </div>
            )}
          />

          <Controller
            name="phone"
            control={control}
            rules={{
              pattern: {
                value: /^\d+$/,
                message: 'Only numbers are allowed',
              },
            }}
            render={({ field, fieldState }) => (
              <div className="flex-1">
                <InputField
                  fieldLabel="Phone"
                  placeholder="Phone"
                  {...field}
                  disabled={readOnly}
                  className="flex-1"
                />
                {fieldState.error && (
                  <p className="mt-1 text-sm text-red-500">
                    {fieldState.error.message}
                  </p>
                )}
              </div>
            )}
          />
        </div>

        <Controller
          name="preferredLanguage"
          control={control}
          render={({ field: { onChange, value } }) => (
            <ComboBoxField
              fieldId="preferredLanguage"
              fieldLabel="Preferred Language"
              options={languages}
              value={(value ?? null) as any}
              onChange={(v) => onChange(v ?? undefined)}
              disabled={readOnly}
              portalContainer={portalContainerRef}
            />
          )}
        />

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
              <InputField
                fieldLabel="Password"
                type="password"
                placeholder="Enter your password"
                {...field}
                disabled={readOnly}
                className="w-full"
                required
              />
              {fieldState.error && (
                <p className="mt-1 text-sm text-red-500">
                  {fieldState.error.message}
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
            validate: validatePasswordMatch,
          }}
          render={({ field, fieldState }) => (
            <div>
              <InputField
                fieldLabel="Confirm Password"
                type="password"
                placeholder="Confirm your password"
                {...field}
                disabled={readOnly}
                className="w-full"
                required
              />
              {fieldState.error && (
                <p className="mt-1 text-sm text-red-500">
                  {fieldState.error.message}
                </p>
              )}
            </div>
          )}
        />
      </div>
    </div>
  )
}
