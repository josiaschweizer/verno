import i18n from 'i18next'
import { initReactI18next } from 'react-i18next'
import LanguageDetector from 'i18next-browser-languagedetector'

import deHome from './locales/de/home.json'
import enHome from './locales/en/home.json'
import frHome from './locales/fr/home.json'

import deLib from './locales/de/lib.json'
import enLib from './locales/en/lib.json'
import frLib from './locales/fr/lib.json'

import deContact from './locales/de/contact.json'
import enContact from './locales/en/contact.json'
import frContact from './locales/fr/contact.json'

import deCompany from './locales/de/company.json'
import enCompany from './locales/en/company.json'
import frCompany from './locales/fr/company.json'

import deLegal from './locales/de/legal.json'
import enLegal from './locales/en/legal.json'
import frLegal from './locales/fr/legal.json'

import dePricing from './locales/de/pricing.json'
import enPricing from './locales/en/pricing.json'
import frPricing from './locales/fr/pricing.json'

import deProduct from './locales/de/product.json'
import enProduct from './locales/en/product.json'
import frProduct from './locales/fr/product.json'

import deWorkspace from './locales/de/workspace.json'
import enWorkspace from './locales/en/workspace.json'
import frWorkspace from './locales/fr/workspace.json'

export default i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    resources: {
      de: {
        home: deHome,
        lib: deLib,
        contact: deContact,
        company: deCompany,
        legal: deLegal,
        pricing: dePricing,
        product: deProduct,
        workspace: deWorkspace,
      },
      en: {
        home: enHome,
        lib: enLib,
        contact: enContact,
        company: enCompany,
        legal: enLegal,
        pricing: enPricing,
        product: enProduct,
        workspace: enWorkspace,
      },
      fr: {
        home: frHome,
        lib: frLib,
        contact: frContact,
        company: frCompany,
        legal: frLegal,
        pricing: frPricing,
        product: frProduct,
        workspace: frWorkspace,
      },
    },
    fallbackLng: 'de',
    supportedLngs: ['de', 'en', 'fr'],
    defaultNS: 'home',
    interpolation: { escapeValue: false },
  })
