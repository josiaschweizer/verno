import { Link } from 'react-router-dom'

export default function CreateTenantPage() {
  return (
    <main className="min-h-screen flex items-center justify-center px-6">
      <section className="w-full max-w-lg rounded-2xl border border-slate-800 bg-slate-900/60 backdrop-blur p-8 shadow-2xl">
        <header className="mb-8">
          <div className="mb-3 text-sm font-semibold tracking-wide text-slate-400">
            VERNO
          </div>
          <h1 className="text-2xl font-semibold tracking-tight">
            Mandant erstellen
          </h1>
          <p className="mt-2 text-sm text-slate-400 leading-relaxed">
            Registriere dich und konfiguriere deinen Mandanten. Die
            Provisionierung erfolgt automatisch.
          </p>
        </header>

        <form className="space-y-5">
          <Field label="E-Mail">
            <input
              type="email"
              placeholder="name@verein.ch"
              className="input"
            />
          </Field>

          <Field label="Organisation / Verein">
            <input type="text" placeholder="Verein Muster" className="input" />
          </Field>

          <Field label="Mandanten-Key">
            <input
              type="text"
              placeholder="z. B. musterverein"
              className="input"
            />
            <p className="mt-1 text-xs text-slate-500">
              Wird Teil der URL:{' '}
              <span className="text-slate-400">musterverein.verno.swiss</span>
            </p>
          </Field>

          <div className="pt-2">
            <button
              type="button"
              className="w-full rounded-xl bg-indigo-600 hover:bg-indigo-500 active:bg-indigo-700 transition px-4 py-2.5 font-medium"
            >
              Weiter
            </button>
          </div>
        </form>

        <footer className="mt-6 text-center text-xs text-slate-500">
          Bereits registriert?{' '}
          <Link to="/login" className="text-slate-300 hover:underline">
            Anmelden
          </Link>
        </footer>
      </section>
    </main>
  )
}

type FieldProps = {
  label: string
  children: React.ReactNode
}

function Field({ label, children }: FieldProps) {
  return (
    <label className="block">
      <span className="mb-1 block text-sm font-medium text-slate-300">
        {label}
      </span>
      {children}
    </label>
  )
}
