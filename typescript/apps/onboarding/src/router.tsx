import { createBrowserRouter } from 'react-router-dom'
import RootLayout from './components/layouts/RootLayout'
import Home from '@/routes/page'
import Company from '@/routes/company/page'
import Product from '@/routes/product/page'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <RootLayout />,
    children: [
      { index: true, element: <Home /> },
      { path: 'company', element: <Company /> },
      { path: 'product', element: <Product /> },
    ],
  },
])
