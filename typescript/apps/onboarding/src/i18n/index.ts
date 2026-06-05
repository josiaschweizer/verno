import i18n from 'i18next'
import { initReactI18next } from 'react-i18next'
import LanguageDetector from 'i18next-browser-languagedetector'

import deHome from './locales/de/home.json'
import enHome from './locales/en/home.json'
import frHome from './locales/fr/home.json'

import deLib from './locales/de/lib.json'
import enLib from './locales/en/lib.json'
import frLib from './locales/fr/lib.json'

import deRegister from './locales/de/register.json'
import enRegister from './locales/en/register.json'
import frRegister from './locales/fr/register.json'

export default i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    resources: {
      de: {
        home: deHome,
        lib: deLib,
        register: deRegister,
      },
      en: {
        home: enHome,
        lib: enLib,
        register: enRegister,
      },
      fr: {
        home: frHome,
        lib: frLib,
        register: frRegister,
      },
    },
    fallbackLng: 'de',
    supportedLngs: ['de', 'en', 'fr'],
    defaultNS: 'home',
    interpolation: { escapeValue: false },
  })
