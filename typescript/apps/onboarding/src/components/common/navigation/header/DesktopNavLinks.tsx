import { Link } from 'react-router'
import { useTranslation } from 'react-i18next'

export default function DesktopNavLinks() {
  const { t } = useTranslation('lib')

  return (
    <>
      <Link
        to="/company"
        className="link-underline-animated text-sm/6 font-semibold text-verno-dark hover:text-verno-dark-hover"
      >
        {t('header.navigation.company')}
      </Link>

      <Link
        to="/pricing"
        className="link-underline-animated text-sm/6 font-semibold text-verno-dark hover:text-verno-dark-hover"
      >
        {t('header.navigation.pricing')}
      </Link>
    </>
  )
}
