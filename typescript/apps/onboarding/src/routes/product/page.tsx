import { useEffect, useMemo, useState } from 'react'
import { useLocation } from 'react-router'
import { useTranslation } from 'react-i18next'
import { HoverSplitImage } from '@/components/ui/custom/HoverSplitImage'
import RevealSection from '@verno/components/ui/RevealSection'
import { tenantsApi } from '@/lib/api/tenantsApi'
import { createCanonicalLinks, createMeta } from '@/lib/seo'

export function meta() {
  return createMeta(
    'Vereinssoftware für Kurse, Teams und Mitglieder | Verno',
    'Entdecke Verno für Kursplanung, Teilnehmerverwaltung, Kommunikation und die tägliche Organisation deines Sportvereins.',
  )
}

export function links() {
  return createCanonicalLinks('/product')
}

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

function formatCount(value: number | null): string {
  if (value == null) {
    return '–'
  }

  const number = value < 10 ? 35 : value
  return Intl.NumberFormat().format(number)
}

function extractNumber(value: unknown): number | null {
  if (typeof value === 'number') {
    return value
  } else if (typeof value === 'string') {
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
  const { t } = useTranslation('product')
  const location = useLocation()

  const organizationConfig = useMemo<
    Record<OrganizationView, OrganizationItem>
  >(
    () => ({
      courseSchedules: {
        title: t('organizationConfig.courseSchedules.title'),
        caption: t('organizationConfig.courseSchedules.caption'),
        alt: t('organizationConfig.courseSchedules.alt'),
        lightSrc: '/product/course-schedules-light.png',
        darkSrc: '/product/course-schedules.png',
      },
      courses: {
        title: t('organizationConfig.courses.title'),
        caption: t('organizationConfig.courses.caption'),
        alt: t('organizationConfig.courses.alt'),
        lightSrc: '/product/courses-light.png',
        darkSrc: '/product/courses.png',
      },
      instructors: {
        title: t('organizationConfig.instructors.title'),
        caption: t('organizationConfig.instructors.caption'),
        alt: t('organizationConfig.instructors.alt'),
        lightSrc: '/product/instructors-light.png',
        darkSrc: '/product/instructors.png',
      },
      participants: {
        title: t('organizationConfig.participants.title'),
        caption: t('organizationConfig.participants.caption'),
        alt: t('organizationConfig.participants.alt'),
        lightSrc: '/product/participants-light.png',
        darkSrc: '/product/participants.png',
      },
    }),
    [t],
  )

  const [peopleView, setPeopleView] = useState<PeopleView>('participants')
  const [organizationView, setOrganizationView] =
    useState<OrganizationView>('courseSchedules')

  const [tenantsCount, setTenantsCount] = useState<number | null>(null)
  const [memberCount, setMemberCount] = useState<number | null>(null)
  const [courseCount, setCourseCount] = useState<number | null>(null)

  const organizationKeyPoints = t('organization.keyPoints', {
    returnObjects: true,
  }) as string[]

  const schedulingKeyPoints = t('scheduling.keyPoints', {
    returnObjects: true,
  }) as string[]

  const participantsKeyPoints = t('participants.keyPoints', {
    returnObjects: true,
  }) as string[]

  const mailingKeyPoints = t('mailing.keyPoints', {
    returnObjects: true,
  }) as string[]

  const reportingKeyPoints = t('reporting.keyPoints', {
    returnObjects: true,
  }) as string[]

  const reportingMonths = t('reporting.months', {
    returnObjects: true,
  }) as string[]

  useEffect(() => {
    let cancelled = false

    ;(async () => {
      try {
        const [t, m, c] = await Promise.all([
          tenantsApi.getCountOfTenants(),
          tenantsApi.getTotalMemberCount(),
          tenantsApi.getTotalCourseCount(),
        ])

        if (cancelled) {
          return
        }

        setTenantsCount(extractNumber(t))
        setMemberCount(extractNumber(m))
        setCourseCount(extractNumber(c))
      } catch {
        if (cancelled) {
          return
        }

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
    if (!location.hash) {
      return
    }

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
      const timeout = window.setTimeout(() => tryScroll(attempt + 1), delay)
      return () => window.clearTimeout(timeout)
    }

    const timeout = window.setTimeout(() => tryScroll(), 150)

    return () => {
      cancelled = true
      window.clearTimeout(timeout)
    }
  }, [location, tenantsCount])

  const activeOrganization = useMemo(
    () => organizationConfig[organizationView],
    [organizationConfig, organizationView],
  )

  const showReporting = tenantsCount != null && tenantsCount > 1

  return (
    <main className="min-h-screen bg-verno-bg text-verno-darker overflow-x-hidden">
      <div className="mx-auto max-w-6xl px-4 sm:px-6 py-12 sm:py-16 space-y-16 sm:space-y-20">
        <section aria-labelledby="verno-hero-title">
          <RevealSection stagger={0}>
            <div className="grid grid-cols-1 gap-8 md:grid-cols-[minmax(0,0.9fr)_minmax(0,1.1fr)] md:gap-12 items-center">
              <div>
                <h1
                  id="verno-hero-title"
                  className="text-3xl sm:text-4xl font-semibold tracking-tight text-verno-darker"
                >
                  {t('hero.title')}
                </h1>
                <p className="mt-5 text-base leading-7 text-muted-foreground">
                  {t('hero.text1')}
                </p>
                <p className="mt-2 text-base leading-7 text-muted-foreground">
                  {t('hero.text2')}
                </p>
              </div>

              <div className="rounded-2xl bg-verno-surface shadow p-4">
                <div className="rounded-xl bg-verno-surface-light border border-transparent overflow-hidden aspect-16/10">
                  <HoverSplitImage
                    lightSrc="/dashboard/dashboard-light.png"
                    darkSrc="/dashboard/dashboard.png"
                    alt={t('hero.imageAlt')}
                    className="h-full"
                    initialSplit={0}
                  />
                </div>
                <p className="mt-3 text-sm leading-5 text-muted-foreground">
                  {t('hero.caption')}
                </p>
              </div>
            </div>
          </RevealSection>
        </section>

        <section id="organization" aria-labelledby="organization-title">
          <RevealSection stagger={30}>
            <div className="grid gap-10 md:grid-cols-[minmax(0,0.9fr)_minmax(0,1.1fr)] md:gap-12 items-start">
              <div>
                <h2
                  id="organization-title"
                  className="text-2xl font-semibold tracking-tight text-verno-darker"
                >
                  {t('organization.title')}
                </h2>
                <p className="mt-3 text-base font-medium text-verno-dark">
                  {t('organization.subtitle')}
                </p>
                <p className="mt-3 text-base leading-7 text-muted-foreground">
                  {t('organization.text1')}
                </p>
                <p className="mt-1 text-base leading-7 text-muted-foreground">
                  {t('organization.text2')}
                </p>

                <div className="mt-4">
                  <p className="text-sm font-medium text-verno-dark">
                    {t('common.keyPoints')}
                  </p>
                  <ul className="mt-2 space-y-2 text-sm leading-6 text-muted-foreground">
                    {organizationKeyPoints.map((item) => (
                      <li key={item}>• {item}</li>
                    ))}
                  </ul>
                </div>
              </div>

              <div className="rounded-2xl bg-verno-surface shadow p-4">
                <div className="flex items-center justify-between gap-3 mb-3">
                  <p className="text-sm font-medium text-verno-darker shrink-0">
                    {activeOrganization.title}
                  </p>

                  <select
                    value={organizationView}
                    onChange={(e) =>
                      setOrganizationView(e.target.value as OrganizationView)
                    }
                    className="md:hidden text-xs rounded-lg bg-verno-bg border border-border px-3 py-1.5 text-verno-darker focus:outline-none focus:ring-2 focus:ring-ring"
                  >
                    {(
                      [
                        'courseSchedules',
                        'courses',
                        'instructors',
                        'participants',
                      ] as OrganizationView[]
                    ).map((view) => (
                      <option key={view} value={view}>
                        {organizationConfig[view].title}
                      </option>
                    ))}
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
                    initialSplit={0}
                    objectFit="contain"
                  />
                </div>

                <p className="mt-3 text-sm leading-5 text-muted-foreground">
                  {activeOrganization.caption} {t('common.hoverCompare')}
                </p>
              </div>
            </div>
          </RevealSection>
        </section>

        <section id="scheduling" aria-labelledby="scheduling-title">
          <RevealSection stagger={60}>
            <div className="grid gap-10 md:grid-cols-[minmax(0,0.9fr)_minmax(0,1.1fr)] md:gap-12 items-start">
              <div>
                <h2
                  id="scheduling-title"
                  className="text-2xl font-semibold tracking-tight text-verno-darker"
                >
                  {t('scheduling.title')}
                </h2>
                <p className="mt-3 text-base font-medium text-verno-dark">
                  {t('scheduling.subtitle')}
                </p>
                <p className="mt-3 text-base leading-7 text-muted-foreground">
                  {t('scheduling.text1')}
                </p>
                <p className="mt-1 text-base leading-7 text-muted-foreground">
                  {t('scheduling.text2')}
                </p>

                <div className="mt-4">
                  <p className="text-sm font-medium text-verno-dark">
                    {t('common.keyPoints')}
                  </p>
                  <ul className="mt-2 space-y-2 text-sm leading-6 text-muted-foreground">
                    {schedulingKeyPoints.map((item) => (
                      <li key={item}>• {item}</li>
                    ))}
                  </ul>
                </div>
              </div>

              <div className="h-64 rounded-2xl bg-verno-surface shadow p-4 flex flex-col">
                <div className="flex-1 rounded-xl bg-verno-surface-light border border-transparent overflow-hidden">
                  <HoverSplitImage
                    lightSrc="/product/calendar-view-light.png"
                    darkSrc="/product/calendar-view.png"
                    alt={t('scheduling.imageAlt')}
                    className="h-full"
                    initialSplit={0}
                  />
                </div>
                <p className="mt-3 text-sm leading-5 text-muted-foreground">
                  {t('scheduling.caption')}
                </p>
              </div>
            </div>
          </RevealSection>
        </section>

        <section id="participants" aria-labelledby="participants-title">
          <RevealSection stagger={90}>
            <div className="grid gap-10 md:grid-cols-[minmax(0,0.9fr)_minmax(0,1.1fr)] md:gap-12 items-start">
              <div>
                <h2
                  id="participants-title"
                  className="text-2xl font-semibold tracking-tight text-verno-darker"
                >
                  {t('participants.title')}
                </h2>
                <p className="mt-3 text-base font-medium text-verno-dark">
                  {t('participants.subtitle')}
                </p>
                <p className="mt-3 text-base leading-7 text-muted-foreground">
                  {t('participants.text1')}
                </p>
                <p className="mt-1 text-base leading-7 text-muted-foreground">
                  {t('participants.text2')}
                </p>

                <div className="mt-4">
                  <p className="text-sm font-medium text-verno-dark">
                    {t('common.keyPoints')}
                  </p>
                  <ul className="mt-2 space-y-2 text-sm leading-6 text-muted-foreground">
                    {participantsKeyPoints.map((item) => (
                      <li key={item}>• {item}</li>
                    ))}
                  </ul>
                </div>
              </div>

              <div className="rounded-2xl bg-verno-surface shadow p-4">
                <div className="flex items-center justify-between gap-3 mb-3">
                  <p className="text-sm font-medium text-verno-darker shrink-0">
                    {peopleView === 'participants'
                      ? t('participants.participantsOverview')
                      : t('participants.rolesUsers')}
                  </p>

                  <select
                    value={peopleView}
                    onChange={(e) =>
                      setPeopleView(e.target.value as PeopleView)
                    }
                    className="md:hidden text-xs rounded-lg bg-verno-bg border border-border px-3 py-1.5 text-verno-darker focus:outline-none focus:ring-2 focus:ring-ring"
                  >
                    <option value="participants">
                      {t('participants.participants')}
                    </option>
                    <option value="users">
                      {t('participants.rolesAndUsers')}
                    </option>
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
                      {t('participants.participants')}
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
                      {t('participants.rolesAndUsers')}
                    </button>
                  </div>
                </div>

                <div className="rounded-xl bg-verno-surface-light border border-transparent overflow-hidden aspect-16/10">
                  {peopleView === 'participants' ? (
                    <HoverSplitImage
                      lightSrc="/product/participants-list-light.png"
                      darkSrc="/product/participants-list.png"
                      alt={t('participants.participantsAlt')}
                      className="h-full"
                      initialSplit={0}
                    />
                  ) : (
                    <HoverSplitImage
                      lightSrc="/product/user-administration-light.png"
                      darkSrc="/product/user-administration.png"
                      alt={t('participants.usersAlt')}
                      className="h-full"
                      initialSplit={0}
                    />
                  )}
                </div>

                <p className="mt-3 text-sm leading-5 text-muted-foreground">
                  {peopleView === 'participants'
                    ? t('participants.participantsCaption')
                    : t('participants.usersCaption')}{' '}
                  {t('common.hoverCompare')}
                </p>
              </div>
            </div>
          </RevealSection>
        </section>

        <section id="mailing" aria-labelledby="mailing-title">
          <RevealSection stagger={120}>
            <div className="grid gap-10 md:grid-cols-[minmax(0,0.9fr)_minmax(0,1.1fr)] md:gap-12 items-start">
              <div>
                <h2
                  id="mailing-title"
                  className="text-2xl font-semibold tracking-tight text-verno-darker"
                >
                  {t('mailing.title')}
                </h2>
                <p className="mt-3 text-base font-medium text-verno-dark">
                  {t('mailing.subtitle')}
                </p>
                <p className="mt-3 text-base leading-7 text-muted-foreground">
                  {t('mailing.text1')}
                </p>
                <p className="mt-1 text-base leading-7 text-muted-foreground">
                  {t('mailing.text2')}
                </p>

                <div className="mt-4">
                  <p className="text-sm font-medium text-verno-dark">
                    {t('common.keyPoints')}
                  </p>
                  <ul className="mt-2 space-y-2 text-sm leading-6 text-muted-foreground">
                    {mailingKeyPoints.map((item) => (
                      <li key={item}>• {item}</li>
                    ))}
                  </ul>
                </div>
              </div>

              <div className="rounded-2xl bg-verno-surface shadow p-4">
                <div className="rounded-xl bg-verno-surface-light border border-transparent overflow-hidden aspect-16/10">
                  <HoverSplitImage
                    lightSrc="/product/mail-overview-light.png"
                    darkSrc="/product/mail-overview.png"
                    alt={t('mailing.imageAlt')}
                    className="w-full h-full"
                    initialSplit={0}
                    objectFit="contain"
                  />
                </div>
                <p className="mt-3 text-sm leading-5 text-muted-foreground">
                  {t('mailing.caption')}
                </p>
              </div>
            </div>
          </RevealSection>
        </section>

        {showReporting && (
          <section id="reporting" aria-labelledby="reporting-title">
            <RevealSection stagger={150}>
              <div className="grid gap-10 md:grid-cols-[minmax(0,0.9fr)_minmax(0,1.1fr)] md:gap-12 items-start">
                <div>
                  <h2
                    id="reporting-title"
                    className="text-2xl font-semibold tracking-tight text-verno-darker"
                  >
                    {t('reporting.title')}
                  </h2>
                  <p className="mt-3 text-base font-medium text-verno-dark">
                    {t('reporting.subtitle')}
                  </p>
                  <p className="mt-3 text-base leading-7 text-muted-foreground">
                    {t('reporting.text1')}
                  </p>
                  <p className="mt-1 text-base leading-7 text-muted-foreground">
                    {t('reporting.text2')}
                  </p>

                  <div className="mt-4">
                    <p className="text-sm font-medium text-verno-dark">
                      {t('common.keyPoints')}
                    </p>
                    <ul className="mt-2 space-y-2 text-sm leading-6 text-muted-foreground">
                      {reportingKeyPoints.map((item) => (
                        <li key={item}>• {item}</li>
                      ))}
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
                        <div className="mt-1 text-sm text-muted-foreground">
                          {t('reporting.stats.activeClubs')}
                        </div>
                      </div>

                      <div className="rounded-xl bg-verno-surface-light p-4 border border-verno-accent/10">
                        <div className="text-2xl font-semibold text-verno-darker">
                          {formatCount(memberCount)}
                        </div>
                        <div className="mt-1 text-sm text-muted-foreground">
                          {t('reporting.stats.totalParticipants')}
                        </div>
                      </div>

                      <div className="rounded-xl bg-verno-surface-light p-4 border border-verno-accent/10">
                        <div className="text-2xl font-semibold text-verno-darker">
                          {formatCount(courseCount)}
                        </div>
                        <div className="mt-1 text-sm text-muted-foreground">
                          {t('reporting.stats.runningCourses')}
                        </div>
                      </div>
                    </div>

                    <div className="rounded-xl bg-verno-surface-light p-4 border border-transparent">
                      <div className="flex items-center justify-between mb-3">
                        <p className="text-sm font-medium text-verno-darker">
                          {t('reporting.attendanceOverview')}
                        </p>
                        <span className="text-sm text-muted-foreground">
                          {t('reporting.lastSixMonths')}
                        </span>
                      </div>

                      <div className="flex items-end justify-between gap-2 h-32">
                        {[65, 78, 82, 75, 88, 92].map((height, i) => (
                          <div
                            key={reportingMonths[i]}
                            className="flex-1 flex flex-col items-center gap-2"
                          >
                            <div className="w-full flex items-end justify-center h-full">
                              <div
                                className="w-full rounded-t-md bg-verno-accent/70 hover:bg-verno-accent transition-colors"
                                style={{ height: `${height}%` }}
                              />
                            </div>
                            <span className="text-sm text-muted-foreground">
                              {reportingMonths[i]}
                            </span>
                          </div>
                        ))}
                      </div>
                    </div>
                  </div>

                  <p className="mt-3 text-sm leading-5 text-muted-foreground">
                    {t('reporting.caption')}
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
                  className="text-2xl font-semibold tracking-tight text-verno-darker"
                >
                  {t('cta.title')}
                </h2>
                <p className="mt-3 text-base leading-7 text-muted-foreground">
                  {t('cta.text')}
                </p>
              </div>
            </div>
          </RevealSection>
        </section>
      </div>
    </main>
  )
}
