import { Outlet } from 'react-router'

export default function RootLayout() {
  return (
    <div className="h-screen overflow-hidden bg-background text-foreground flex flex-col">
      <main className="p-4 flex-1 overflow-auto">
        <Outlet />
      </main>
    </div>
  )
}
