interface Props {
  title: string
  children: React.ReactNode
}

export default function LegalSection({ title, children }: Props) {
  return (
    <section className="space-y-2">
      <h2 className="font-medium text-foreground">{title}</h2>
      <div>{children}</div>
    </section>
  )
}
