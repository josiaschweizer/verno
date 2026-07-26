import { Link } from 'react-router-dom'
import { useTranslation } from 'react-i18next'

export default function HeaderLogo() {
  const { t } = useTranslation('lib')

  return (
    <Link to="/" className="-m-1.5 p-1.5 flex items-center gap-2">
      <span className="sr-only">{t('header.logo.label')}</span>

      <img
        src="/logos/verno-app.png"
        alt={t('header.logo.alt')}
        className="h-26 w-auto"
        loading="lazy"
      />

      <span className="text-verno-dark font-semibold text-2xl tracking-wide">
        Verno
      </span>
    </Link>
  )
}
