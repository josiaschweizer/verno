import { useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import RevealSection from '@verno/components/ui/RevealSection'
import GetInTouchDialog from '@/components/common/contact/GetInTouchDialog'

type Stat = { label: string; value: string }
type Value = { title: string; text: string }

export default function Company() {
  const [contactOpen, setContactOpen] = useState(false)

  const stats = useMemo<Stat[]>(
    () => [
      { value: 'St. Gallen, Switzerland', label: 'Headquarters' },
      { value: 'Sports clubs & Sport Schools', label: 'Focus' },
      { value: 'CH-first', label: 'Data boundary' },
      {
        value: 'Small team',
        label: 'Built with care (and yes, we hate programming typescript)',
      },
    ],
    [],
  )

  const values = useMemo<Value[]>(
    () => [
      {
        title: 'Clarity over complexity',
        text: 'We design calm workflows that stay consistent across teams, seasons and venues.',
      },
      {
        title: 'Trust and stability',
        text: 'Reliable foundations, secure defaults and predictable operations for everyday use.',
      },
      {
        title: 'Built for real clubs',
        text: 'Verno is shaped by the day-to-day needs of coordinators, coaches and administrators.',
      },
    ],
    [],
  )

  return (
    <div className="h-full bg-verno-bg text-verno-darker overflow-y-auto md:overflow-hidden">
      <div className="mx-auto min-h-full max-w-5xl px-4 sm:px-6 pt-12 sm:pt-20 md:pt-24 pb-8 flex flex-col md:justify-center">
        <section className="space-y-6 md:space-y-8">
          <RevealSection>
            <h1 className="text-2xl sm:text-3xl md:text-4xl font-semibold text-verno-darker">
              About Verno
            </h1>
            <p className="mt-2 text-sm text-muted-foreground max-w-2xl">
              Verno builds modern club software for teams, courses and venues.
              We help sports clubs replace scattered tools with one calm,
              reliable system for scheduling, memberships and everyday
              coordination.
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
        </section>

        <RevealSection stagger={100}>
          <div className="mt-6 sm:mt-8 shrink-0 flex flex-col sm:flex-row sm:items-center gap-4 justify-between">
            <p className="text-xs text-muted-foreground order-1 sm:order-2">
              Switzerland-first mindset. Long-term partnerships with clubs.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 sm:gap-6 md:gap-8 order-2 sm:order-1">
              <button
                onClick={() => setContactOpen(true)}
                className="inline-flex justify-center sm:justify-start items-center gap-2 px-4 py-2.5 rounded-lg text-sm font-medium text-verno-darker border border-verno-accent/30 hover:bg-verno-surface-light transition-colors active:bg-verno-surface-light/70"
              >
                Get in touch <span aria-hidden="true">&rarr;</span>
              </button>

              <Link
                to="/product#organization"
                className="inline-flex justify-center sm:justify-start items-center gap-2 px-4 py-2.5 rounded-lg text-sm font-medium text-verno-darker border border-verno-accent/30 hover:bg-verno-surface-light transition-colors active:bg-verno-surface-light/70"
              >
                Product overview
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
