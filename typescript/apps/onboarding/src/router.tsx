import { createBrowserRouter } from 'react-router-dom'
import AuthLayout from './components/layouts/AuthLayout'
import CreateTenantPage from './routes/create-tenant/page'
import RootLayout from './components/layouts/RootLayout'
import LoginPage from './routes/auth/login/page'
import SignUpPage from './routes/auth/signup/page'
import Home from '@/routes/page'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <RootLayout />,
    children: [
      { index: true, element: <Home /> },

      {
        path: 'auth',
        element: <AuthLayout />,
        children: [
          { path: 'signup', element: <SignUpPage /> },
          { path: 'login', element: <LoginPage /> },
        ],
      },
    ],
  },
])
