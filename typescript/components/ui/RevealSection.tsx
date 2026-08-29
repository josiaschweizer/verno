import type { ReactNode } from 'react'

interface RevealSectionProps {
  children: ReactNode
  className?: string
  stagger?: number
}

export default function RevealSection({ children, className, stagger = 0 }: RevealSectionProps) {
  return (
    <div
      className={['reveal-section', className].filter(Boolean).join(' ')}
      style={{
        animationDelay: stagger ? `${stagger}ms` : undefined,
      }}
    >
      {children}
    </div>
  )
}
