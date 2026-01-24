export function StepTwo() {
  return (
    <div>
      <h3 className="text-sm font-medium">Schritt 2 — Details</h3>
      <p className="mt-2 text-sm text-verno-darker/80">
        Platzhalter: Organisationstyp, Anzahl Teilnehmer.
      </p>

      <div className="mt-4 space-y-2">
        <input className="input w-full" placeholder="Organisation" />
        <input className="input w-full" placeholder="Teilnehmer (Anzahl)" />
      </div>
    </div>
  )
}

export default StepTwo
