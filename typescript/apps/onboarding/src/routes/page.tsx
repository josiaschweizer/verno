import { Link } from 'react-router-dom'
import RevealSection from '@verno/components/ui/RevealSection'

export default function Home() {
  return (
    <div className="h-full bg-verno-bg text-verno-darker -m-4 flex items-center">
      <div className="mx-auto w-full max-w-6xl px-6">
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
                one clear system. No scattered tools. No chaos. Just structure.
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

              <div className="mt-6 flex flex-wrap gap-2">
                <span className="inline-flex items-center rounded-full border border-verno-accent/20 bg-verno-accent/10 px-3 py-1 text-xs font-medium text-verno-darker">
                  Secure by default
                </span>

                <span className="inline-flex items-center rounded-full border border-verno-accent/20 bg-verno-accent/10 px-3 py-1 text-xs font-medium text-verno-darker">
                  Switzerland-first infrastructure
                </span>

                <span className="inline-flex items-center rounded-full border border-verno-accent/20 bg-verno-accent/10 px-3 py-1 text-xs font-medium text-verno-darker">
                  Designed for real clubs
                </span>
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
    </div>
  )
}
