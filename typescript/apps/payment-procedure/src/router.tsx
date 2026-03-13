import { createBrowserRouter } from 'react-router-dom'
import RootLayout from './components/layouts/RootLayout.tsx'
import Home from '@/routes/page'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <RootLayout />,
    children: [
      { index: true, element: <Home /> },
    ],
  },
])
