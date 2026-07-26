import type { Config } from '@react-router/dev/config'

export default {
  appDirectory: 'src',
  buildDirectory: 'dist',
  ssr: false,
  prerender: [
    '/',
    '/company',
    '/product',
    '/pricing',
    '/legal/imprint',
    '/legal/privacy',
    '/legal/terms',
    '/workspace-starting',
  ],
} satisfies Config
