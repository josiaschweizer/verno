type Route = {
  method: string
  path: RegExp
  target: (match: RegExpMatchArray) => string
}

export const routes: Route[] = [
  { method: 'POST', path: /^tenants$/, target: () => '/api/v1/tenants' },
  { method: 'POST', path: /^email$/, target: () => '/api/v1/email' },
  {
    method: 'GET',
    path: /^tenants\/count$/,
    target: () => '/api/v1/tenants/count',
  },
  {
    method: 'GET',
    path: /^application\/(memberCount|courseCount)$/,
    target: (m) => `/api/v1/application/${m[1]}`,
  },
]
