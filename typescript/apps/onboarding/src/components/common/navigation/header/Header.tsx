import {
  ComponentType,
  ReactNode,
  SVGProps,
  useEffect,
  useMemo,
  useState,
} from 'react'
import { Dialog } from '@headlessui/react'
import { Bars3Icon } from '@heroicons/react/24/solid'
import {
  ArrowRightStartOnRectangleIcon,
  CalendarDaysIcon,
  ChartBarIcon,
  FolderIcon,
  UsersIcon,
} from '@heroicons/react/24/outline'
import { useTranslation } from 'react-i18next'
import { Link } from 'react-router'
import MenuPopover from './MenuPopover'
import DesktopNavLinks from './DesktopNavLinks'
import MobileMenu from './MobileMenu'
import HeaderLogo from './HeaderLogo'

import { Button } from '@verno/components/ui/button'
import RegisterMultiStepDialog from '@/components/common/register/dialog/RegisterMultiStepDialog'
import StartWorkspaceDialog from '@/components/common/tenantstart/TenantStartDialog'

export interface Product {
  name: string
  description: string
  href: string
  icon: ComponentType<SVGProps<SVGSVGElement>>
}

export default function Header() {
  const { t } = useTranslation('lib')
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)
  const [startWorkspaceOpen, setStartWorkspaceOpen] = useState(false)
  const [registerOpen, setRegisterOpen] = useState(false)

  const products: Product[] = useMemo(
    () => [
      {
        name: t('header.product.organization.name'),
        description: t('header.product.organization.description'),
        href: '/product#organization',
        icon: FolderIcon,
      },
      {
        name: t('header.product.scheduling.name'),
        description: t('header.product.scheduling.description'),
        href: '/product#scheduling',
        icon: CalendarDaysIcon,
      },
      {
        name: t('header.product.participants.name'),
        description: t('header.product.participants.description'),
        href: '/product#participants',
        icon: UsersIcon,
      },
      {
        name: t('header.product.reporting.name'),
        description: t('header.product.reporting.description'),
        href: '/product#reporting',
        icon: ChartBarIcon,
      },
    ],
    [t],
  )

  return (
    <header>
      <nav
        aria-label={t('header.aria.globalNavigation')}
        className="mx-auto flex max-w-6xl items-center justify-between p-6 lg:px-6"
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
            <span className="sr-only">{t('header.mobile.openMainMenu')}</span>
            <Bars3Icon aria-hidden className="size-6" />
          </Button>
        </div>

        <div className="hidden lg:flex lg:gap-x-12">
          <ClientOnly
            fallback={
              <Link
                to="/product"
                className="link-underline-animated text-sm/6 font-semibold text-verno-dark hover:text-verno-dark-hover"
              >
                {t('header.navigation.product')}
              </Link>
            }
          >
            <MenuPopover
              title={t('header.navigation.product')}
              products={products}
            />
          </ClientOnly>
          <DesktopNavLinks />
        </div>

        <div className="hidden lg:flex lg:flex-1 lg:justify-end lg:gap-x-3">
          <Button
            size="sm"
            variant="ghost"
            onClick={() => setStartWorkspaceOpen(true)}
            className="rounded-md gap-2"
          >
            {t('header.actions.login')}
            <ArrowRightStartOnRectangleIcon className="size-4" />
          </Button>

          <Button
            size="sm"
            onClick={() => setRegisterOpen(true)}
            className="rounded-md"
          >
            {t('header.actions.getStarted')}{' '}
            <span aria-hidden="true">&rarr;</span>
          </Button>
        </div>
      </nav>

      <ClientOnly>
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
            onStartWorkspace={() => {
              setMobileMenuOpen(false)
              setStartWorkspaceOpen(true)
            }}
          />
        </Dialog>

        <_StartWorkspaceDialogRenderer
          open={startWorkspaceOpen}
          onClose={() => setStartWorkspaceOpen(false)}
        />
        <_RegisterDialogRenderer
          open={registerOpen}
          onClose={() => setRegisterOpen(false)}
        />
      </ClientOnly>
    </header>
  )
}

function ClientOnly({
  children,
  fallback = null,
}: {
  children: ReactNode
  fallback?: ReactNode
}) {
  const [hydrated, setHydrated] = useState(false)

  useEffect(() => {
    setHydrated(true)
  }, [])

  return hydrated ? children : fallback
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

function _StartWorkspaceDialogRenderer({
  open,
  onClose,
}: {
  open: boolean
  onClose: () => void
}) {
  return <StartWorkspaceDialog open={open} onClose={onClose} />
}
