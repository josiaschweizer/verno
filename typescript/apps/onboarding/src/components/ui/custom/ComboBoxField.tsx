import { Field, FieldDescription, FieldLabel } from '@/components/ui/field'
import {
  ComboboxContent,
  ComboboxEmpty,
  ComboboxInput,
  ComboboxItem,
  ComboboxList,
  ComboboxRoot,
  ComboboxTrigger,
} from '@/components/ui/combobox'
import { ComboBoxItem } from '@/type/ComboBoxItem'
import { cn } from '@/lib/utils'
import { RefObject } from 'react'

type Props = {
  fieldLabel?: string
  fieldDescription?: string
  fieldId?: string
  options: ComboBoxItem[]
  // allow string also because some consumers may pass raw string values
  value?: ComboBoxItem | string | null
  onChange?: (value: ComboBoxItem | string | null) => void
  placeholder?: string
  disabled?: boolean
  className?: string
  portalContainer?:
    | HTMLElement
    | RefObject<HTMLElement | null> /** Container for the dropdown portal - use when combobox is inside a Dialog to avoid inert issues */
}

export function ComboBoxField({
  fieldLabel,
  fieldDescription,
  fieldId,
  options,
  value,
  onChange,
  placeholder = 'Select...',
  disabled = false,
  className,
  portalContainer,
}: Props) {
  const hasLabelOrDescription = fieldLabel != null || fieldDescription != null

  const comboboxContent = (
    <div className="relative w-full">
      <ComboboxRoot<ComboBoxItem>
        value={(value as ComboBoxItem) ?? null}
        onValueChange={(v) => {
          // v can be a ComboBoxItem or string depending on the underlying combobox impl
          if (v && typeof v === 'object' && 'value' in v) {
            onChange?.(v as ComboBoxItem)
            return
          }

          if (typeof v === 'string') {
            // try to map string to an option by value
            const found = options.find((o) => o.value === v) || null
            onChange?.(found)
            return
          }

          onChange?.(null)
        }}
        items={options}
        disabled={disabled}
        isItemEqualToValue={(a, b) =>
          (a as ComboBoxItem)?.value === (b as ComboBoxItem)?.value
        }
      >
        <ComboboxInput
          id={fieldId}
          placeholder={placeholder}
          aria-label={fieldLabel}
        />
        <ComboboxTrigger />
        <ComboboxContent container={portalContainer}>
          <ComboboxEmpty>No results found.</ComboboxEmpty>
          <ComboboxList>
            {(item: ComboBoxItem) => (
              <ComboboxItem key={item.value} value={item}>
                {item.label}
              </ComboboxItem>
            )}
          </ComboboxList>
        </ComboboxContent>
      </ComboboxRoot>
    </div>
  )

  if (!hasLabelOrDescription) {
    return <div className={cn('w-full', className)}>{comboboxContent}</div>
  }

  return (
    <Field className={cn(className)}>
      {fieldLabel != null && (
        <FieldLabel htmlFor={fieldId}>{fieldLabel}</FieldLabel>
      )}
      {comboboxContent}
      {fieldDescription != null && (
        <FieldDescription>{fieldDescription}</FieldDescription>
      )}
    </Field>
  )
}
