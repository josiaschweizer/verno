import { Link } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import RevealSection from '@verno/components/ui/RevealSection'
import { createCanonicalLinks, createMeta } from '@/lib/seo'

export function meta() {
  return createMeta(
    'Verno – Vereinsverwaltung für Schweizer Sportvereine',
    'Verno hilft Sportvereinen, Teams, Kurse, Mitglieder und Standorte einfach und sicher zu organisieren.',
  )
}

export function links() {
  return createCanonicalLinks('/')
}

export default function Home() {
  const { t } = useTranslation('home')

  return (
    <div className="flex-1 bg-verno-bg text-verno-darker flex items-start md:items-center">
      <div className="mx-auto w-full max-w-6xl px-5 sm:px-6 py-12 sm:py-14 md:py-0">
        <RevealSection>
          <section className="grid grid-cols-1 md:grid-cols-2 gap-10 sm:gap-12 md:gap-16 items-start md:items-center">
            <div className="flex flex-col">
              <h1 className="text-3xl sm:text-4xl md:text-6xl font-semibold leading-tight underline decoration-verno-accent underline-offset-4 decoration-3 mb-8 sm:mb-10">
                {t('hero.title')}
              </h1>

              <p className="order-1 max-w-lg text-base leading-7 text-muted-foreground sm:text-base sm:leading-7">
                {t('hero.description')}
              </p>

              <div className="order-2 mt-8 overflow-hidden rounded-2xl shadow-lg sm:rounded-3xl md:hidden">
                <img
                  src="/product/landing-page-opi.png"
                  alt={t('hero.imageAlt')}
                  className="h-56 w-full object-cover object-top sm:h-64"
                />
              </div>

              <div className="order-3 mt-8 flex flex-wrap gap-3">
                <span className="inline-flex items-center rounded-full border border-verno-accent/20 bg-verno-accent/10 px-4 py-1.5 text-xs font-medium text-verno-darker">
                  {t('badges.secureByDefault')}
                </span>

                <span className="inline-flex items-center rounded-full border border-verno-accent/20 bg-verno-accent/10 px-4 py-1.5 text-xs font-medium text-verno-darker">
                  {t('badges.switzerlandFirst')}
                </span>

                <span className="inline-flex items-center rounded-full border border-verno-accent/20 bg-verno-accent/10 px-4 py-1.5 text-xs font-medium text-verno-darker">
                  {t('badges.designedForClubs')}
                </span>
              </div>

              <div className="order-4 mt-8 flex flex-col gap-4 sm:mt-10 sm:flex-row sm:items-center sm:gap-6">
                <Link
                  to="/product"
                  className="btn-primary w-full justify-center py-3 sm:w-auto sm:py-2"
                >
                  {t('actions.exploreProduct')}
                </Link>

                <Link
                  to="/company"
                  className="group inline-flex items-center justify-center gap-1.5 py-2.5 text-sm font-medium text-verno-darker/80 transition-colors hover:text-verno-darker sm:justify-start sm:py-0"
                >
                  <span className="relative">
                    {t('actions.aboutVerno')}

                    <span className="absolute -bottom-0.5 left-0 hidden h-px w-full origin-left scale-x-0 bg-verno-accent transition-transform duration-300 group-hover:scale-x-100 sm:block" />
                  </span>

                  <span className="transition-transform duration-300 group-hover:translate-x-0.5">
                    →
                  </span>
                </Link>
              </div>
            </div>

            <div className="hidden overflow-hidden rounded-2xl shadow-lg sm:rounded-3xl md:block">
              <img
                src="/product/landing-page-opi.png"
                alt={t('hero.imageAlt')}
                className="h-64 w-full object-cover object-top lg:h-80"
              />
            </div>
          </section>
        </RevealSection>
      </div>
    </div>
  )
}
