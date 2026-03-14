import { Link } from 'react-router-dom'
import RevealSection from '@verno/components/ui/RevealSection'

export default function Home() {
  return (
    <main className="h-full bg-verno-bg text-verno-darker overflow-y-auto md:overflow-hidden -m-4">
      <div className="mx-auto min-h-full max-w-6xl px-6 py-8 md:py-0 flex flex-col">
        <div className="flex-1 flex flex-col justify-center">
          <RevealSection>
            <h1 className="text-4xl md:text-6xl font-semibold leading-tight underline decoration-verno-accent underline-offset-4 decoration-3 mb-10">
              Club management, made calm.
            </h1>
          </RevealSection>

          <RevealSection stagger={80}>
            <section className="grid md:grid-cols-2 gap-8 md:gap-16 items-center">
              <div>
                <p className="text-base text-muted-foreground max-w-lg">
                  Verno helps sports clubs organize teams, courses and venues in
                  one clear system. No scattered tools. No chaos. Just
                  structure.
                </p>

                <div className="mt-8 flex items-center gap-6">
                  <Link to="/product" className="btn-primary">
                    Explore product
                  </Link>

                  <Link
                    to="/company"
                    className="link-underline-animated inline-flex items-center text-sm font-medium"
                  >
                    About Verno
                  </Link>
                </div>
              </div>

              <div className="rounded-3xl overflow-hidden shadow-lg">
                <img
                  src="/landing-page-opi.png"
                  alt="Happy club manager using Verno"
                  className="w-full h-64 object-cover object-top"
                />
              </div>
            </section>
          </RevealSection>
        </div>

        <RevealSection stagger={120}>
          <section className="border-t border-verno-accent/40 py-6 flex flex-col md:flex-row items-center justify-between gap-6 text-sm text-muted-foreground">
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
        </RevealSection>
      </div>
    </main>
  )
}
