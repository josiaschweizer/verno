'use client'

import { ComponentType, SVGProps, useState } from 'react'
import { Dialog } from '@headlessui/react'
import { Bars3Icon } from '@heroicons/react/24/solid'
import MenuPopover from './MenuPopover'
import DesktopNavLinks from './DesktopNavLinks'
import MobileMenu from './MobileMenu'
import HeaderLogo from './HeaderLogo'
import {
  FolderIcon,
  CalendarDaysIcon,
  UsersIcon,
  ChartBarIcon,
} from '@heroicons/react/24/outline'

import { Button } from '@/components/ui/button'
import RegisterMultiStepDialog from '@/components/common/register/dialog/RegisterMultiStepDialog'

export interface Product {
  name: string
  description: string
  href: string
  icon: ComponentType<SVGProps<SVGSVGElement>>
}

const product: Product[] = [
  {
    name: 'Organization',
    description: 'Manage members, instructors, roles and structure',
    href: '/product#organization',
    icon: FolderIcon,
  },
  {
    name: 'Scheduling & Courses',
    description: 'Plan courses, trainings and schedules in one place',
    href: '/product#scheduling',
    icon: CalendarDaysIcon,
  },
  {
    name: 'Participants & Memberships',
    description: 'Keep track of participants, memberships and statuses',
    href: '/product#participants',
    icon: UsersIcon,
  },
  {
    name: 'Reporting & Insights',
    description: 'Get clear insights, reports and exports instantly',
    href: '/product#reporting',
    icon: ChartBarIcon,
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
          <MenuPopover title="Product" products={product} />
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
          products={product}
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
