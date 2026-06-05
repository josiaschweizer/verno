import React, { useEffect, useMemo, useRef, useState } from 'react'
import { X } from 'lucide-react'

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
  const containerRef = useRef<HTMLDivElement | null>(null)
  const dialogRef = useRef<HTMLDialogElement | null>(null)
  const dialogInnerRef = useRef<HTMLDivElement | null>(null)
  const isDragging = useRef(false)
  const [split, setSplit] = useState<number>(initialSplit)
  const [dialogSplit, setDialogSplit] = useState<number>(initialSplit)

  const clipStyle = useMemo(() => {
    const right = Math.max(0, Math.min(1, 1 - split)) * 100
    return { clipPath: `inset(0 ${right}% 0 0)` }
  }, [split])

  const dialogClipStyle = useMemo(() => {
    const right = Math.max(0, Math.min(1, 1 - dialogSplit)) * 100
    return { clipPath: `inset(0 ${right}% 0 0)` }
  }, [dialogSplit])

  function updateFromEvent(e: React.MouseEvent) {
    if (!containerRef.current) return
    const rect = containerRef.current.getBoundingClientRect()
    setSplit(Math.max(0, Math.min(1, (e.clientX - rect.left) / rect.width)))
  }

  function updateDialogSplitFromEvent(e: React.MouseEvent) {
    if (!dialogInnerRef.current) return
    const rect = dialogInnerRef.current.getBoundingClientRect()
    setDialogSplit(
      Math.max(0, Math.min(1, (e.clientX - rect.left) / rect.width)),
    )
  }

  function handleDialogMouseDown(e: React.MouseEvent) {
    isDragging.current = true
    updateDialogSplitFromEvent(e)
  }

  function handleDialogMouseMove(e: React.MouseEvent) {
    if (!isDragging.current) return
    updateDialogSplitFromEvent(e)
  }

  function handleDialogMouseUp() {
    isDragging.current = false
  }

  function openDialog() {
    setDialogSplit(initialSplit)
    dialogRef.current?.showModal()
  }

  function closeDialog() {
    dialogRef.current?.close()
  }

  useEffect(() => {
    const dialog = dialogRef.current
    if (!dialog) return
    const handleClose = () => {
      setDialogSplit(initialSplit)
      isDragging.current = false
    }
    dialog.addEventListener('close', handleClose)
    return () => dialog.removeEventListener('close', handleClose)
  }, [initialSplit])

  return (
    <>
      <div
        ref={containerRef}
        onMouseMove={updateFromEvent}
        onMouseLeave={() => setSplit(initialSplit)}
        onClick={openDialog}
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

      <dialog
        ref={dialogRef}
        onClick={(e) => {
          if (e.target === dialogRef.current) closeDialog()
        }}
        className="m-0 p-3 w-screen h-screen max-w-none max-h-none bg-transparent backdrop:bg-black/90"
      >
        <div className="flex flex-col w-full h-full gap-2">
          <div className="flex justify-end shrink-0">
            <button
              onClick={closeDialog}
              className="p-2 rounded-full bg-white/10 hover:bg-white/20 text-white transition-colors"
              aria-label="Vollbild schliessen"
            >
              <X className="w-6 h-6" />
            </button>
          </div>

          <div
            ref={dialogInnerRef}
            onMouseDown={handleDialogMouseDown}
            onMouseMove={handleDialogMouseMove}
            onMouseUp={handleDialogMouseUp}
            onMouseLeave={handleDialogMouseUp}
            className="relative w-full min-h-0 flex-1 overflow-hidden cursor-col-resize select-none rounded-xl"
          >
            <img
              src={lightSrc}
              alt={alt}
              className="block w-full h-full object-contain pointer-events-none"
              draggable={false}
            />
            <div className="absolute inset-0" style={dialogClipStyle}>
              <img
                src={darkSrc}
                alt=""
                aria-hidden="true"
                className="block w-full h-full object-contain pointer-events-none"
                draggable={false}
              />
            </div>

            {showHandle && (
              <div
                aria-hidden="true"
                className="absolute top-0 bottom-0 w-0.5 bg-white/80 shadow-lg pointer-events-none"
                style={{ left: `${dialogSplit * 100}%` }}
              >
                <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-8 h-8 rounded-full bg-white shadow-lg flex items-center justify-center">
                  <div className="flex gap-0.5">
                    <div className="w-0.5 h-4 bg-gray-600 rounded-full" />
                    <div className="w-0.5 h-4 bg-gray-600 rounded-full" />
                  </div>
                </div>
              </div>
            )}
          </div>

          <div className="flex justify-between items-center shrink-0 px-1">
            <span className="px-3 py-1.5 rounded-full bg-white/10 text-white text-sm font-medium">
              Light Mode
            </span>
            <span className="px-3 py-1.5 rounded-full bg-white/10 text-white text-sm font-medium">
              Dark Mode
            </span>
          </div>
        </div>
      </dialog>
    </>
  )
}
