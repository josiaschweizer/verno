import {
  Dialog,
  DialogPanel,
  Disclosure,
  DisclosureButton,
  DisclosurePanel,
} from '@headlessui/react'
import { ChevronDownIcon } from 'lucide-react'
import { XMarkIcon } from '@heroicons/react/24/outline'
import type { Product } from './Header'

interface Props {
  products: Product[]
  onClose: () => void
}

export default function MobileMenu({ products, onClose }: Props) {
  return (
    <>
      <Dialog open={true} onClose={onClose} className="lg:hidden">
        <div className="fixed inset-0 z-50" aria-hidden />
        <DialogPanel className="fixed inset-y-0 right-0 z-50 w-full overflow-y-auto bg-verno-bg p-6 sm:max-w-sm sm:ring-1 sm:ring-verno-accent/20">
          <div className="flex items-center justify-between">
            <a href="#" className="-m-1.5 p-1.5 flex items-center gap-2">
              <img
                alt="Verno logo"
                src="https://tailwindcss.com/plus-assets/img/logos/mark.svg?color=indigo&shade=500"
                className={`w-10 h-10 rounded-md`}
              />
              <span className="text-verno-dark text-base font-semibold tracking-wide">
                Verno
              </span>
            </a>
            <button
              type="button"
              onClick={onClose}
              className="-m-2.5 rounded-md p-2.5 text-verno-darker"
            >
              <span className="sr-only">Close menu</span>
              <XMarkIcon aria-hidden="true" className="size-6" />
            </button>
          </div>

          <div className="mt-6 flow-root">
            <div className="-my-6 divide-y divide-verno-surface-light/20">
              <div className="space-y-2 py-6">
                <Disclosure as="div" className="-mx-3">
                  <DisclosureButton className="group flex w-full items-center justify-between rounded-lg py-2 pr-3.5 pl-3 text-base/7 font-semibold text-verno-dark hover:bg-verno-surface-light/20">
                    Product
                    <ChevronDownIcon
                      aria-hidden="true"
                      className="size-5 flex-none group-data-open:rotate-180 text-verno-darker/80"
                    />
                  </DisclosureButton>
                  <DisclosurePanel className="mt-2 space-y-2">
                    {products.map((item) => (
                      <Disclosure.Button
                        key={item.name}
                        as="a"
                        href={item.href}
                        className="block rounded-lg py-2 pr-3 pl-6 text-sm/7 font-semibold text-verno-dark hover:bg-verno-surface-light/20"
                      >
                        {item.name}
                      </Disclosure.Button>
                    ))}
                  </DisclosurePanel>
                </Disclosure>

                <a
                  href="/features"
                  className="-mx-3 block rounded-lg px-3 py-2 text-base/7 font-semibold text-verno-dark hover:bg-verno-surface-light/20"
                >
                  Features
                </a>
                <a
                  href="/marketplace"
                  className="-mx-3 block rounded-lg px-3 py-2 text-base/7 font-semibold text-verno-dark hover:bg-verno-surface-light/20"
                >
                  Marketplace
                </a>
                <a
                  href="/company"
                  className="-mx-3 block rounded-lg px-3 py-2 text-base/7 font-semibold text-verno-dark hover:bg-verno-surface-light/20"
                >
                  Company
                </a>
              </div>

              <div className="py-6">
                <a
                  href="/login"
                  className="-mx-3 block rounded-lg px-3 py-2.5 text-base/7 font-semibold text-verno-dark hover:bg-verno-surface-light/20"
                >
                  Log in
                </a>
              </div>
            </div>
          </div>
        </DialogPanel>
      </Dialog>
    </>
  )
}
