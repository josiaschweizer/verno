const SITE_URL = 'https://www.verno-app.ch'

export function createMeta(title: string, description: string) {
  return [
    { title },
    { name: 'description', content: description },
    { property: 'og:title', content: title },
    { property: 'og:description', content: description },
    { property: 'og:type', content: 'website' },
  ]
}

export function createCanonicalLinks(path: string) {
  return [{ rel: 'canonical', href: `${SITE_URL}${path}` }]
}
