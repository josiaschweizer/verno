import { Outlet } from 'react-router-dom'

export default function AuthLayout() {
  return (
    <div className="min-h-screen grid place-items-center">
      <div className="w-full max-w-md p-6 rounded-xl bg-slate-900/50 border border-slate-800">
        <Outlet />
      </div>
    </div>
  )
}
