import { Outlet } from 'react-router-dom'
import Header from '@/components/common/navigation/header/Header'
import Footer from '@/components/common/navigation/footer/Footer'

export default function RootLayout() {
  return (
    <div className="min-h-screen bg-background text-foreground flex flex-col">
      <Header />

      <main className="flex flex-1 flex-col">
        <Outlet />
      </main>

      <Footer />
    </div>
  )
}
