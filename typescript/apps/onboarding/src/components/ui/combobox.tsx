'use client'

import * as React from 'react'
import { Combobox } from '@base-ui/react/combobox'
import { ChevronDownIcon } from 'lucide-react'
import { cn } from '@/lib/utils'

const inputBaseClasses =
  'flex h-9 w-full rounded-md border border-input bg-transparent px-3 py-1 text-base shadow-sm transition-colors placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50 md:text-sm'

function ComboboxRoot<T>(
  props: React.ComponentProps<typeof Combobox.Root<T>>
) {
  return <Combobox.Root {...props} />
}

function ComboboxInput({
  className,
  ...props
}: React.ComponentProps<typeof Combobox.Input>) {
  return (
    <Combobox.Input
      className={cn(inputBaseClasses, 'pr-9', className)}
      {...props}
    />
  )
}

function ComboboxTrigger({
  className,
  ...props
}: React.ComponentProps<typeof Combobox.Trigger>) {
  return (
    <Combobox.Trigger
      className={cn(
        'absolute right-0 top-0 z-10 flex h-9 cursor-pointer items-center justify-center rounded-r-md px-2 text-muted-foreground hover:text-foreground data-[disabled]:pointer-events-none data-[disabled]:opacity-50',
        className
      )}
      {...props}
    >
      <ChevronDownIcon className="size-4" />
    </Combobox.Trigger>
  )
}

function ComboboxContent({
  className,
  children,
  container,
  ...props
}: React.ComponentProps<'div'> & {
  children: React.ReactNode
  container?: HTMLElement | React.RefObject<HTMLElement | null>
}) {
  return (
    <Combobox.Portal container={container}>
      <Combobox.Positioner className="z-[200]" sideOffset={4}>
        <Combobox.Popup
          className={cn(
            'max-h-60 w-[var(--anchor-width)] overflow-auto rounded-md border border-input bg-popover p-1 text-popover-foreground shadow-md',
            className
          )}
          {...props}
        >
          {children}
        </Combobox.Popup>
      </Combobox.Positioner>
    </Combobox.Portal>
  )
}

function ComboboxEmpty({
  className,
  ...props
}: React.ComponentProps<typeof Combobox.Empty>) {
  return (
    <Combobox.Empty
      className={cn(
        'py-6 text-center text-sm text-muted-foreground [&:empty]:hidden [&:empty]:p-0 [&:empty]:min-h-0',
        className
      )}
      {...props}
    />
  )
}

function ComboboxList({
  children,
  className,
  ...props
}: React.ComponentProps<typeof Combobox.List>) {
  return (
    <Combobox.List className={cn('flex flex-col gap-0.5', className)} {...props}>
      {children}
    </Combobox.List>
  )
}

function ComboboxItem({
  className,
  children,
  ...props
}: React.ComponentProps<typeof Combobox.Item>) {
  return (
    <Combobox.Item
      className={cn(
        'relative flex cursor-pointer select-none items-center rounded-sm px-2 py-1.5 text-sm outline-none transition-colors data-[highlighted]:bg-accent data-[highlighted]:text-accent-foreground data-[selected]:bg-accent data-[selected]:text-accent-foreground data-[disabled]:pointer-events-none data-[disabled]:opacity-50',
        className
      )}
      {...props}
    >
      {children}
    </Combobox.Item>
  )
}

export {
  ComboboxRoot,
  ComboboxInput,
  ComboboxTrigger,
  ComboboxContent,
  ComboboxEmpty,
  ComboboxList,
  ComboboxItem,
}
