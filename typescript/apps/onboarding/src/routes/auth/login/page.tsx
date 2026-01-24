import { Link } from "react-router-dom";

export default function LoginPage() {
  return (
    <main className="min-h-full flex items-center justify-center px-6">
      <section className="w-full max-w-lg rounded-2xl border border-slate-800 bg-slate-900/60 backdrop-blur p-8 shadow-2xl">
        <header className="mb-6">
          <h1 className="text-2xl font-semibold tracking-tight">Anmelden</h1>
          <p className="mt-2 text-sm text-slate-400 leading-relaxed">
            Placeholder – kommt als nächstes.
          </p>
        </header>

        <div className="text-sm text-slate-300">
          <Link to="/" className="text-slate-300 hover:underline">
            Zurück
          </Link>
        </div>
      </section>
    </main>
  );
}