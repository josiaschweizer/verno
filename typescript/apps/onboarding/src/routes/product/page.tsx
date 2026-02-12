import { useEffect, useState } from 'react'
import { useLocation } from 'react-router-dom'
import { HoverSplitImage } from '@/components/ui/custom/HoverSplitImage'
import { tenantsApi } from '@/lib/api/tenantsApi'

export default function Product() {
  const location = useLocation()

  const [peopleView, setPeopleView] = useState<'participants' | 'users'>(
    'participants',
  )
  const [organizationView, setOrganizationView] = useState<
    'courseSchedules' | 'courses' | 'instructors' | 'participants'
  >('courseSchedules')

  const [tenantsCount, setTenantsCount] = useState<number | null>(null)
  const [memberCount, setMemberCount] = useState<number | null>(null)
  const [courseCount, setCourseCount] = useState<number | null>(null)

  useEffect(() => {
    let cancelled = false

    // Helper function to safely extract a number from various response formats
    const extractNumber = (value: unknown): number | null => {
      if (typeof value === 'number') return value
      if (typeof value === 'string') {
        const parsed = Number(value)
        return Number.isNaN(parsed) ? null : parsed
      }
      if (value && typeof value === 'object' && !Array.isArray(value)) {
        const obj = value as Record<string, unknown>
        if (typeof obj.count === 'number') {
          return obj.count
        }
        if (typeof obj.total === 'number') {
          return obj.total
        }
        if (typeof obj.value === 'number') {
          return obj.value
        }
      }
      return null
    }

    ;(async () => {
      try {
        const [t, m, c] = await Promise.all([
          tenantsApi.getCountOfTenants(),
          tenantsApi.getTotalMemberCount(),
          tenantsApi.getTotalCourseCount(),
        ])

        if (!cancelled) {
          setTenantsCount(extractNumber(t))
          setMemberCount(extractNumber(m))
          setCourseCount(extractNumber(c))
        }
      } catch (error) {
        console.error('Failed to fetch counts:', error)
        if (!cancelled) {
          setTenantsCount(null)
          setMemberCount(null)
          setCourseCount(null)
        }
      }
    })()

    return () => {
      cancelled = true
    }
  }, [])

  useEffect(() => {
    if (location.hash) {
      const id = location.hash.replace('#', '')
      const element = document.getElementById(id)

      if (element) {
        // timeout to ensure that page is already rendered
        setTimeout(() => {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' })
        }, 100)
      }
    }
  }, [location])

  const organizationConfig: Record<
    'courseSchedules' | 'courses' | 'instructors' | 'participants',
    {
      title: string
      caption: string
      alt: string
      lightSrc: string
      darkSrc: string
    }
  > = {
    courseSchedules: {
      title: 'Schedules',
      caption: 'Plan blocks and weeks clearly.',
      alt: 'Course schedules overview',
      lightSrc: '/course-schedules-light.png',
      darkSrc: '/course-schedules.png',
    },
    courses: {
      title: 'Courses',
      caption: 'Capacity, weekdays, levels and times.',
      alt: 'Courses overview',
      lightSrc: '/courses-light.png',
      darkSrc: '/courses.png',
    },
    instructors: {
      title: 'Instructors',
      caption: 'Directory with contact and assignment info.',
      alt: 'Instructors overview',
      lightSrc: '/instructors-light.png',
      darkSrc: '/instructors.png',
    },
    participants: {
      title: 'Participants',
      caption: 'Fast search, statuses and course links.',
      alt: 'Participants overview',
      lightSrc: '/participants-light.png',
      darkSrc: '/participants.png',
    },
  }

  const activeOrganization = organizationConfig[organizationView]

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

            <div className="rounded-2xl bg-verno-surface shadow p-4">
              <div className="rounded-xl bg-verno-surface-light border border-transparent overflow-hidden aspect-[16/10]">
                <HoverSplitImage
                  lightSrc="/dashboard-light.png"
                  darkSrc="/dashboard.png"
                  alt="Dashboard with course sections, participant tables and quick actions"
                  className="h-full"
                  initialSplit={0.6}
                />
              </div>

              <p className="mt-3 text-[11px] text-muted-foreground">
                Screenshot: dashboard with course sections, sortable participant
                tables and quick actions (report, edit participants). Hover the
                image to compare light and dark mode.
              </p>
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

            <div className="rounded-2xl bg-verno-surface shadow p-4">
              <div className="flex items-center justify-between gap-3 mb-3">
                <p className="text-xs font-medium text-verno-darker">
                  {activeOrganization.title}
                </p>

                <div className="inline-flex rounded-xl bg-verno-bg p-1">
                  <button
                    type="button"
                    onClick={() => setOrganizationView('courseSchedules')}
                    className={[
                      'px-3 py-1.5 text-xs rounded-lg transition',
                      organizationView === 'courseSchedules'
                        ? 'bg-verno-surface shadow text-verno-darker'
                        : 'text-muted-foreground hover:text-verno-darker',
                    ].join(' ')}
                  >
                    Schedules
                  </button>
                  <button
                    type="button"
                    onClick={() => setOrganizationView('courses')}
                    className={[
                      'px-3 py-1.5 text-xs rounded-lg transition',
                      organizationView === 'courses'
                        ? 'bg-verno-surface shadow text-verno-darker'
                        : 'text-muted-foreground hover:text-verno-darker',
                    ].join(' ')}
                  >
                    Courses
                  </button>
                  <button
                    type="button"
                    onClick={() => setOrganizationView('instructors')}
                    className={[
                      'px-3 py-1.5 text-xs rounded-lg transition',
                      organizationView === 'instructors'
                        ? 'bg-verno-surface shadow text-verno-darker'
                        : 'text-muted-foreground hover:text-verno-darker',
                    ].join(' ')}
                  >
                    Instructors
                  </button>
                  <button
                    type="button"
                    onClick={() => setOrganizationView('participants')}
                    className={[
                      'px-3 py-1.5 text-xs rounded-lg transition',
                      organizationView === 'participants'
                        ? 'bg-verno-surface shadow text-verno-darker'
                        : 'text-muted-foreground hover:text-verno-darker',
                    ].join(' ')}
                  >
                    Participants
                  </button>
                </div>
              </div>

              <div className="rounded-xl bg-verno-surface-light border border-transparent overflow-hidden aspect-[16/10]">
                <HoverSplitImage
                  lightSrc={activeOrganization.lightSrc}
                  darkSrc={activeOrganization.darkSrc}
                  alt={activeOrganization.alt}
                  className="h-full"
                  initialSplit={0.6}
                />
              </div>

              <p className="mt-3 text-[11px] text-muted-foreground">
                {activeOrganization.caption} Hover the image to compare light
                and dark mode.
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
              <div className="flex-1 rounded-xl bg-verno-surface-light border border-transparent overflow-hidden">
                <HoverSplitImage
                  lightSrc="/calendar-view-light.png"
                  darkSrc="/calendar-view.png"
                  alt="Weekly schedules overview"
                  className="h-full"
                  initialSplit={0.6}
                />
              </div>
              <p className="mt-3 text-[11px] text-muted-foreground">
                Screenshot: weekly timetable with a clear hour grid, week
                navigation and color-coded sessions (training, courses and
                events). Each block shows the session name and instructor;
                longer sessions (e.g. all-day events) span the full day. A left
                sidebar keeps all club areas accessible while the calendar stays
                the focus.
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
                Brings participant profiles, guardians and team roles into one
                place.
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
                  <li>• Integrated user and role administration</li>
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

            <div className="rounded-2xl bg-verno-surface shadow p-4">
              <div className="flex items-center justify-between gap-3 mb-3">
                <p className="text-xs font-medium text-verno-darker">
                  {peopleView === 'participants'
                    ? 'Participants overview'
                    : 'Roles & users'}
                </p>

                <div className="inline-flex rounded-xl bg-verno-bg p-1">
                  <button
                    type="button"
                    onClick={() => setPeopleView('participants')}
                    className={[
                      'px-3 py-1.5 text-xs rounded-lg transition',
                      peopleView === 'participants'
                        ? 'bg-verno-surface shadow text-verno-darker'
                        : 'text-muted-foreground hover:text-verno-darker',
                    ].join(' ')}
                  >
                    Participants
                  </button>
                  <button
                    type="button"
                    onClick={() => setPeopleView('users')}
                    className={[
                      'px-3 py-1.5 text-xs rounded-lg transition',
                      peopleView === 'users'
                        ? 'bg-verno-surface shadow text-verno-darker'
                        : 'text-muted-foreground hover:text-verno-darker',
                    ].join(' ')}
                  >
                    Roles & Users
                  </button>
                </div>
              </div>

              <div className="rounded-xl bg-verno-surface-light border border-transparent overflow-hidden aspect-[16/10]">
                {peopleView === 'participants' ? (
                  <HoverSplitImage
                    lightSrc="/participants-list-light.png"
                    darkSrc="/participants-list.png"
                    alt="Participants overview table with search, filters and status badges"
                    className="h-full"
                    initialSplit={0.6}
                  />
                ) : (
                  <HoverSplitImage
                    lightSrc="/user-administration-light.png"
                    darkSrc="/user-administration.png"
                    alt="User administration overview with roles and create-user dialog"
                    className="h-full"
                    initialSplit={0.6}
                  />
                )}
              </div>

              <p className="mt-3 text-[11px] text-muted-foreground">
                {peopleView === 'participants'
                  ? 'Directory view with fast search, sortable columns, filters and clear active/inactive badges.'
                  : 'Role-based access with a streamlined create-user flow and clear role/status badges.'}{' '}
                Hover the image to compare light and dark mode.
              </p>
            </div>
          </div>
        </section>

        {tenantsCount > 1 && (
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
                  Helps leaders prepare simple updates for boards and
                  committees.
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
                      <div className="text-sm font-semibold text-verno-darker">
                        {tenantsCount ?? '–'}
                      </div>
                      <div className="text-[11px] text-muted-foreground">
                        Tenants
                      </div>
                    </div>

                    <div className="rounded-md bg-verno-bg py-2 text-center">
                      <div className="text-sm font-semibold text-verno-darker">
                        {memberCount ?? '–'}
                      </div>
                      <div className="text-[11px] text-muted-foreground">
                        Members
                      </div>
                    </div>

                    <div className="rounded-md bg-verno-bg py-2 text-center">
                      <div className="text-sm font-semibold text-verno-darker">
                        {courseCount ?? '–'}
                      </div>
                      <div className="text-[11px] text-muted-foreground">
                        Courses
                      </div>
                    </div>
                  </div>
                </div>
                <p className="text-[11px] text-muted-foreground">
                  Composition: dashboard on <code>bg-verno-surface</code> with
                  simple chart and metric cards on{' '}
                  <code>bg-verno-surface-light</code>. Key data points
                  highlighted using <code>bg-verno-accent</code>, labels in{' '}
                  <code>text-verno-dark</code>.
                </p>
              </div>
            </div>
          </section>
        )}

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
