import { Link } from 'react-router-dom'

export default function Home() {
  return (
    <main className="h-full bg-verno-bg text-verno-darker overflow-y-auto md:overflow-hidden -m-4">
      <div className="mx-auto min-h-full max-w-6xl px-6 py-8 md:py-0 flex flex-col md:justify-center">
        <div className="space-y-8 md:space-y-12">
          <section className="grid md:grid-cols-2 gap-8 md:gap-16 items-center">
            <div>
              <h1 className="text-4xl md:text-5xl font-semibold leading-tight">
                Club management,
                <br />
                made calm.
              </h1>

              <p className="mt-6 text-base text-muted-foreground max-w-lg">
                Verno helps sports clubs organize teams, courses and venues in
                one clear system. No scattered tools. No chaos. Just structure.
              </p>

              <div className="mt-8 flex items-center gap-6">
                <Link to="/product" className="btn-primary">
                  Explore product
                </Link>

                <Link
                  to="/company"
                  className="inline-flex items-center text-sm font-medium hover:underline"
                >
                  About Verno
                </Link>
              </div>
            </div>

            <div className="rounded-3xl bg-verno-surface shadow-lg p-10">
              <div className="space-y-6">
                <div>
                  <div className="text-sm font-semibold">Organization</div>
                  <p className="text-sm text-muted-foreground mt-1">
                    Clear structure for clubs, teams and venues.
                  </p>
                </div>

                <div>
                  <div className="text-sm font-semibold">Scheduling</div>
                  <p className="text-sm text-muted-foreground mt-1">
                    Plan training, matches and courses without friction.
                  </p>
                </div>

                <div>
                  <div className="text-sm font-semibold">Membership</div>
                  <p className="text-sm text-muted-foreground mt-1">
                    Keep participants, guardians and roles aligned.
                  </p>
                </div>
              </div>
            </div>
          </section>

          <section className="border-t border-verno-surface pt-8 flex flex-col md:flex-row items-center justify-between gap-6 text-sm text-muted-foreground">
            <p>
              Switzerland-first infrastructure. Built for long-term club
              operations.
            </p>

            <div className="flex items-center gap-8">
              <span>Secure by default</span>
              <span>Multi-tenant ready</span>
              <span>Designed for real clubs</span>
            </div>
          </section>
        </div>
      </div>
    </main>
  )
}
