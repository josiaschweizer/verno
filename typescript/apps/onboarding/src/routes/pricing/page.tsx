import { useState } from 'react'
import { Button } from '@verno/components/ui/button'
import RegisterMultiStepDialog from '@/components/common/register/dialog/RegisterMultiStepDialog'
import RevealSection from '@verno/components/ui/RevealSection'
import GetInTouchDialog from '@/components/common/contact/GetInTouchDialog'

export default function VernoPricingPage() {
  const [registerOpen, setRegisterOpen] = useState(false)
  const [demoOpen, setDemoOpen] = useState(false)

  return (
    <main className="min-h-screen bg-verno-bg text-verno-darker overflow-x-hidden">
      <section className="relative overflow-hidden">
        <div className="mx-auto max-w-6xl px-4 sm:px-6 py-12 sm:py-16">
          <RevealSection stagger={0}>
            <div className="mx-auto max-w-3xl text-center">
              <h1 className="text-2xl sm:text-3xl md:text-4xl font-semibold text-verno-darker">
                Simple pricing for calm club management
              </h1>

              <p className="mt-3 sm:mt-4 text-sm text-muted-foreground">
                Start with one clear package and get everything you need to run
                your club in a modern and structured way.
              </p>
            </div>
          </RevealSection>
        </div>
      </section>

      <section className="mx-auto max-w-6xl px-4 sm:px-6 pb-12">
        <RevealSection stagger={60}>
          <div className="mx-auto">
            <div className="mx-auto max-w-3xl rounded-2xl bg-verno-surface p-4 sm:p-6 md:p-8 shadow">
              <div className="flex flex-col gap-4 sm:gap-6 border-b border-border pb-4 sm:pb-6 sm:flex-row sm:items-stretch sm:justify-between">
                <div className="flex-1 rounded-xl bg-verno-surface-light px-4 sm:px-5 py-4">
                  <div className="inline-flex items-center rounded-full bg-verno-accent/10 px-3 py-1 text-xs sm:text-sm text-verno-dark">
                    Basic Package
                  </div>
                  <h2 className="mt-3 sm:mt-4 text-xl sm:text-2xl font-semibold text-verno-darker">
                    Everything essential to get started
                  </h2>
                  <p className="mt-2 text-sm text-muted-foreground">
                    Ideal for clubs that want a clean and reliable system
                    without unnecessary complexity.
                  </p>
                </div>

                <div className="flex-1 rounded-xl bg-verno-surface-light px-4 sm:px-5 py-4">
                  <div className="text-xs text-muted-foreground">
                    Starting at
                  </div>
                  <div className="mt-1 flex items-end gap-2">
                    <span className="text-2xl sm:text-3xl font-semibold text-verno-darker">
                      CHF 25
                    </span>
                    <span className="text-xs text-muted-foreground">
                      / mo
                    </span>
                  </div>
                  <p className="mt-1 text-xs text-muted-foreground">per club</p>
                </div>
              </div>

              <div className="grid grid-cols-1 gap-6 sm:gap-8 pt-4 sm:pt-6 md:grid-cols-2">
                <div>
                  <h3 className="text-xs font-semibold uppercase text-muted-foreground">
                    Included
                  </h3>
                  <ul className="mt-3 sm:mt-4 space-y-2 sm:space-y-3 text-sm text-verno-dark">
                    {[
                      'Member management',
                      'Team and role overview',
                      'Central communication tools',
                      'Modern admin dashboard',
                      'Responsive design',
                      'Secure cloud access',
                    ].map((item) => (
                      <li key={item} className="flex items-start gap-3">
                        <span className="mt-1 h-2 w-2 rounded-full bg-verno-accent" />
                        <span>{item}</span>
                      </li>
                    ))}
                  </ul>
                </div>

                <div>
                  <h3 className="text-xs font-semibold uppercase text-muted-foreground">
                    Why this plan
                  </h3>
                  <div className="mt-4 space-y-3">
                    {[
                      {
                        title: 'One clear package',
                        text: 'No confusing pricing. Just one simple offer.',
                      },
                      {
                        title: 'Fast to understand',
                        text: 'Perfect while the product is still evolving.',
                      },
                      {
                        title: 'Clean first impression',
                        text: 'Looks modern and builds trust immediately.',
                      },
                    ].map((item) => (
                      <div
                        key={item.title}
                        className="rounded-xl bg-verno-surface-light p-4"
                      >
                        <div className="text-sm font-semibold text-verno-darker">
                          {item.title}
                        </div>
                        <p className="mt-1 text-xs text-muted-foreground">
                          {item.text}
                        </p>
                      </div>
                    ))}
                  </div>
                </div>
              </div>

              <div className="mt-6 sm:mt-8 flex flex-col gap-3 sm:flex-row">
                <Button
                  onClick={() => setRegisterOpen(true)}
                  variant="outline"
                  className="flex-1 rounded-xl border border-border bg-verno-accent px-5 py-3 text-sm font-normal text-white"
                >
                  Get started <span aria-hidden="true">&rarr;</span>
                </Button>

                <Button
                  variant="outline"
                  className="flex-1 rounded-xl border border-border bg-verno-bg px-5 py-3 text-sm font-normal text-verno-darker"
                  onClick={() => setDemoOpen(true)}
                >
                  Book a demo
                </Button>
              </div>
            </div>
          </div>
        </RevealSection>
      </section>

      <section className="mx-auto max-w-3xl px-4 sm:px-6 pb-16 sm:pb-20">
        <RevealSection stagger={120}>
          <div className="rounded-2xl bg-verno-surface p-4 sm:p-6 md:p-8 shadow">
            <h3 className="text-sm font-semibold text-verno-darker">FAQ</h3>

            <div className="mt-4 space-y-5 text-sm text-muted-foreground">
              <div>
                <div className="font-medium text-verno-darker">
                  Can I cancel anytime?
                </div>
                <p className="mt-1">Yes, you can cancel at any time.</p>
              </div>

              <div>
                <div className="font-medium text-verno-darker">
                  Is support included?
                </div>
                <p className="mt-1">Yes, basic support is included.</p>
              </div>

              <div>
                <div className="font-medium text-verno-darker">
                  Can pricing change later?
                </div>
                <p className="mt-1">
                  We reserve the right to introduce new plans and to modify
                  existing plans, including their pricing and included features,
                  at any time.
                </p>
              </div>
            </div>
          </div>
        </RevealSection>
      </section>

      <RegisterMultiStepDialog
        open={registerOpen}
        onClose={() => setRegisterOpen(false)}
      />

      <GetInTouchDialog open={demoOpen} onClose={() => setDemoOpen(false)} />
    </main>
  )
}
