import {
  index,
  layout,
  route,
  type RouteConfig,
} from '@react-router/dev/routes'

export default [
  layout('components/layouts/RootLayout.tsx', [
    index('routes/page.tsx'),
    route('company', 'routes/company/page.tsx'),
    route('product', 'routes/product/page.tsx'),
    route('pricing', 'routes/pricing/page.tsx'),
    route('legal/imprint', 'routes/legal/imprint/page.tsx'),
    route('legal/privacy', 'routes/legal/privacy/page.tsx'),
    route('legal/terms', 'routes/legal/terms/page.tsx'),
  ]),
  layout('components/layouts/WorkspaceStartLayout.tsx', [
    route('workspace-starting', 'routes/workspace/start/page.tsx'),
  ]),
] satisfies RouteConfig
