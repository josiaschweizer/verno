import { Toaster as T } from 'sonner'

export default function Toaster() {
  return (
    <T
      position="bottom-right"
      toastOptions={{
        unstyled: true,
        classNames: {
          toast: 'rounded-xl p-3 w-full flex items-center gap-3 pl-5 border',
          title: 'text-base',
          description: 'text-sm',
          info: 'bg-verno-surface border-verno-dark/60 text-verno-dark/80',
          error: 'bg-red-900/40 border-red-600/50 text-red-200',
          success: 'bg-green-900/40 border-green-600/50 text-green-200',
        },
      }}
    />
  )
}
