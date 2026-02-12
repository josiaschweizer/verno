import { useMemo } from 'react'

type Stat = { label: string; value: string }
type Value = { title: string; text: string }

export default function Company() {
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
    <div className="h-full bg-verno-bg text-verno-darker overflow-hidden -m-4">
      <div className="mx-auto h-full max-w-5xl px-4 flex flex-col justify-center">
        <section className="space-y-8">
          <h1 className="text-3xl md:text-4xl font-semibold text-verno-darker">
            About Verno
          </h1>

          <p className="text-sm md:text-base text-muted-foreground max-w-2xl">
            Verno builds modern club software for teams, courses and venues. We
            help sports clubs replace scattered tools with one calm, reliable
            system for scheduling, memberships and everyday coordination.
          </p>

          <div className="grid grid-cols-2 gap-4">
            {stats.map((s) => (
              <div
                key={s.label}
                className="rounded-2xl bg-verno-surface shadow p-6"
              >
                <div className="text-lg md:text-xl font-semibold text-verno-darker">
                  {s.value}
                </div>
                <div className="mt-1 text-xs text-muted-foreground">
                  {s.label}
                </div>
              </div>
            ))}
          </div>

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
        </section>

        <div className="mt-8 shrink-0 flex flex-wrap items-center gap-4 justify-between">
          <p className="text-xs text-muted-foreground">
            Switzerland-first mindset. Long-term partnerships with clubs.
          </p>
          <div className="flex gap-4">
            {/*<a href="#contact" className="btn-primary">*/}
            {/*  Contact*/}
            {/*</a>*/}
            <a
              href="/product#organization"
              className="inline-flex items-center text-sm font-medium text-verno-darker hover:underline"
            >
              Product overview
            </a>
          </div>
        </div>
      </div>
    </div>
  )
}
