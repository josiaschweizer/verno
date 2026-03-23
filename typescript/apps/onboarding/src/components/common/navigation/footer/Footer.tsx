import { Link } from 'react-router-dom'

export default function Footer() {
  return (
    <footer className="border-t border-verno-accent/40 text-sm text-muted-foreground">
      <div className="mx-auto max-w-6xl px-6 py-6 flex flex-col md:flex-row items-center justify-between gap-4">
        <p>© {new Date().getFullYear()} Verno</p>

        <div className="flex items-center gap-6">
          <Link to="/legal/imprint" className="link-underline-animated">
            Imprint
          </Link>

          <Link to="/legal/privacy" className="link-underline-animated">
            Privacy
          </Link>

          <Link to="/legal/terms" className="link-underline-animated">
            Terms
          </Link>
        </div>
      </div>
    </footer>
  )
}
