import { Outlet } from 'react-router'

export default function WorkspaceStartLayout() {
  return (
    <div className="min-h-screen bg-verno-bg text-verno-darker items-center justify-center">
      <Outlet />
    </div>
  )
}
