import { useEffect } from 'react'
import { useLocation } from 'react-router-dom'

export default function Product() {
  const location = useLocation()

  useEffect(() => {
    if (location.hash) {
      const id = location.hash.replace('#', '')
      const element = document.getElementById(id)

      if (element) {
        //timeout to ensure that page is already rendered
        setTimeout(() => {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' })
        }, 100)
      }
    }
  }, [location])

  return (
    <main className="min-h-screen bg-verno-bg text-verno-darker">
      <div className="mx-auto max-w-6xl px-4 py-16 space-y-24">
        <section aria-labelledby="verno-hero-title">
          <div className="grid gap-10 md:grid-cols-[minmax(0,1.35fr)_minmax(0,1fr)] items-center">
            <div>
              <h1
                id="verno-hero-title"
                className="text-3xl md:text-4xl font-semibold text-verno-darker"
              >
                Verno for sports clubs
              </h1>
              <p className="mt-4 text-base text-muted-foreground">
                Clear structure, schedules and membership overview for clubs
                running teams, courses and venues.
              </p>
              <p className="mt-3 text-sm text-muted-foreground">
                Designed for coordinators, coaches and club managers who need a
                calm, reliable system for everyday work.
              </p>
              <div className="mt-6 flex flex-wrap items-center gap-4">
                {/* TODO: Primary CTA button */}
                {/* TODO: Secondary CTA link */}
              </div>
            </div>
            <div className="h-64 rounded-2xl bg-verno-surface shadow flex items-center justify-center">
              {/* TODO: Hero illustration / screenshot */}
              <span className="text-sm text-muted-foreground">
                Hero illustration / UI overview
              </span>
            </div>
          </div>
        </section>

        <section aria-labelledby="verno-for-title">
          <div className="grid gap-10 md:grid-cols-[minmax(0,1.35fr)_minmax(0,1fr)] items-start">
            <div>
              <h2
                id="verno-for-title"
                className="text-2xl font-semibold text-verno-darker"
              >
                Built for real club operations
              </h2>
              <p className="mt-3 text-sm text-muted-foreground">
                Verno supports clubs, academies and community organizations that
                run multiple teams, courses and venues.
              </p>
              <p className="mt-2 text-sm text-muted-foreground">
                It fits into the daily work of coordinators, head coaches and
                club managers.
              </p>
              <ul className="mt-4 space-y-2 text-sm text-verno-dark">
                <li>• Multi-team field and court sports</li>
                <li>• Seasonal programs and courses</li>
                <li>• Volunteer or part-time coaches</li>
              </ul>
            </div>
            <div className="space-y-3">
              <div className="rounded-2xl bg-verno-surface p-4 shadow">
                <p className="text-sm font-medium text-verno-darker">
                  Club managers
                </p>
                <p className="mt-1 text-xs text-muted-foreground">
                  Overview of teams, venues and seasons.
                </p>
              </div>
              <div className="rounded-2xl bg-verno-surface p-4 shadow">
                <p className="text-sm font-medium text-verno-darker">
                  Coordinators
                </p>
                <p className="mt-1 text-xs text-muted-foreground">
                  Day-to-day scheduling and changes.
                </p>
              </div>
              <div className="rounded-2xl bg-verno-surface p-4 shadow">
                <p className="text-sm font-medium text-verno-darker">Coaches</p>
                <p className="mt-1 text-xs text-muted-foreground">
                  Clear view of sessions and teams.
                </p>
              </div>
            </div>
          </div>
        </section>

        <section id="organization" aria-labelledby="organization-title">
          <div className="grid gap-10 md:grid-cols-[minmax(0,1.35fr)_minmax(0,1fr)] items-start">
            <div>
              <div className="flex items-center gap-3">
                {/* TODO: Icon for Organization (squares-2x2 / rectangle-group) */}
                <h2
                  id="organization-title"
                  className="text-2xl font-semibold text-verno-darker"
                >
                  Organization
                </h2>
              </div>
              <p className="mt-3 text-sm font-medium text-verno-dark">
                Keep clubs, teams and venues organized.
              </p>
              <p className="mt-2 text-sm text-muted-foreground">
                Designed for clubs with many teams and locations.
              </p>
              <p className="mt-1 text-sm text-muted-foreground">
                Gives you a clear, shared structure for your organization.
              </p>
              <div className="mt-4">
                <h3 className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">
                  Key points
                </h3>
                <ul className="mt-2 space-y-1 text-sm text-verno-dark">
                  <li>• Clear club and team hierarchy</li>
                  <li>• Shared directory for venues and contacts</li>
                  <li>• Templates for seasons and programs</li>
                </ul>
              </div>
              <div className="mt-4 text-xs text-muted-foreground space-y-1">
                <p>
                  Icon suggestion: Heroicon outline <code>squares-2x2</code> or{' '}
                  <code>rectangle-group</code>.
                </p>
                {/* TODO: Swap in actual icon component */}
              </div>
            </div>
            <div className="h-64 rounded-2xl bg-verno-surface shadow p-4 flex flex-col">
              {/* TODO: Organization illustration / screenshot */}
              <div className="flex-1 rounded-xl bg-verno-surface-light border border-transparent flex items-center justify-center">
                <span className="text-xs text-muted-foreground">
                  Organization overview: club, teams, venues
                </span>
              </div>
              <p className="mt-3 text-[11px] text-muted-foreground">
                Composition: main dashboard with a left sidebar and central
                organization tree. Simple cards for club, teams and venues on{' '}
                <code>bg-verno-surface</code>, connections and secondary panels
                on <code>bg-verno-surface-light</code>, text in{' '}
                <code>text-verno-dark</code>, highlights in{' '}
                <code>bg-verno-accent</code>.
              </p>
            </div>
          </div>
        </section>

        <section id="scheduling" aria-labelledby="scheduling-title">
          <div className="grid gap-10 md:grid-cols-[minmax(0,1.35fr)_minmax(0,1fr)] items-start">
            <div>
              <div className="flex items-center gap-3">
                {/* TODO: Icon for Scheduling (calendar-days / clock) */}
                <h2
                  id="scheduling-title"
                  className="text-2xl font-semibold text-verno-darker"
                >
                  Scheduling &amp; Courses
                </h2>
              </div>
              <p className="mt-3 text-sm font-medium text-verno-dark">
                Plan training, matches and courses clearly.
              </p>
              <p className="mt-2 text-sm text-muted-foreground">
                Built for weekly training, fixtures and multi-week courses.
              </p>
              <p className="mt-1 text-sm text-muted-foreground">
                Helps coaches see where they are needed and when.
              </p>
              <div className="mt-4">
                <h3 className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">
                  Key points
                </h3>
                <ul className="mt-2 space-y-1 text-sm text-verno-dark">
                  <li>• Calendar views for teams and venues</li>
                  <li>• Course sessions with instructors and capacity</li>
                  <li>• Filters for training, games, events</li>
                </ul>
              </div>
              <div className="mt-4 text-xs text-muted-foreground space-y-1">
                <p>
                  Icon suggestion: Heroicon outline <code>calendar-days</code>{' '}
                  or <code>clock</code>.
                </p>
                {/* TODO: Swap in actual icon component */}
              </div>
            </div>
            <div className="h-64 rounded-2xl bg-verno-surface shadow p-4 flex flex-col">
              {/* TODO: Scheduling illustration / screenshot */}
              <div className="flex-1 rounded-xl bg-verno-surface-light border border-transparent flex items-center justify-center">
                <span className="text-xs text-muted-foreground">
                  Weekly schedule: training, games, courses
                </span>
              </div>
              <p className="mt-3 text-[11px] text-muted-foreground">
                Composition: central weekly calendar on{' '}
                <code>bg-verno-surface</code> with event blocks on{' '}
                <code>bg-verno-surface-light</code>. Filters and selected events
                highlighted using <code>bg-verno-accent</code>, text in{' '}
                <code>text-verno-dark</code>.
              </p>
            </div>
          </div>
        </section>

        <section id="participants" aria-labelledby="participants-title">
          <div className="grid gap-10 md:grid-cols-[minmax(0,1.35fr)_minmax(0,1fr)] items-start">
            <div>
              <div className="flex items-center gap-3">
                {/* TODO: Icon for Participants (users / identification) */}
                <h2
                  id="participants-title"
                  className="text-2xl font-semibold text-verno-darker"
                >
                  Participants &amp; Memberships
                </h2>
              </div>
              <p className="mt-3 text-sm font-medium text-verno-dark">
                Keep players, guardians and staff aligned.
              </p>
              <p className="mt-2 text-sm text-muted-foreground">
                Brings player profiles, guardians and team roles into one place.
              </p>
              <p className="mt-1 text-sm text-muted-foreground">
                Shows who is active and where they play.
              </p>
              <div className="mt-4">
                <h3 className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">
                  Key points
                </h3>
                <ul className="mt-2 space-y-1 text-sm text-verno-dark">
                  <li>• Profiles for players and guardians</li>
                  <li>• Membership status by team or season</li>
                  <li>• Simple groups for communication lists</li>
                </ul>
              </div>
              <div className="mt-4 text-xs text-muted-foreground space-y-1">
                <p>
                  Icon suggestion: Heroicon outline <code>users</code> or{' '}
                  <code>identification</code>.
                </p>
                {/* TODO: Swap in actual icon component */}
              </div>
            </div>
            <div className="h-64 rounded-2xl bg-verno-surface shadow p-4 flex flex-col gap-3">
              {/* TODO: Participants illustration / screenshot */}
              <div className="flex-1 rounded-xl bg-verno-surface-light flex">
                <div className="w-2/5 border-r border-transparent flex flex-col justify-center items-center">
                  <span className="text-xs text-muted-foreground">
                    Profile card
                  </span>
                </div>
                <div className="w-3/5 flex items-center justify-center">
                  <span className="text-xs text-muted-foreground">
                    Participants list
                  </span>
                </div>
              </div>
              <p className="text-[11px] text-muted-foreground">
                Composition: large profile card and compact list on{' '}
                <code>bg-verno-surface</code>, chips and tags on{' '}
                <code>bg-verno-surface-light</code>, text in{' '}
                <code>text-verno-dark</code>, active membership and main actions
                using <code>bg-verno-accent</code>.
              </p>
            </div>
          </div>
        </section>

        <section id="reporting" aria-labelledby="reporting-title">
          <div className="grid gap-10 md:grid-cols-[minmax(0,1.35fr)_minmax(0,1fr)] items-start">
            <div>
              <div className="flex items-center gap-3">
                {/* TODO: Icon for Reporting (chart-bar / presentation-chart-line) */}
                <h2
                  id="reporting-title"
                  className="text-2xl font-semibold text-verno-darker"
                >
                  Reporting &amp; Insights
                </h2>
              </div>
              <p className="mt-3 text-sm font-medium text-verno-dark">
                See attendance and capacity at a glance.
              </p>
              <p className="mt-2 text-sm text-muted-foreground">
                Focuses on participation across teams, venues and courses.
              </p>
              <p className="mt-1 text-sm text-muted-foreground">
                Helps leaders prepare simple updates for boards and committees.
              </p>
              <div className="mt-4">
                <h3 className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">
                  Key points
                </h3>
                <ul className="mt-2 space-y-1 text-sm text-verno-dark">
                  <li>• Attendance by team and course</li>
                  <li>• Venue and field utilization trends</li>
                  <li>• Exports for board reports</li>
                </ul>
              </div>
              <div className="mt-4 text-xs text-muted-foreground space-y-1">
                <p>
                  Icon suggestion: Heroicon outline <code>chart-bar</code> or{' '}
                  <code>presentation-chart-line</code>.
                </p>
                {/* TODO: Swap in actual icon component */}
              </div>
            </div>
            <div className="h-64 rounded-2xl bg-verno-surface shadow p-4 flex flex-col gap-3">
              {/* TODO: Reporting illustration / screenshot */}
              <div className="flex-1 rounded-xl bg-verno-surface-light p-3 flex flex-col gap-3">
                <div className="h-20 rounded-md bg-verno-bg flex items-center justify-center">
                  <span className="text-xs text-muted-foreground">
                    Attendance chart
                  </span>
                </div>
                <div className="grid grid-cols-3 gap-2">
                  <div className="rounded-md bg-verno-bg py-2 text-center">
                    <span className="text-[11px] text-muted-foreground">
                      Total members
                    </span>
                  </div>
                  <div className="rounded-md bg-verno-bg py-2 text-center">
                    <span className="text-[11px] text-muted-foreground">
                      Avg attendance
                    </span>
                  </div>
                  <div className="rounded-md bg-verno-bg py-2 text-center">
                    <span className="text-[11px] text-muted-foreground">
                      Field usage
                    </span>
                  </div>
                </div>
              </div>
              <p className="text-[11px] text-muted-foreground">
                Composition: dashboard on <code>bg-verno-surface</code> with
                simple chart and metric cards on{' '}
                <code>bg-verno-surface-light</code>. Key data points highlighted
                using <code>bg-verno-accent</code>, labels in{' '}
                <code>text-verno-dark</code>.
              </p>
            </div>
          </div>
        </section>

        <section aria-labelledby="closing-cta-title">
          <div className="rounded-2xl bg-verno-surface px-6 py-8 shadow flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div>
              <h2
                id="closing-cta-title"
                className="text-xl font-semibold text-verno-darker"
              >
                Start with one season and grow
              </h2>
              <p className="mt-2 text-sm text-muted-foreground">
                Begin with a single season, team or venue and add more once your
                staff are comfortable.
              </p>
            </div>
            <div className="flex flex-wrap items-center gap-4">
              {/* TODO: Closing primary CTA (use btn-primary style) */}
              {/* TODO: Optional secondary text link */}
            </div>
          </div>
        </section>
      </div>
    </main>
  )
}
