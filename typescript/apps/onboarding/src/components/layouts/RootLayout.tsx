import { Outlet } from 'react-router-dom'
import Header from '@/components/common/navigation/header/Header'

export default function RootLayout() {
  return (
    <div className="min-h-screen bg-background text-foreground">
      <Header />

      <main className="p-4">
        <Outlet />
      </main>
    </div>
  )
}
