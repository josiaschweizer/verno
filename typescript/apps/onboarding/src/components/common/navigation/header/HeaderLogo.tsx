import { Link } from 'react-router-dom'

export default function HeaderLogo() {
  return (
    <Link to="/" className="-m-1.5 p-1.5 flex items-center gap-2">
      <span className="sr-only">Verno</span>

      <img
        alt="Verno logo"
        src="https://tailwindcss.com/plus-assets/img/logos/mark.svg?color=indigo&shade=500"
        className="w-10 h-10 rounded-md"
      />

      <span className="text-verno-dark text-base font-semibold tracking-wide">
        Verno
      </span>
    </Link>
  )
}
