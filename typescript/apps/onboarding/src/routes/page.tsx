export default function Home() {
  return (
    <div className="min-h-screen flex items-center justify-center p-6">
      <div className="card max-w-xl w-full">
        <h1 className="text-2xl font-semibold text-df-dark mb-4">
          Verno – Design Tokens aktiv
        </h1>

        <p className="mb-6">
          Hintergrund ist <code>bg-verno-bg</code>, Karte ist{' '}
          <code>bg-verno-surface</code>, Text ist <code>text-verno-darker</code>
          /<code>text-verno-dark</code>.
        </p>

        <button className="btn-primary">Aktion ausführen</button>
      </div>
    </div>
  )
}
