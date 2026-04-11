type LegalPageProps = {
  title: string
  children: React.ReactNode
}

export default function LegalPage({ title, children }: LegalPageProps) {
  return (
    <div className="mx-auto w-full max-w-3xl px-6 py-12">
      <div className="space-y-8">
        <h1 className="text-3xl font-semibold text-foreground">{title}</h1>

        <div className="space-y-6 text-sm leading-6 text-muted-foreground">
          {children}
        </div>
      </div>
    </div>
  )
}
