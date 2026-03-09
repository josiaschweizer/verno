import { useEffect, useRef, useState, type ReactNode } from 'react'

interface RevealSectionProps {
  children: ReactNode
  className?: string
  stagger?: number
}

export default function RevealSection({
  children,
  className,
  stagger = 0,
}: RevealSectionProps) {
  const ref = useRef<HTMLDivElement>(null)
  const [visible, setVisible] = useState(false)

  useEffect(() => {
    const el = ref.current
    if (!el) return

    const observer = new IntersectionObserver(
      (entries) => {
        const [entry] = entries
        if (entry?.isIntersecting) {
          setVisible(true)
        }
      },
      {
        threshold: 0.1,
        rootMargin: '0px 0px -40px 0px',
      },
    )

    observer.observe(el)
    return () => observer.disconnect()
  }, [])

  return (
    <div
      ref={ref}
      className={[
        'transition-all duration-500 ease-out',
        visible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-6',
        className,
      ]
        .filter(Boolean)
        .join(' ')}
      style={{
        transitionDelay: visible ? `${stagger}ms` : undefined,
      }}
    >
      {children}
    </div>
  )
}
