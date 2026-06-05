import RegisterDialogFormData from '@/interfaces/register/RegisterDialogFormData'
import { UseFormGetValues } from 'react-hook-form'
import resolveUsername from '@/components/common/register/steps/resolveUsername'
import { useTranslation } from 'react-i18next'

interface Props {
  getValues: UseFormGetValues<RegisterDialogFormData>
}

export function StepThree({ getValues }: Props) {
  const { t } = useTranslation('contact')
  const values = getValues()

  const overviewFields = [
    { key: 'firstname' as const, label: t('stepThree.fields.firstname') },
    { key: 'lastname' as const, label: t('stepThree.fields.lastname') },
    { key: 'username' as const, label: t('stepThree.fields.username') },
    { key: 'email' as const, label: t('stepThree.fields.email') },
    { key: 'tenantSubdomain' as const, label: t('stepThree.fields.url') },
    { key: 'tenantName' as const, label: t('stepThree.fields.displayName') },
  ] as const

  return (
    <div>
      <h3 className="text-base font-medium">{t('stepThree.title')}</h3>

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
                  {key === 'username'
                    ? (resolveUsername(values) ?? t('stepThree.empty'))
                    : (values[key] ?? t('stepThree.empty'))}
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
