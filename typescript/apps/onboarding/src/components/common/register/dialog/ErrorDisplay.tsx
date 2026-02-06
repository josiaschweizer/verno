// ErrorDisplay.tsx
import { AlertCircle, X } from 'lucide-react'

interface ErrorDisplayProps {
  title: string
  message?: string
  details?: string[]
  onDismiss: () => void
}

export default function ErrorDisplay({
  title,
  message,
  details,
  onDismiss,
}: ErrorDisplayProps) {
  return (
    <div className="w-full animate-in fade-in slide-in-from-top-2 duration-300">
      <div className="w-full rounded-lg border border-red-200 bg-linear-to-br from-red-50 to-red-50/50 shadow-sm">
        <div className="flex gap-3 p-4">
          <div className="shrink-0">
            <AlertCircle className="h-5 w-5 text-red-600" />
          </div>

          <div className="min-w-0 flex-1">
            <h3 className="text-sm font-semibold text-red-900">{title}</h3>

            {message && (
              <p className="mt-1.5 text-sm leading-relaxed text-red-800/90">
                {message}
              </p>
            )}

            {details && details.length > 0 && (
              <ul className="mt-2 space-y-1">
                {details.map((detail, idx) => (
                  <li
                    key={idx}
                    className="flex items-start gap-2 text-sm text-red-800/90"
                  >
                    <span className="mt-1.5 h-1 w-1 shrink-0 rounded-full bg-red-400" />
                    Y<span className="flex-1">{detail}</span>
                  </li>
                ))}
              </ul>
            )}
          </div>

          <div className="shrink-0">
            <button
              type="button"
              onClick={onDismiss}
              className="rounded-md p-1.5 text-red-400 transition-colors hover:bg-red-100 hover:text-red-600 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-1"
              aria-label="Fehler schließen"
            >
              <X className="h-4 w-4" />
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
