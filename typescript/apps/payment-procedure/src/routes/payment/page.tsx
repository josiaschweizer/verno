import { CreditCard, ShieldCheck, TriangleAlert } from 'lucide-react'
import { useMemo, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { toast } from 'sonner'
import RevealSection from '@verno/components/ui/RevealSection'
import { ApiError } from '@verno/lib/apiClient'
import { billingSessionApi } from '@/lib/api/billingSession'

type BillingEntryContext = {
  tenantId: number
  userId: number
  purpose: string
  expiresAt: string | null
}

function formatDate(date: string | null) {
  if (!date) return '-'

  try {
    return new Intl.DateTimeFormat('de-CH', {
      dateStyle: 'medium',
      timeStyle: 'short',
    }).format(new Date(date))
  } catch {
    return date
  }
}

function getPurposeLabel(purpose: string) {
  switch (purpose) {
    case 'UPDATE_PAYMENT_METHOD':
      return 'Zahlungsmethode aktualisieren'
    case 'START_CHECKOUT':
      return 'Subscription starten'
    case 'OPEN_BILLING_PORTAL':
      return 'Billing-Portal öffnen'
    default:
      return purpose
  }
}

export default function PaymentPage() {
  const navigate = useNavigate()
  const token = sessionStorage.getItem('billingEntryToken')
  const rawContext = sessionStorage.getItem('billingEntryContext')
  const [isStartingPayment, setIsStartingPayment] = useState(false)

  const context = useMemo(() => {
    if (!rawContext) {
      return null
    }

    try {
      return JSON.parse(rawContext) as BillingEntryContext
    } catch {
      return null
    }
  }, [rawContext])

  async function startPayment() {
    if (!token) {
      console.error('Billing entry token is missing')
      return
    }

    try {
      setIsStartingPayment(true)

      const result = await billingSessionApi.startSession(token)
      window.location.href = result.url
    } catch (error) {
      console.error('Failed to start billing session', error)

      const description =
        error instanceof ApiError && error.message
          ? error.message
          : 'Es ist ein Fehler aufgetreten. Bitte versuchen Sie es später erneut.'

      toast.error('Zahlung konnte nicht gestartet werden', { description })
      setIsStartingPayment(false)
    }
  }

  if (!token) {
    return (
      <main className="h-full bg-verno-bg text-verno-darker overflow-y-auto md:overflow-hidden -m-4">
        <div className="mx-auto min-h-full max-w-6xl px-6 py-8 md:py-0 flex flex-col">
          <div className="flex-1 flex flex-col justify-center">
            <RevealSection>
              <div className="max-w-3xl">
                <div className="inline-flex items-center gap-2 rounded-full bg-verno-surface px-3 py-1 text-sm text-muted-foreground shadow-sm">
                  <TriangleAlert className="h-4 w-4" />
                  Billing access
                </div>

                <h1 className="mt-6 text-4xl md:text-5xl font-semibold leading-tight text-verno-darker">
                  Kein Zahlungszugang vorhanden.
                </h1>

                <p className="mt-4 text-base text-muted-foreground max-w-xl">
                  Der Zahlungszugang konnte nicht geladen werden. Bitte öffnen
                  Sie den Link erneut direkt aus Verno.
                </p>

                <div className="mt-8 flex items-center gap-6">
                  <Link to="/" className="btn-primary">
                    Zur Startseite
                  </Link>
                </div>
              </div>
            </RevealSection>
          </div>
        </div>
      </main>
    )
  }

  return (
    <main className="min-h-screen bg-verno-bg text-verno-darker overflow-x-hidden">
      <div className="mx-auto max-w-6xl px-4 py-16 space-y-20">
        <section aria-labelledby="billing-title">
          <RevealSection>
            <div className="grid gap-10 md:grid-cols-[minmax(0,1.35fr)_minmax(0,1fr)] items-start">
              <div>
                <div className="inline-flex items-center gap-2 rounded-full bg-verno-surface px-3 py-1 text-sm text-muted-foreground shadow-sm">
                  <CreditCard className="h-4 w-4" />
                  Verno Billing
                </div>

                <h1
                  id="billing-title"
                  className="mt-6 text-3xl md:text-4xl font-semibold text-verno-darker"
                >
                  Zahlung erforderlich
                </h1>

                <p className="mt-4 text-sm text-muted-foreground max-w-xl">
                  Für diesen Mandanten ist aktuell keine gültige Subscription
                  vorhanden oder die hinterlegte Zahlungsmethode muss
                  aktualisiert werden.
                </p>

                <p className="mt-2 text-sm text-muted-foreground max-w-xl">
                  Um Verno weiterhin nutzen zu können, führen Sie bitte den
                  nächsten Schritt im Billing-Prozess aus.
                </p>

                <div className="mt-8 rounded-2xl bg-verno-surface shadow p-5">
                  <div className="flex items-start gap-3">
                    <ShieldCheck className="h-5 w-5 shrink-0 mt-0.5" />
                    <div>
                      <p className="font-medium text-verno-darker mb-1">
                        Sicherer Zugriff
                      </p>
                      <p className="text-sm text-muted-foreground max-w-xl">
                        Dieser Zugriff wurde über einen zeitlich begrenzten Link
                        freigegeben und ist nur für diesen Billing-Vorgang
                        gültig.
                      </p>
                    </div>
                  </div>
                </div>

                <div className="mt-8 flex items-center gap-6 flex-wrap">
                  <button
                    type="button"
                    className="btn-primary"
                    disabled={isStartingPayment}
                    onClick={() => {
                      void startPayment()
                    }}
                  >
                    {isStartingPayment ? 'Wird gestartet…' : 'Zahlung starten'}
                  </button>

                  <button
                    type="button"
                    className="link-underline-animated inline-flex items-center text-sm font-medium"
                    onClick={() => navigate('/')}
                  >
                    Abbrechen
                  </button>
                </div>
              </div>

              {context && (
                <div className="rounded-2xl bg-verno-surface shadow p-5">
                  <h2 className="text-lg font-semibold text-verno-darker">
                    Billing-Kontext
                  </h2>

                  <div className="mt-5 space-y-4">
                    <div className="border-b border-verno-bg/80 pb-3">
                      <p className="text-xs uppercase text-muted-foreground">
                        Mandant
                      </p>
                      <p className="mt-1 text-sm font-medium text-verno-darker">
                        {context.tenantId}
                      </p>
                    </div>

                    <div className="border-b border-verno-bg/80 pb-3">
                      <p className="text-xs uppercase text-muted-foreground">
                        Benutzer
                      </p>
                      <p className="mt-1 text-sm font-medium text-verno-darker">
                        {context.userId}
                      </p>
                    </div>

                    <div className="border-b border-verno-bg/80 pb-3">
                      <p className="text-xs uppercase text-muted-foreground">
                        Aktion
                      </p>
                      <p className="mt-1 text-sm font-medium text-verno-darker">
                        {getPurposeLabel(context.purpose)}
                      </p>
                    </div>

                    <div>
                      <p className="text-xs uppercase text-muted-foreground">
                        Gültig bis
                      </p>
                      <p className="mt-1 text-sm font-medium text-verno-darker">
                        {formatDate(context.expiresAt)}
                      </p>
                    </div>
                  </div>
                </div>
              )}
            </div>
          </RevealSection>
        </section>

        <section aria-labelledby="billing-help-title">
          <RevealSection stagger={180}>
            <div className="rounded-2xl bg-verno-surface px-6 py-8 shadow flex flex-col md:flex-row md:items-center md:justify-between gap-4">
              <div>
                <h2
                  id="billing-help-title"
                  className="text-xl font-semibold text-verno-darker"
                >
                  Billing in einem klaren Schritt
                </h2>
                <p className="mt-2 text-sm text-muted-foreground max-w-2xl">
                  Sie werden im nächsten Schritt zur sicheren Zahlungsabwicklung
                  weitergeleitet. Danach wird der Zugriff auf Verno wieder
                  automatisch freigeschaltet.
                </p>
              </div>

              <button
                type="button"
                className="btn-primary"
                disabled={isStartingPayment}
                onClick={() => {
                  void startPayment()
                }}
              >
                {isStartingPayment ? 'Wird gestartet…' : 'Jetzt fortfahren'}
              </button>
            </div>
          </RevealSection>
        </section>
      </div>
    </main>
  )
}
