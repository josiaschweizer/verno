import { useEffect, useMemo, useState } from 'react'
import { useLocation } from 'react-router-dom'
import { HoverSplitImage } from '@/components/ui/custom/HoverSplitImage'
import RevealSection from '@/components/ui/RevealSection'
import { tenantsApi } from '@/lib/api/tenantsApi'

type PeopleView = 'participants' | 'users'
type OrganizationView =
  | 'courseSchedules'
  | 'courses'
  | 'instructors'
  | 'participants'

type OrganizationItem = {
  title: string
  caption: string
  alt: string
  lightSrc: string
  darkSrc: string
}

const organizationConfig: Record<OrganizationView, OrganizationItem> = {
  courseSchedules: {
    title: 'Schedules',
    caption: 'Plan weeks and sessions with clarity.',
    alt: 'Course schedules overview',
    lightSrc: '/course-schedules-light.png',
    darkSrc: '/course-schedules.png',
  },
  courses: {
    title: 'Courses',
    caption: 'Capacity, levels, weekdays and times in one place.',
    alt: 'Courses overview',
    lightSrc: '/courses-light.png',
    darkSrc: '/courses.png',
  },
  instructors: {
    title: 'Instructors',
    caption: 'Contacts and assignments, always up to date.',
    alt: 'Instructors overview',
    lightSrc: '/instructors-light.png',
    darkSrc: '/instructors.png',
  },
  participants: {
    title: 'Participants',
    caption: 'Search, statuses and direct links to courses.',
    alt: 'Participants overview',
    lightSrc: '/participants-light.png',
    darkSrc: '/participants.png',
  },
}

function formatCount(value: number | null): string {
  if (value == null) return '–'
  return Intl.NumberFormat().format(value)
}

function extractNumber(value: unknown): number | null {
  if (typeof value === 'number') return value
  if (typeof value === 'string') {
    const parsed = Number(value)
    return Number.isNaN(parsed) ? null : parsed
  }
  if (value && typeof value === 'object' && !Array.isArray(value)) {
    const obj = value as Record<string, unknown>
    if (typeof obj.count === 'number') return obj.count
    if (typeof obj.total === 'number') return obj.total
    if (typeof obj.value === 'number') return obj.value
  }
  return null
}

