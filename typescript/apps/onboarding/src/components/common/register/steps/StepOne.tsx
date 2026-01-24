export function StepOne() {
  return (
    <div>
      <h3 className="text-sm font-medium">Schritt 1 — Grunddaten</h3>
      <p className="mt-2 text-sm text-verno-darker/80">
        Platzhalter: Name, E-Mail, Organisation.
      </p>

      <div className="mt-4 space-y-2">
        <input className="input w-full" placeholder="Name" />
        <input className="input w-full" placeholder="E-Mail" />
      </div>
    </div>
  )
}

export default StepOne
