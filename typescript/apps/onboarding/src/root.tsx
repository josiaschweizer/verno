import {
  Links,
  Meta,
  Outlet,
  Scripts,
  ScrollRestoration,
  type LinksFunction,
} from 'react-router'
import { useEffect, useState } from 'react'
import Toaster from '@verno/components/toaster/Toaster'
import stylesHref from '@verno/apps/styles.css?url'
import i18n from './i18n'

const SUPPORTED_LANGUAGES = new Set(['de', 'en', 'fr'])

export const links: LinksFunction = () => [
  { rel: 'stylesheet', href: stylesHref },
]

function BrowserLanguageSync() {
  useEffect(() => {
    const updateDocumentLanguage = (language: string) => {
      document.documentElement.lang = language.split('-')[0]
    }

    const storedLanguage = window.localStorage
      .getItem('i18nextLng')
      ?.split('-')[0]
    const browserLanguage = window.navigator.language.split('-')[0]
    const detectedLanguage =
      storedLanguage && SUPPORTED_LANGUAGES.has(storedLanguage)
        ? storedLanguage
        : browserLanguage

    i18n.on('languageChanged', updateDocumentLanguage)

    if (
      SUPPORTED_LANGUAGES.has(detectedLanguage) &&
      detectedLanguage !== i18n.resolvedLanguage
    ) {
      void i18n.changeLanguage(detectedLanguage)
    } else {
      updateDocumentLanguage(i18n.resolvedLanguage ?? 'de')
    }

    return () => {
      i18n.off('languageChanged', updateDocumentLanguage)
    }
  }, [])

  return null
}

function ClientToaster() {
  const [hydrated, setHydrated] = useState(false)

  useEffect(() => {
    setHydrated(true)
  }, [])

  return hydrated ? <Toaster /> : null
}

export function Layout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="de">
      <head>
        <meta charSet="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <Meta />
        <Links />
        <link rel="icon" href="/favicon.ico" sizes="any" />
        <link
          rel="icon"
          type="image/png"
          sizes="48x48"
          href="/favicon-48.png"
        />
        <link
          rel="apple-touch-icon"
          sizes="180x180"
          href="/apple-touch-icon.png"
        />
      </head>
      <body>
        {children}
        <ScrollRestoration />
        <Scripts />
      </body>
    </html>
  )
}

export default function App() {
  return (
    <>
      <BrowserLanguageSync />
      <ClientToaster />
      <Outlet />
    </>
  )
}