export default function Product() {
  const location = useLocation()

  const [peopleView, setPeopleView] = useState<PeopleView>('participants')
  const [organizationView, setOrganizationView] =
    useState<OrganizationView>('courseSchedules')

  const [tenantsCount, setTenantsCount] = useState<number | null>(null)
  const [memberCount, setMemberCount] = useState<number | null>(null)
  const [courseCount, setCourseCount] = useState<number | null>(null)

  useEffect(() => {
    let cancelled = false

    ;(async () => {
      try {
        const [t, m, c] = await Promise.all([
          tenantsApi.getCountOfTenants(),
          tenantsApi.getTotalMemberCount(),
          tenantsApi.getTotalCourseCount(),
        ])

        if (cancelled) return

        setTenantsCount(extractNumber(t))
        setMemberCount(extractNumber(m))
        setCourseCount(extractNumber(c))
      } catch {
        if (cancelled) return
        setTenantsCount(null)
        setMemberCount(null)
        setCourseCount(null)
      }
    })()

    return () => {
      cancelled = true
    }
  }, [])

  useEffect(() => {
    if (!location.hash) return

    const id = location.hash.replace('#', '')
    let cancelled = false

    const scrollToElement = () => {
      const element = document.getElementById(id)
      if (element) {
        element.scrollIntoView({ behavior: 'smooth', block: 'start' })
        return true
      }

      return false
    }

    const tryScroll = (attempt = 0) => {
      if (cancelled) {
        return
      } else if (scrollToElement()) {
        return
      } else if (attempt >= 8) {
        return
      }

      const delay = 50 + attempt * 100
      const t = window.setTimeout(() => tryScroll(attempt + 1), delay)
      return () => window.clearTimeout(t)
    }

    const t = window.setTimeout(() => tryScroll(), 150)

    return () => {
      cancelled = true
      window.clearTimeout(t)
    }
  }, [location, tenantsCount])

  const activeOrganization = useMemo(
    () => organizationConfig[organizationView],
    [organizationView],
  )

  const showReporting = tenantsCount != null && tenantsCount > 1

  return (
    <main className="min-h-screen bg-verno-bg text-verno-darker overflow-x-hidden">
      <div className="mx-auto max-w-6xl px-4 py-16 space-y-24">
        <section aria-labelledby="verno-hero-title">
          <RevealSection stagger={0}>
            <div className="grid gap-10 md:grid-cols-[minmax(0,1.35fr)_minmax(0,1fr)] items-center">
              <div>
                <h1
                  id="verno-hero-title"
                  className="text-3xl md:text-4xl font-semibold text-verno-darker"
                >
                  Verno for sports clubs
                </h1>
                <p className="mt-4 text-sm text-muted-foreground">
                  Clear structure, schedules and membership overview for clubs
                  running teams, courses and venues.
                </p>
                <p className="mt-2 text-sm text-muted-foreground">
                  Built for coordinators, coaches and club managers who want a
                  calm, reliable system for everyday operations.
                </p>
              </div>

              <div className="rounded-2xl bg-verno-surface shadow p-4">
                <div className="rounded-xl bg-verno-surface-light border border-transparent overflow-hidden aspect-16/10">
                  <HoverSplitImage
                    lightSrc="/dashboard-light.png"
                    darkSrc="/dashboard.png"
                    alt="Dashboard with course sections, participant tables and quick actions"
                    className="h-full"
                    initialSplit={0.6}
                  />
                </div>
                <p className="mt-3 text-xs text-muted-foreground">
                  Dashboard overview with course sections, sortable tables and
                  quick actions. Hover to compare light and dark mode.
                </p>
              </div>
            </div>
          </RevealSection>
        </section>

        <section id="organization" aria-labelledby="organization-title">
          <RevealSection stagger={30}>
            <div className="grid gap-10 md:grid-cols-[minmax(0,1.35fr)_minmax(0,1fr)] items-start">
              <div>
                <h2
                  id="organization-title"
                  className="text-2xl font-semibold text-verno-darker"
                >
                  Organization
                </h2>
                <p className="mt-3 text-sm font-semibold uppercase text-verno-dark">
                  Keep clubs, teams and venues organized.
                </p>
                <p className="mt-2 text-sm text-muted-foreground">
                  Designed for clubs with many teams and locations.
                </p>
                <p className="mt-1 text-sm text-muted-foreground">
                  A shared structure that stays consistent across seasons.
                </p>

                <div className="mt-4">
                  <p className="text-xs uppercase text-muted-foreground">
                    Key points
                  </p>
                  <ul className="mt-2 space-y-1 text-sm text-verno-dark">
                    <li>• Clear club and team hierarchy</li>
                    <li>• Shared directory for venues and contacts</li>
                    <li>• Templates for seasons and programs</li>
                  </ul>
                </div>
              </div>

              <div className="rounded-2xl bg-verno-surface shadow p-4">
                <div className="flex items-center justify-between gap-3 mb-3">
                  <p className="text-xs text-verno-darker shrink-0">
                    {activeOrganization.title}
                  </p>

                  <select
                    value={organizationView}
                    onChange={(e) =>
                      setOrganizationView(e.target.value as OrganizationView)
                    }
                    className="md:hidden text-xs rounded-lg bg-verno-bg border border-border px-3 py-1.5 text-verno-darker focus:outline-none focus:ring-2 focus:ring-ring"
                  >
                    <option value="courseSchedules">Schedules</option>
                    <option value="courses">Courses</option>
                    <option value="instructors">Instructors</option>
                    <option value="participants">Participants</option>
                  </select>

                  <div className="hidden md:inline-flex rounded-xl bg-verno-bg p-1 shrink-0">
                    {(
                      [
                        'courseSchedules',
                        'courses',
                        'instructors',
                        'participants',
                      ] as OrganizationView[]
                    ).map((view) => (
                      <button
                        key={view}
                        type="button"
                        onClick={() => setOrganizationView(view)}
                        className={[
                          'px-3 py-1.5 text-xs rounded-lg transition',
                          organizationView === view
                            ? 'bg-verno-surface shadow text-verno-darker'
                            : 'text-muted-foreground hover:text-verno-darker',
                        ].join(' ')}
                      >
                        {organizationConfig[view].title}
                      </button>
                    ))}
                  </div>
                </div>

                <div className="rounded-xl bg-verno-surface-light border border-transparent overflow-hidden aspect-16/10">
                  <HoverSplitImage
                    lightSrc={activeOrganization.lightSrc}
                    darkSrc={activeOrganization.darkSrc}
                    alt={activeOrganization.alt}
                    className="w-full h-full"
                    initialSplit={0.6}
                    objectFit="contain"
                  />
                </div>

                <p className="mt-3 text-xs text-muted-foreground">
                  {activeOrganization.caption} Hover to compare light and dark
                  mode.
                </p>
              </div>
            </div>
          </RevealSection>
        </section>

        <section id="scheduling" aria-labelledby="scheduling-title">
          <RevealSection stagger={60}>
            <div className="grid gap-10 md:grid-cols-[minmax(0,1.35fr)_minmax(0,1fr)] items-start">
              <div>
                <h2
                  id="scheduling-title"
                  className="text-2xl font-semibold text-verno-darker"
                >
                  Scheduling &amp; Courses
                </h2>
                <p className="mt-3 text-sm font-semibold uppercase text-verno-dark">
                  Plan training, matches and courses clearly.
                </p>
                <p className="mt-2 text-sm text-muted-foreground">
                  Built for weekly training, fixtures and multi-week courses.
                </p>
                <p className="mt-1 text-sm text-muted-foreground">
                  Coaches can see where they are needed and when.
                </p>

                <div className="mt-4">
                  <p className="text-xs uppercase text-muted-foreground">
                    Key points
                  </p>
                  <ul className="mt-2 space-y-1 text-sm text-verno-dark">
                    <li>• Calendar views for teams and venues</li>
                    <li>• Sessions with instructors and capacity</li>
                    <li>• Filters for training, games and events</li>
                  </ul>
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
                <p className="mt-3 text-xs text-muted-foreground">
                  Weekly timetable with a clear hour grid, week navigation and
                  color-coded sessions. Hover to compare light and dark mode.
                </p>
              </div>
            </div>
          </RevealSection>
        </section>

        <section id="participants" aria-labelledby="participants-title">
          <RevealSection stagger={90}>
            <div className="grid gap-10 md:grid-cols-[minmax(0,1.35fr)_minmax(0,1fr)] items-start">
              <div>
                <h2
                  id="participants-title"
                  className="text-2xl font-semibold text-verno-darker"
                >
                  Participants &amp; Memberships
                </h2>
                <p className="mt-3 text-sm font-semibold uppercase text-verno-dark">
                  Keep players, guardians and staff aligned.
                </p>
                <p className="mt-2 text-sm text-muted-foreground">
                  Profiles, guardians and team roles in one place.
                </p>
                <p className="mt-1 text-sm text-muted-foreground">
                  See who is active and where they train.
                </p>

                <div className="mt-4">
                  <p className="text-xs uppercase text-muted-foreground">
                    Key points
                  </p>
                  <ul className="mt-2 space-y-1 text-sm text-verno-dark">
                    <li>• Profiles for participants and guardians</li>
                    <li>• Membership status by team or season</li>
                    <li>• Integrated user and role administration</li>
                  </ul>
                </div>
              </div>

              <div className="rounded-2xl bg-verno-surface shadow p-4">
                <div className="flex items-center justify-between gap-3 mb-3">
                  <p className="text-xs text-verno-darker shrink-0">
                    {peopleView === 'participants'
                      ? 'Participants overview'
                      : 'Roles & users'}
                  </p>

                  <select
                    value={peopleView}
                    onChange={(e) =>
                      setPeopleView(e.target.value as PeopleView)
                    }
                    className="md:hidden text-xs rounded-lg bg-verno-bg border border-border px-3 py-1.5 text-verno-darker focus:outline-none focus:ring-2 focus:ring-ring"
                  >
                    <option value="participants">Participants</option>
                    <option value="users">Roles &amp; Users</option>
                  </select>

                  <div className="hidden md:inline-flex rounded-xl bg-verno-bg p-1 shrink-0">
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
                      Roles &amp; Users
                    </button>
                  </div>
                </div>

                <div className="rounded-xl bg-verno-surface-light border border-transparent overflow-hidden aspect-16/10">
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

                <p className="mt-3 text-xs text-muted-foreground">
                  {peopleView === 'participants'
                    ? 'Fast search, sortable columns and clear active/inactive status.'
                    : 'Role-based access with a streamlined user creation flow.'}{' '}
                  Hover to compare light and dark mode.
                </p>
              </div>
            </div>
          </RevealSection>
        </section>

        <section id="mailing" aria-labelledby="mailing-title">
          <RevealSection stagger={120}>
            <div className="grid gap-10 md:grid-cols-[minmax(0,1.35fr)_minmax(0,1fr)] items-start">
              <div>
                <h2
                  id="mailing-title"
                  className="text-2xl font-semibold text-verno-darker"
                >
                  Mail Delivery &amp; Communication
                </h2>
                <p className="mt-3 text-sm font-semibold uppercase text-verno-dark">
                  Reach participants, guardians and instructors quickly.
                </p>
                <p className="mt-2 text-sm text-muted-foreground">
                  Send targeted emails for course updates, reminders and
                  important club communication.
                </p>
                <p className="mt-1 text-sm text-muted-foreground">
                  Keep templates, recipients and delivery history in one
                  structured place.
                </p>

                <div className="mt-4">
                  <p className="text-xs uppercase text-muted-foreground">
                    Key points
                  </p>
                  <ul className="mt-2 space-y-1 text-sm text-verno-dark">
                    <li>• Email templates for recurring communication</li>
                    <li>• Send to courses, groups or selected recipients</li>
                    <li>• Delivery logs with status and error visibility</li>
                  </ul>
                </div>
              </div>

              <div className="rounded-2xl bg-verno-surface shadow p-4">
                <div className="rounded-xl bg-verno-surface-light border border-transparent overflow-hidden aspect-16/10">
                  <HoverSplitImage
                    lightSrc="/mail-overview-light.png"
                    darkSrc="/mail-overview.png"
                    alt="Mail overview with templates, recipients and delivery log"
                    className="w-full h-full"
                    initialSplit={0.6}
                    objectFit="contain"
                  />
                </div>
                <p className="mt-3 text-xs text-muted-foreground">
                  Manage templates, send messages to the right audience and
                  review delivery history. Hover to compare light and dark mode.
                </p>
              </div>
            </div>
          </RevealSection>
        </section>

        {showReporting && (
          <section id="reporting" aria-labelledby="reporting-title">
            <RevealSection stagger={150}>
              <div className="grid gap-10 md:grid-cols-[minmax(0,1.35fr)_minmax(0,1fr)] items-start">
                <div>
                  <h2
                    id="reporting-title"
                    className="text-2xl font-semibold text-verno-darker"
                  >
                    Reporting &amp; Insights
                  </h2>
                  <p className="mt-3 text-sm font-semibold uppercase text-verno-dark">
                    Track participation and capacity trends.
                  </p>
                  <p className="mt-2 text-sm text-muted-foreground">
                    Simple metrics to understand attendance patterns and
                    resource usage across your organization.
                  </p>
                  <p className="mt-1 text-sm text-muted-foreground">
                    Export data for board meetings and season planning.
                  </p>

                  <div className="mt-4">
                    <p className="text-xs uppercase text-muted-foreground">
                      Key points
                    </p>
                    <ul className="mt-2 space-y-1 text-sm text-verno-dark">
                      <li>• Attendance and enrollment tracking</li>
                      <li>• Venue and resource utilization</li>
                      <li>• Season-over-season comparisons</li>
                    </ul>
                  </div>
                </div>

                <div className="rounded-2xl bg-verno-surface shadow p-4">
                  <div className="space-y-3">
                    <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
                      <div className="rounded-xl bg-verno-surface-light p-4 border border-verno-accent/10">
                        <div className="text-2xl font-semibold text-verno-darker">
                          {formatCount(tenantsCount)}
                        </div>
                        <div className="text-xs text-muted-foreground mt-1">
                          Active clubs
                        </div>
                      </div>

                      <div className="rounded-xl bg-verno-surface-light p-4 border border-verno-accent/10">
                        <div className="text-2xl font-semibold text-verno-darker">
                          {formatCount(memberCount)}
                        </div>
                        <div className="text-xs text-muted-foreground mt-1">
                          Total participants
                        </div>
                      </div>

                      <div className="rounded-xl bg-verno-surface-light p-4 border border-verno-accent/10">
                        <div className="text-2xl font-semibold text-verno-darker">
                          {formatCount(courseCount)}
                        </div>
                        <div className="text-xs text-muted-foreground mt-1">
                          Running courses
                        </div>
                      </div>
                    </div>

                    <div className="rounded-xl bg-verno-surface-light p-4 border border-transparent">
                      <div className="flex items-center justify-between mb-3">
                        <p className="text-xs text-verno-darker">
                          Attendance overview
                        </p>
                        <span className="text-xs text-muted-foreground">
                          Last 6 months
                        </span>
                      </div>

                      <div className="flex items-end justify-between gap-2 h-32">
                        {[65, 78, 82, 75, 88, 92].map((height, i) => (
                          <div
                            key={i}
                            className="flex-1 flex flex-col items-center gap-2"
                          >
                            <div className="w-full flex items-end justify-center h-full">
                              <div
                                className="w-full rounded-t-md bg-verno-accent/70 hover:bg-verno-accent transition-colors"
                                style={{ height: `${height}%` }}
                              />
                            </div>
                            <span className="text-xs text-muted-foreground">
                              {['Sep', 'Oct', 'Nov', 'Dec', 'Jan', 'Feb'][i]}
                            </span>
                          </div>
                        ))}
                      </div>
                    </div>
                  </div>

                  <p className="mt-3 text-xs text-muted-foreground">
                    Metrics showing club activity, participation and course
                    demand.
                  </p>
                </div>
              </div>
            </RevealSection>
          </section>
        )}

        <section id="contact" aria-labelledby="closing-cta-title">
          <RevealSection stagger={180}>
            <div className="rounded-2xl bg-verno-surface px-6 py-8 shadow flex flex-col md:flex-row md:items-center md:justify-between gap-4">
              <div>
                <h2
                  id="closing-cta-title"
                  className="text-xl font-semibold text-verno-darker"
                >
                  Start with one season and grow
                </h2>
                <p className="mt-2 text-sm text-muted-foreground">
                  Begin with a single team or venue, then expand as your staff
                  get comfortable.
                </p>
              </div>
            </div>
          </RevealSection>
        </section>
      </div>
    </main>
  )
}
