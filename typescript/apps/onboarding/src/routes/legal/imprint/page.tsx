import { useTranslation } from 'react-i18next'
import LegalPage from '@/components/common/legal/LegalPage'
import LegalSection from '@/components/common/legal/LegalSection'
import { createCanonicalLinks, createMeta } from '@/lib/seo'

export function meta() {
  return createMeta(
    'Impressum | Verno',
    'Impressum und Kontaktangaben von Verno.',
  )
}

export function links() {
  return createCanonicalLinks('/legal/imprint')
}

export default function Imprint() {
  const { t } = useTranslation('legal')

  return (
    <LegalPage title={t('imprint.title')}>
      <LegalSection title={t('imprint.provider.title')}>
        <p>
          {t('imprint.provider.name')}
          <br />
          {t('imprint.provider.owner')}
          <br />
          {t('imprint.provider.street')}
          <br />
          {t('imprint.provider.city')}
          <br />
          {t('imprint.provider.country')}
        </p>
      </LegalSection>

      <LegalSection title={t('imprint.contact.title')}>
        <p>
          <a
            href={`mailto:${t('imprint.contact.email')}`}
            className="text-primary underline"
          >
            {t('imprint.contact.email')}
          </a>
        </p>
      </LegalSection>
    </LegalPage>
  )
}
