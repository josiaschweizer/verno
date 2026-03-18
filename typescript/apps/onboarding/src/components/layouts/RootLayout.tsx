import { useEffect } from 'react'
import { Outlet } from 'react-router-dom'
import Header from '@/components/common/navigation/header/Header'
import { PostReloadToast } from '@/types/ui/toast/PostReloadToast'
import { toast } from 'sonner'

export const POST_RELOAD_TOAST_KEY = 'onboarding:postReloadToast'

export default function RootLayout() {
  useEffect(() => {
    const rawToast = sessionStorage.getItem(POST_RELOAD_TOAST_KEY)
    if (!rawToast) return

    sessionStorage.removeItem(POST_RELOAD_TOAST_KEY)

    try {
      const toastContent = JSON.parse(rawToast) as PostReloadToast
      if (!toastContent?.message) {
        return
      }

      toast.success(toastContent.message, {
        duration: toastContent.duration,
        description: toastContent.link ? (
          <span className="flex flex-col gap-1">
            {toastContent.description && (
              <span>{toastContent.description}</span>
            )}
            <a
              href={toastContent.link.href}
              target="_blank"
              rel="noopener noreferrer"
              className="underline font-medium"
            >
              {toastContent.link.label} →
            </a>
          </span>
        ) : (
          toastContent.description
        ),
      })
    } catch {}
  }, [])

  return (
    <div className="h-screen overflow-hidden bg-background text-foreground flex flex-col">
      <Header />

      <main className="p-4 flex-1 overflow-auto">
        <Outlet />
      </main>
    </div>
  )
}
