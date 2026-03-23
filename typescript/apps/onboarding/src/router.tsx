import { createBrowserRouter } from 'react-router-dom'
import RootLayout from './components/layouts/RootLayout'
import Home from '@/routes/page'
import Company from '@/routes/company/page'
import Product from '@/routes/product/page'
import Pricing from '@/routes/pricing/page'
import Privacy from '@/routes/legal/privacy/page'
import Imprint from '@/routes/legal/imprint/page'
import Terms from '@/routes/legal/terms/page'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <RootLayout />,
    children: [
      { index: true, element: <Home /> },
      { path: 'company', element: <Company /> },
      { path: 'product', element: <Product /> },
      { path: 'pricing', element: <Pricing /> },

      { path: 'legal/imprint', element: <Imprint /> },
      { path: 'legal/privacy', element: <Privacy /> },
      { path: 'legal/terms', element: <Terms /> },
    ],
  },
])
