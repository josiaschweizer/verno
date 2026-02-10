import {
  Popover,
  PopoverButton,
  PopoverGroup,
  PopoverPanel,
  CloseButton,
} from '@headlessui/react'
import { ChevronDownIcon } from 'lucide-react'
import { Link } from 'react-router-dom'
import type { Product } from './Header'

interface Props {
  title: string
  products: Product[]
}

export default function MenuPopover({ title, products }: Props) {
  return (
    <PopoverGroup>
      <Popover className="relative">
        <PopoverButton className="flex items-center gap-x-1 text-sm/6 font-semibold text-verno-dark hover:text-verno-dark-hover outline-0">
          {title}
          <ChevronDownIcon
            aria-hidden="true"
            className="size-5 flex-none text-verno-darker/80"
          />
        </PopoverButton>

        <PopoverPanel
          transition
          className="absolute left-1/2 z-10 mt-3 w-screen max-w-md -translate-x-1/2 overflow-hidden rounded-3xl bg-verno-surface outline-1 -outline-offset-1 outline-verno-accent/10 transition data-closed:translate-y-1 data-closed:opacity-0 data-enter:duration-200 data-enter:ease-out data-leave:duration-150 data-leave:ease-in"
        >
          <div className="p-4">
            {products.map((item) => (
              <div
                key={item.name}
                className="group relative flex items-center gap-x-6 rounded-lg p-4 text-sm/6 hover:bg-verno-surface-light/20"
              >
                <div className="flex size-11 flex-none items-center justify-center rounded-lg bg-verno-bg/60 group-hover:bg-verno-bg">
                  <item.icon
                    aria-hidden="true"
                    className="size-6 text-verno-darker group-hover:text-verno-dark"
                  />
                </div>
                <div className="flex-auto">
                  <CloseButton
                    as={Link}
                    to={item.href}
                    className="block font-semibold text-verno-dark"
                  >
                    {item.name}
                    <span className="absolute inset-0" />
                  </CloseButton>
                  <p className="mt-1 text-verno-darker/80">
                    {item.description}
                  </p>
                </div>
              </div>
            ))}
          </div>
        </PopoverPanel>
      </Popover>
    </PopoverGroup>
  )
}
