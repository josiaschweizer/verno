import { Outlet } from 'react-router-dom'
import Header from '../common/navigation/header/Header'

export default function RootLayout() {
  return (
    <div className="min-h-screen bg-slate-950 text-slate-100">
      <Header />

      <main className="p-4">
        <Outlet />
      </main>
    </div>
  )
}
