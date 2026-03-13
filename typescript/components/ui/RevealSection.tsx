import { type ReactNode, useEffect, useRef, useState } from 'react'

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

    let rafId1 = 0
    let rafId2 = 0
    const observer = new IntersectionObserver(
      (entries) => {
        const [entry] = entries
        if (entry?.isIntersecting) {
          rafId1 = requestAnimationFrame(() => {
            rafId2 = requestAnimationFrame(() => {
              setVisible(true)
            })
          })
        }
      },
      {
        threshold: 0.1,
        rootMargin: '0px 0px -40px 0px',
      },
    )

    observer.observe(el)
    return () => {
      observer.disconnect()
      cancelAnimationFrame(rafId1)
      cancelAnimationFrame(rafId2)
    }
  }, [])

  return (
    <div
      ref={ref}
      data-visible={visible}
      className={['reveal-section', className].filter(Boolean).join(' ')}
      style={{
        transitionDelay: visible ? `${stagger}ms` : undefined,
      }}
    >
      {children}
    </div>
  )
}
