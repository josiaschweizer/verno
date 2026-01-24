import {Link, Outlet} from "react-router-dom";

export default function RootLayout() {
  return (
    <div className="min-h-screen bg-slate-950 text-slate-100">
      <header className="p-4 flex gap-4">
        <Link to="/">Create Tenant</Link>
        <Link to="login">Login</Link>
      </header>

      <main className="p-4">
        <Outlet/>
      </main>
    </div>
  );
}