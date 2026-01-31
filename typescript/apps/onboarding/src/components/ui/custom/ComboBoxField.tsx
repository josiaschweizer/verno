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
  value?: ComboBoxItem | null
  onChange?: (value: ComboBoxItem | null) => void
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
        value={value ?? null}
        onValueChange={(v) => {
          const item =
            v && typeof v === 'object' && 'value' in v
              ? (v as ComboBoxItem)
              : null
          onChange?.(item)
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
