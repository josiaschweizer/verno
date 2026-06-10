import {
  DialogPanel,
  Disclosure,
  DisclosureButton,
  DisclosurePanel,
} from '@headlessui/react'
import { ChevronDownIcon } from 'lucide-react'
import {
  ArrowRightStartOnRectangleIcon,
  XMarkIcon,
} from '@heroicons/react/24/outline'
import { Link } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import type { Product } from './Header'
import HeaderLogo from './HeaderLogo'
import { Button } from '@verno/components/ui/button'

interface Props {
  products: Product[]
  onClose: () => void
  onRegisterOpen?: () => void
  onStartWorkspace?: () => void
}

export default function MobileMenu({
  products,
  onClose,
  onRegisterOpen,
  onStartWorkspace,
}: Props) {
  const { t } = useTranslation('lib')

  return (
    <>
      <div className="fixed inset-0 z-40 bg-verno-dark/20 backdrop-blur-sm" />

      <DialogPanel className="fixed inset-y-0 right-0 z-50 w-full overflow-y-auto bg-verno-bg px-6 py-6 sm:max-w-sm sm:ring-1 sm:ring-verno-accent/15">
        <div className="flex items-center justify-between">
          <div onClick={onClose}>
            <HeaderLogo />
          </div>

          <button
            type="button"
            onClick={onClose}
            className="inline-flex h-10 w-10 items-center justify-center rounded-md text-verno-darker transition-colors hover:bg-verno-surface-light/40"
          >
            <span className="sr-only">{t('header.mobile.closeMenu')}</span>
            <XMarkIcon aria-hidden="true" className="size-6" />
          </button>
        </div>

        <div className="mt-8 flex flex-col">
          <div className="border-t border-verno-surface-light/30 pt-6">
            <div className="space-y-1">
              <Disclosure as="div" className="rounded-xl">
                {({ open }) => (
                  <>
                    <DisclosureButton className="flex w-full items-center justify-between rounded-xl px-3 py-3 text-left text-base font-medium text-verno-dark transition-colors hover:bg-verno-surface-light/35">
                      <span>{t('header.navigation.product')}</span>
                      <ChevronDownIcon
                        aria-hidden="true"
                        className={`size-5 text-verno-darker/70 transition-transform ${
                          open ? 'rotate-180' : ''
                        }`}
                      />
                    </DisclosureButton>

                    <DisclosurePanel className="mt-1 space-y-1 pb-2">
                      {products.map((item) => (
                        <Link
                          key={item.href}
                          to={item.href}
                          onClick={onClose}
                          className="block rounded-xl px-3 py-3 text-sm text-verno-darker transition-colors hover:bg-verno-surface-light/30"
                        >
                          <div className="font-medium text-verno-dark">
                            {item.name}
                          </div>
                          <div className="mt-1 text-sm text-verno-darker/80">
                            {item.description}
                          </div>
                        </Link>
                      ))}
                    </DisclosurePanel>
                  </>
                )}
              </Disclosure>

              <Link
                to="/company"
                onClick={onClose}
                className="block rounded-xl px-3 py-3 text-base font-medium text-verno-dark transition-colors hover:bg-verno-surface-light/35"
              >
                {t('header.navigation.company')}
              </Link>

              <Link
                to="/pricing"
                onClick={onClose}
                className="block rounded-xl px-3 py-3 text-base font-medium text-verno-dark transition-colors hover:bg-verno-surface-light/35"
              >
                {t('header.navigation.pricing')}
              </Link>
            </div>
          </div>

          <div className="mt-8 border-t border-verno-surface-light/30 pt-6 space-y-6">
            <Button
              type="button"
              variant="ghost"
              onClick={() => {
                onClose()
                onStartWorkspace?.()
              }}
              className="w-full justify-center rounded-md"
            >
              {t('header.actions.login')}
              <ArrowRightStartOnRectangleIcon className="size-4" />
            </Button>
            <Button
              type="button"
              onClick={() => {
                onClose()
                onRegisterOpen?.()
              }}
              className="w-full justify-center rounded-md"
            >
              {t('header.actions.getStarted')}{' '}
              <span aria-hidden="true">&rarr;</span>
            </Button>
          </div>
        </div>
      </DialogPanel>
    </>
  )
}
