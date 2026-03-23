import { Outlet } from 'react-router-dom'
import Header from '@/components/common/navigation/header/Header'
import Footer from '@/components/common/navigation/footer/Footer'

export default function RootLayout() {
  return (
    <div className="h-screen overflow-hidden bg-background text-foreground flex flex-col">
      <Header />

      <main className="flex-1 min-h-0 overflow-y-auto overflow-x-hidden">
        <Outlet />
      </main>

      <Footer />
    </div>
  )
}
