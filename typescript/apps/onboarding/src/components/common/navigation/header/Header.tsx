'use client'

import { ComponentType, SVGProps, useState } from 'react'
import { Dialog } from '@headlessui/react'
import { Bars3Icon } from '@heroicons/react/24/solid'
import MenuPopover from './MenuPopover'
import DesktopNavLinks from './DesktopNavLinks'
import MobileMenu from './MobileMenu'
import HeaderLogo from './HeaderLogo'
import { CursorArrowRaysIcon, FolderIcon } from '@heroicons/react/24/outline'
import { Button } from '@/components/ui/button'
import RegisterMultiStepDialog from '@/components/common/register/dialog/RegisterMultiStepDialog'

export interface Product {
  name: string
  description: string
  href: string
  icon: ComponentType<SVGProps<SVGSVGElement>>
}

const products: Product[] = [
  {
    name: 'Organization',
    description: 'Manage your participants and instructors',
    href: '/products/organization',
    icon: FolderIcon,
  },
  {
    name: 'Engagement ',
    description: 'Speak directly to your customers in a more meaningful way',
    href: '/products/engagement',
    icon: CursorArrowRaysIcon,
  },
]

export default function Header() {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)
  const [registerOpen, setRegisterOpen] = useState(false)

  return (
    <header>
      <nav
        aria-label="global"
        className="mx-auto flex max-w-7xl items-center justify-between p-6 lg:px-8"
      >
        <div className="flex lg:flex-1">
          <HeaderLogo />
        </div>

        <div className="flex lg:hidden">
          <Button
            size="sm"
            onClick={() => setMobileMenuOpen(true)}
            className="-m-2.5 inline-flex items-center justify-center rounded-md p-2.5 text-verno-dark"
          >
            <span className="sr-only">Open main menu</span>
            <Bars3Icon aria-hidden className="size-6" />
          </Button>
        </div>

        <div className="hidden lg:flex lg:gap-x-12">
          <MenuPopover title="Products" products={products} />
          <DesktopNavLinks />
        </div>

        <div className="hidden lg:flex lg:flex-1 lg:justify-end">
          <Button
            size="sm"
            onClick={() => setRegisterOpen(true)}
            className="rounded-md"
          >
            Get started <span aria-hidden="true">&rarr;</span>
          </Button>
        </div>
      </nav>

      <Dialog
        open={mobileMenuOpen}
        onClose={setMobileMenuOpen}
        className="lg:hidden"
      >
        <MobileMenu
          products={products}
          onClose={() => setMobileMenuOpen(false)}
          onRegisterOpen={() => {
            setMobileMenuOpen(false)
            setRegisterOpen(true)
          }}
        />
      </Dialog>

      <_RegisterDialogRenderer
        open={registerOpen}
        onClose={() => setRegisterOpen(false)}
      />
    </header>
  )
}

function _RegisterDialogRenderer({
  open,
  onClose,
}: {
  open: boolean
  onClose: () => void
}) {
  return <RegisterMultiStepDialog open={open} onClose={onClose} />
}
