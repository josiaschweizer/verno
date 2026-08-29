import { useMemo, useState } from 'react'
import { Link } from 'react-router'
import { useTranslation } from 'react-i18next'
import RevealSection from '@verno/components/ui/RevealSection'
import GetInTouchDialog from '@/components/common/contact/GetInTouchDialog'
import { Button } from '@verno/components/ui/button'
import TeamMemberUser from '@/components/common/company/team/TeamMemberUser'
import { TeamMember } from '@/types/company/team/TeamMember'
import { createCanonicalLinks, createMeta } from '@/lib/seo'

export function meta() {
  return createMeta(
    'Über Verno – Kursverwaltung aus der Schweiz',
    'Lerne Verno, unsere Werte und den Schweizer Fokus hinter unserer Kursverwaltung für Vereine kennen.',
  )
}

export function links() {
  return createCanonicalLinks('/company')
}

type Stat = { label: string; value: string }
type Value = { title: string; text: string }

export default function Company() {
  const { t } = useTranslation('company')
  const [contactOpen, setContactOpen] = useState(false)

  const stats = useMemo<Stat[]>(
    () => [
      {
        value: t('stats.headquarters.value'),
        label: t('stats.headquarters.label'),
      },
      {
        value: t('stats.focus.value'),
        label: t('stats.focus.label'),
      },
      {
        value: t('stats.dataBoundary.value'),
        label: t('stats.dataBoundary.label'),
      },
      {
        value: t('stats.team.value'),
        label: t('stats.team.label'),
      },
    ],
    [t],
  )

  const values = useMemo<Value[]>(
    () => [
      {
        title: t('values.clarity.title'),
        text: t('values.clarity.text'),
      },
      {
        title: t('values.trust.title'),
        text: t('values.trust.text'),
      },
      {
        title: t('values.realClubs.title'),
        text: t('values.realClubs.text'),
      },
    ],
    [t],
  )

  const team = useMemo<TeamMember[]>(
    () => [
      {
        name: t('teamSection.members.josia.name'),
        role: t('teamSection.members.josia.role'),
        text: t('teamSection.members.josia.text'),
        image: '/team/josia.jpg',
      },
    ],
    [t],
  )

  return (
    <div className="h-full bg-verno-bg text-verno-darker overflow-y-auto md:overflow-hidden">
      <div className="mx-auto min-h-full max-w-6xl px-6 sm:px-6 pt-12 sm:pt-20 md:pt-24 pb-8 flex flex-col md:justify-center">
        <section className="space-y-6 md:space-y-8">
          <RevealSection>
            <h1 className="text-2xl sm:text-3xl md:text-4xl font-semibold text-verno-darker">
              {t('hero.title')}
            </h1>
            <p className="mt-2 text-sm text-muted-foreground max-w-2xl">
              {t('hero.description')}
            </p>
          </RevealSection>

          <RevealSection stagger={50}>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4">
              {stats.map((s) => (
                <div
                  key={s.label}
                  className="rounded-2xl bg-verno-surface shadow p-4 sm:p-6"
                >
                  <div className="text-base sm:text-lg md:text-xl font-semibold text-verno-darker">
                    {s.value}
                  </div>
                  <div className="mt-1 text-xs text-muted-foreground">
                    {s.label}
                  </div>
                </div>
              ))}
            </div>
          </RevealSection>

          <RevealSection stagger={80}>
            <div className="grid gap-4">
              {values.map((v) => (
                <div
                  key={v.title}
                  className="rounded-2xl bg-verno-surface shadow p-6"
                >
                  <div className="text-sm font-semibold text-verno-darker">
                    {v.title}
                  </div>
                  <p className="mt-2 text-sm text-muted-foreground">{v.text}</p>
                </div>
              ))}
            </div>
          </RevealSection>

          <RevealSection stagger={90}>
            <h2 className="text-lg sm:text-xl font-semibold text-verno-darker">
              {t('teamSection.title')}
            </h2>
            <div
              className={`mt-3 grid gap-3 sm:gap-4 ${
                team.length === 1 ? 'grid-cols-1' : 'grid-cols-1 sm:grid-cols-2'
              }`}
            >
              {team.map((m) => (
                <TeamMemberUser
                  key={m.name}
                  member={m}
                  featured={team.length === 1}
                />
              ))}
            </div>
          </RevealSection>
        </section>

        <RevealSection stagger={100}>
          <div className="mt-6 sm:mt-8 shrink-0 flex flex-col sm:flex-row sm:items-center gap-4 justify-between">
            <p className="text-xs text-muted-foreground order-1 sm:order-2">
              {t('footer.text')}
            </p>
            <div className="flex flex-col sm:flex-row gap-4 sm:gap-6 md:gap-8 order-2 sm:order-1">
              <Button onClick={() => setContactOpen(true)}>
                {t('buttons.getInTouch')} <span aria-hidden="true">&rarr;</span>
              </Button>

              <Link
                to="/product#organization"
                className="inline-flex h-9 items-center justify-center gap-2 whitespace-nowrap rounded-md px-4 py-2 text-sm font-medium text-verno-darker border border-verno-accent/30 hover:bg-verno-surface-light transition-colors active:bg-verno-surface-light/70"
              >
                {t('buttons.productOverview')}
              </Link>
            </div>
          </div>
        </RevealSection>
      </div>

      <GetInTouchDialog
        open={contactOpen}
        onClose={() => setContactOpen(false)}
      />
    </div>
  )
}
