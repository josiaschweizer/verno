import RegisterDialogFormData from '@/interfaces/register/RegisterDialogFormData'
import { UseFormGetValues } from 'react-hook-form'

interface Props {
  getValues: UseFormGetValues<RegisterDialogFormData>
}

const overviewFields = [
  { key: 'firstname' as const, label: 'Vorname' },
  { key: 'lastname' as const, label: 'Nachname' },
  { key: 'email' as const, label: 'E-Mail' },
  { key: 'tenantSubdomain' as const, label: 'URL' },
  { key: 'tenantName' as const, label: 'Display Name' },
] as const

export function StepThree({ getValues }: Props) {
  const values = getValues()

  return (
    <div>
      <h3 className="text-base font-medium">Step 3 — Overview</h3>

      <div className="mt-4">
        <div className="rounded-xl border border-verno-darker/20 bg-verno-surface p-4">
          <dl className="space-y-3">
            {overviewFields.map(({ key, label }) => (
              <div
                key={key}
                className="flex flex-col gap-0.5 border-b border-verno-darker/10 pb-3 last:border-0 last:pb-0"
              >
                <dt className="text-xs font-medium uppercase tracking-wide text-verno-darker/70">
                  {label}
                </dt>
                <dd className="text-sm text-verno-dark">
                  {values[key] ?? '—'}
                </dd>
              </div>
            ))}
          </dl>
        </div>
      </div>
    </div>
  )
}

export default StepThree
