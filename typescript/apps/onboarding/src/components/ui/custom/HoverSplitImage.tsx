import React, { useMemo, useRef, useState } from 'react'

type HoverSplitImageProps = {
  lightSrc: string
  darkSrc: string
  alt?: string
  className?: string
  initialSplit?: number
  showHandle?: boolean
}

export function HoverSplitImage({
  lightSrc,
  darkSrc,
  alt = '',
  className = '',
  initialSplit = 0.55,
  showHandle = true,
}: HoverSplitImageProps) {
  const ref = useRef<HTMLDivElement | null>(null)
  const [split, setSplit] = useState<number>(initialSplit)

  const clipStyle = useMemo(() => {
    const right = Math.max(0, Math.min(1, 1 - split)) * 100
    return { clipPath: `inset(0 ${right}% 0 0)` }
  }, [split])

  function updateFromEvent(e: React.MouseEvent) {
    if (!ref.current) return
    const rect = ref.current.getBoundingClientRect()
    const x = e.clientX - rect.left
    const next = rect.width > 0 ? x / rect.width : initialSplit
    setSplit(Math.max(0, Math.min(1, next)))
  }

  function reset() {
    setSplit(initialSplit)
  }

  return (
    <div
      ref={ref}
      onMouseMove={updateFromEvent}
      onMouseLeave={reset}
      className={[
        'relative w-full overflow-hidden rounded-xl',
        'bg-verno-bg',
        className,
      ].join(' ')}
    >
      <img
        src={lightSrc}
        alt={alt}
        className="block w-full h-full object-cover select-none pointer-events-none"
        draggable={false}
      />

      <div className="absolute inset-0" style={clipStyle}>
        <img
          src={darkSrc}
          alt=""
          aria-hidden="true"
          className="block w-full h-full object-cover select-none pointer-events-none"
          draggable={false}
        />
      </div>

      {showHandle && (
        <div
          aria-hidden="true"
          className="absolute top-0 bottom-0 w-px bg-verno-accent/70 shadow pointer-events-none"
          style={{ left: `${split * 100}%` }}
        />
      )}

      <div className="absolute inset-0 ring-1 ring-black/5 pointer-events-none" />
    </div>
  )
}
