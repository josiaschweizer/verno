import React, { useEffect, useMemo, useRef, useState } from 'react'
import { X } from 'lucide-react' // or your preferred close icon

type HoverSplitImageProps = {
  lightSrc: string
  darkSrc: string
  alt?: string
  className?: string
  initialSplit?: number
  showHandle?: boolean
  objectFit?: 'cover' | 'contain'
}

export function HoverSplitImage({
  lightSrc,
  darkSrc,
  alt = '',
  className = '',
  initialSplit = 0.55,
  showHandle = true,
  objectFit = 'cover',
}: HoverSplitImageProps) {
  const ref = useRef<HTMLDivElement | null>(null)
  const [split, setSplit] = useState<number>(initialSplit)
  const [isFullscreen, setIsFullscreen] = useState(false)

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

  function openFullscreen() {
    setIsFullscreen(true)
    setSplit(initialSplit)
  }

  function closeFullscreen() {
    setIsFullscreen(false)
    setSplit(initialSplit)
  }

  useEffect(() => {
    if (!isFullscreen) return

    function handleEscape(e: KeyboardEvent) {
      if (e.key === 'Escape') {
        closeFullscreen()
      }
    }

    document.addEventListener('keydown', handleEscape)
    return () => document.removeEventListener('keydown', handleEscape)
  }, [isFullscreen])

  return (
    <>
      <div
        ref={ref}
        onMouseMove={updateFromEvent}
        onMouseLeave={reset}
        onClick={openFullscreen}
        className={[
          'relative w-full overflow-hidden rounded-xl',
          'bg-verno-bg cursor-pointer',
          className,
        ].join(' ')}
      >
        <img
          src={lightSrc}
          alt={alt}
          className={`block w-full h-full object-${objectFit} select-none pointer-events-none`}
          draggable={false}
        />

        <div className="absolute inset-0" style={clipStyle}>
          <img
            src={darkSrc}
            alt=""
            aria-hidden="true"
            className={`block w-full h-full object-${objectFit} select-none pointer-events-none`}
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

      {isFullscreen && (
        <div
          className="fixed inset-0 z-50 bg-black/90 flex items-center justify-center p-4"
          onClick={closeFullscreen}
        >
          <button
            onClick={closeFullscreen}
            className="absolute top-4 right-4 z-10 p-2 rounded-full bg-white/10 hover:bg-white/20 text-white transition-colors"
            aria-label="Close fullscreen"
          >
            <X className="w-6 h-6" />
          </button>

          <div
            className="relative w-full h-full max-w-7xl max-h-[90vh] overflow-hidden rounded-lg"
            onClick={(e) => e.stopPropagation()}
            onMouseMove={updateFromEvent}
          >
            <img
              src={lightSrc}
              alt={alt}
              className="block w-full h-full object-contain select-none pointer-events-none"
              draggable={false}
            />

            <div className="absolute inset-0" style={clipStyle}>
              <img
                src={darkSrc}
                alt=""
                aria-hidden="true"
                className="block w-full h-full object-contain select-none pointer-events-none"
                draggable={false}
              />
            </div>

            {showHandle && (
              <div
                aria-hidden="true"
                className="absolute top-0 bottom-0 w-0.5 bg-white/80 shadow-lg pointer-events-none"
                style={{ left: `${split * 100}%` }}
              >
                <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-8 h-8 rounded-full bg-white shadow-lg flex items-center justify-center">
                  <div className="flex gap-0.5">
                    <div className="w-0.5 h-4 bg-gray-600 rounded-full" />
                    <div className="w-0.5 h-4 bg-gray-600 rounded-full" />
                  </div>
                </div>
              </div>
            )}

            <div className="absolute bottom-4 left-4 px-3 py-1.5 rounded-full bg-black/50 text-white text-sm font-medium pointer-events-none">
              Light Mode
            </div>
            <div className="absolute bottom-4 right-4 px-3 py-1.5 rounded-full bg-white/20 text-white text-sm font-medium pointer-events-none">
              Dark Mode
            </div>
          </div>
        </div>
      )}
    </>
  )
}
