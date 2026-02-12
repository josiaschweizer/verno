import { Outlet } from 'react-router-dom'
import Header from '@/components/common/navigation/header/Header'

export default function RootLayout() {
  return (
    <div className="h-screen overflow-hidden bg-background text-foreground flex flex-col">
      <Header />

      <main className="p-4 flex-1 overflow-auto">
        <Outlet />
      </main>
    </div>
  )
}
