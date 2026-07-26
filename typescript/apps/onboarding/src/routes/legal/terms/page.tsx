import { useTranslation } from 'react-i18next'
import LegalPage from '@/components/common/legal/LegalPage'
import LegalSection from '@/components/common/legal/LegalSection'
import { createCanonicalLinks, createMeta } from '@/lib/seo'

export function meta() {
  return createMeta(
    'Allgemeine Geschäftsbedingungen | Verno',
    'Allgemeine Geschäftsbedingungen für die Nutzung von Verno.',
  )
}

export function links() {
  return createCanonicalLinks('/legal/terms')
}

export default function Terms() {
  const { t } = useTranslation('legal')

  return (
    <LegalPage title={t('terms.title')}>
      <p>{t('terms.intro')}</p>

      <LegalSection title={t('terms.sections.scope.title')}>
        <p>{t('terms.sections.scope.text')}</p>
      </LegalSection>

      <LegalSection title={t('terms.sections.accounts.title')}>
        <p>{t('terms.sections.accounts.text')}</p>
      </LegalSection>

      <LegalSection title={t('terms.sections.acceptableUse.title')}>
        <p>{t('terms.sections.acceptableUse.text')}</p>
      </LegalSection>

      <LegalSection title={t('terms.sections.availability.title')}>
        <p>{t('terms.sections.availability.text')}</p>
      </LegalSection>

      <LegalSection title={t('terms.sections.liability.title')}>
        <p>{t('terms.sections.liability.text')}</p>
      </LegalSection>

      <LegalSection title={t('terms.sections.changes.title')}>
        <p>{t('terms.sections.changes.text')}</p>
      </LegalSection>

      <LegalSection title={t('terms.sections.contact.title')}>
        <p>
          <a
            href={`mailto:${t('terms.sections.contact.email')}`}
            className="text-primary underline"
          >
            {t('terms.sections.contact.email')}
          </a>
        </p>
      </LegalSection>
    </LegalPage>
  )
}
