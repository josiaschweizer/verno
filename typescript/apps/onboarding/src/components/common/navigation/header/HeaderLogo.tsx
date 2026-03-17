import { Link } from 'react-router-dom'

export default function HeaderLogo() {
  return (
    <Link to="/" className="-m-1.5 p-1.5 flex items-center gap-2">
      <span className="sr-only">Verno</span>

      <img
        src="/verno-app.png"
        alt="Verno logo"
        className="h-26 w-auto"
        loading="lazy"
      />

      <span className="text-verno-dark font-semibold text-2xl tracking-wide">
        Verno
      </span>
    </Link>
  )
}
