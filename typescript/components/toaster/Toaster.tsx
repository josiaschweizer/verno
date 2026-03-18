import { Toaster as T } from 'sonner'

export default function Toaster() {
  return (
    <T
      position="bottom-right"
      closeButton
      gap={8}
      toastOptions={{
        unstyled: true,
        classNames: {
          toast:
            'relative rounded-2xl px-4 py-3.5 w-full flex items-start gap-3.5 pr-10 border shadow-lg backdrop-blur-sm',
          icon: 'mt-0.5 shrink-0',
          content: 'flex flex-col gap-0.5 min-w-0',
          title: 'text-sm font-semibold leading-snug',
          description: 'text-xs leading-relaxed opacity-75',
          closeButton:
            'absolute right-3 top-3 rounded-lg p-0.5 opacity-50 hover:opacity-100 hover:bg-white/10 transition-all duration-150',

          // Info — warm neutral (passt zu verno-surface + verno-accent)
          info: [
            'bg-[#1e2427]/80 border-[#6b5744]/40 text-[#eae0d5]',
            'dark:bg-[#141a1d]/90 dark:border-[#c6ac8f]/30 dark:text-[#f3ede6]',
          ].join(' '),

          // Error — klares Rot, aber nicht grell
          error: [
            'bg-rose-950/70 border-rose-500/40 text-rose-100',
          ].join(' '),

          // Success — gedämpftes Grün, harmonisch
          success: [
            'bg-emerald-950/70 border-emerald-500/35 text-emerald-100',
          ].join(' '),

          // Warning — Amber, passt zum warmen Accent
          warning: [
            'bg-amber-950/70 border-amber-500/40 text-amber-100',
          ].join(' '),
        },
      }}
    />
  )
}