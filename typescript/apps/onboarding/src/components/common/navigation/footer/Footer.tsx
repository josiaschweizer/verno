import { Link } from 'react-router'
import { useTranslation } from 'react-i18next'

export default function Footer() {
  const { t, i18n } = useTranslation('lib')

  const changeLanguage = (language: string) => {
    void i18n.changeLanguage(language)
  }

  return (
    <footer className="border-t border-verno-accent/40 text-sm text-muted-foreground">
      <div className="mx-auto max-w-6xl px-6 py-6 flex flex-col md:flex-row items-center justify-between gap-4">
        <p>{t('copyright', { year: new Date().getFullYear() })}</p>

        <div className="flex flex-col sm:flex-row items-center gap-4 sm:gap-6">
          <div className="flex items-center gap-6">
            <Link to="/legal/imprint" className="link-underline-animated">
              {t('links.imprint')}
            </Link>

            <Link to="/legal/privacy" className="link-underline-animated">
              {t('links.privacy')}
            </Link>

            <Link to="/legal/terms" className="link-underline-animated">
              {t('links.terms')}
            </Link>
          </div>

          <div className="flex items-center gap-2">
            <label htmlFor="language-select" className="sr-only">
              {t('language.label')}
            </label>

            <select
              id="language-select"
              value={i18n.resolvedLanguage ?? i18n.language}
              onChange={(event) => changeLanguage(event.target.value)}
              className="rounded-full border border-verno-accent/30 bg-transparent px-3 py-1 text-sm text-verno-darker outline-none focus:border-verno-accent"
            >
              <option value="de">{t('language.de')}</option>
              <option value="en">{t('language.en')}</option>
              <option value="fr">{t('language.fr')}</option>
            </select>
          </div>
        </div>
      </div>
    </footer>
  )
}
