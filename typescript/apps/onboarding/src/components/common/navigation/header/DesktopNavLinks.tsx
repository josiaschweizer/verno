import { Link } from 'react-router-dom'

export default function DesktopNavLinks() {
  return (
    <>
      <Link
        to="/company"
        className="link-underline-animated text-sm/6 font-semibold text-verno-dark hover:text-verno-dark-hover"
      >
        Company
      </Link>

      <Link
        to="/pricing"
        className="link-underline-animated text-sm/6 font-semibold text-verno-dark hover:text-verno-dark-hover"
      >
        Pricing
      </Link>
    </>
  )
}
