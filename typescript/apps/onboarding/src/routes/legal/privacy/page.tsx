import { useTranslation } from 'react-i18next'
import LegalPage from '@/components/common/legal/LegalPage'
import LegalSection from '@/components/common/legal/LegalSection'
import { createCanonicalLinks, createMeta } from '@/lib/seo'

export function meta() {
  return createMeta(
    'Datenschutzerklärung | Verno',
    'Informationen zum Datenschutz und zur Verarbeitung personenbezogener Daten bei Verno.',
  )
}

export function links() {
  return createCanonicalLinks('/legal/privacy')
}

export default function Privacy() {
  const { t } = useTranslation('legal')

  return (
    <LegalPage title={t('privacy.title')}>
      <p>{t('privacy.intro')}</p>

      <LegalSection title={t('privacy.sections.dataWeCollect.title')}>
        <p>{t('privacy.sections.dataWeCollect.text')}</p>
      </LegalSection>

      <LegalSection title={t('privacy.sections.purpose.title')}>
        <p>{t('privacy.sections.purpose.text')}</p>
      </LegalSection>

      <LegalSection title={t('privacy.sections.hosting.title')}>
        <p>{t('privacy.sections.hosting.text')}</p>
      </LegalSection>

      <LegalSection title={t('privacy.sections.thirdParty.title')}>
        <p>{t('privacy.sections.thirdParty.text')}</p>
      </LegalSection>

      <LegalSection title={t('privacy.sections.security.title')}>
        <p>{t('privacy.sections.security.text')}</p>
      </LegalSection>

      <LegalSection title={t('privacy.sections.rights.title')}>
        <p>{t('privacy.sections.rights.text')}</p>
      </LegalSection>

      <LegalSection title={t('privacy.sections.contact.title')}>
        <p>
          {t('privacy.sections.contact.text')}{' '}
          <a
            href={`mailto:${t('privacy.sections.contact.email')}`}
            className="text-primary underline"
          >
            {t('privacy.sections.contact.email')}
          </a>
        </p>
      </LegalSection>
    </LegalPage>
  )
}
