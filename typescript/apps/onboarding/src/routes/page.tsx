import { Link } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import RevealSection from '@verno/components/ui/RevealSection'

export default function Home() {
  const { t } = useTranslation('home')

  return (
    <div className="flex-1 bg-verno-bg text-verno-darker flex items-start md:items-center">
      <div className="mx-auto w-full max-w-6xl px-4 sm:px-6 py-8 md:py-0">
        <RevealSection>
          <section className="grid grid-cols-1 md:grid-cols-2 gap-6 sm:gap-8 md:gap-16 items-start md:items-center">
            <div className="flex flex-col">
              <h1 className="text-3xl sm:text-4xl md:text-6xl font-semibold leading-tight underline decoration-verno-accent underline-offset-4 decoration-3 mb-6 sm:mb-10">
                {t('hero.title')}
              </h1>

              <p className="text-sm sm:text-base text-muted-foreground max-w-lg order-1">
                {t('hero.description')}
              </p>

              <div className="order-2 mt-6 rounded-2xl sm:rounded-3xl overflow-hidden shadow-lg md:hidden">
                <img
                  src="/landing-page-opi.png"
                  alt={t('hero.imageAlt')}
                  className="w-full h-48 object-cover object-top"
                />
              </div>

              <div className="order-3 mt-6 flex flex-wrap gap-2">
                <span className="inline-flex items-center rounded-full border border-verno-accent/20 bg-verno-accent/10 px-3 py-1 text-xs font-medium text-verno-darker">
                  {t('badges.secureByDefault')}
                </span>

                <span className="inline-flex items-center rounded-full border border-verno-accent/20 bg-verno-accent/10 px-3 py-1 text-xs font-medium text-verno-darker">
                  {t('badges.switzerlandFirst')}
                </span>

                <span className="inline-flex items-center rounded-full border border-verno-accent/20 bg-verno-accent/10 px-3 py-1 text-xs font-medium text-verno-darker">
                  {t('badges.designedForClubs')}
                </span>
              </div>

              <div className="order-4 mt-6 sm:mt-8 flex flex-col sm:flex-row sm:items-center gap-4 sm:gap-6">
                <Link to="/product" className="btn-primary w-full sm:w-auto">
                  {t('actions.exploreProduct')}
                </Link>

                <Link
                  to="/company"
                  className="link-underline-animated inline-flex justify-center sm:justify-start items-center text-sm font-medium py-2.5 sm:py-0"
                >
                  {t('actions.aboutVerno')}
                </Link>
              </div>
            </div>

            <div className="hidden md:block rounded-2xl sm:rounded-3xl overflow-hidden shadow-lg">
              <img
                src="/landing-page-opi.png"
                alt={t('hero.imageAlt')}
                className="w-full h-64 lg:h-80 object-cover object-top"
              />
            </div>
          </section>
        </RevealSection>
      </div>
    </div>
  )
}
