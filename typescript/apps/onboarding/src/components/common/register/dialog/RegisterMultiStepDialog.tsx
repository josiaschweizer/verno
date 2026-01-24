import { useEffect, useState } from 'react'
import {
  Dialog,
  DialogBackdrop,
  DialogPanel,
  DialogTitle,
} from '@headlessui/react'
import { Button } from '@/components/ui/button'

import StepOne from '../steps/StepOne'
import StepTwo from '../steps/StepTwo'
import StepThree from '../steps/StepThree'
import { XMarkIcon } from '@heroicons/react/24/outline'
import {
  ArrowLeftIcon,
  ArrowRightIcon,
  CircleSlash,
  FlagIcon,
} from 'lucide-react'

interface Props {
  open: boolean
  onClose: () => void
}

export default function RegisterMultiStepDialog({ open, onClose }: Props) {
  const [step, setStep] = useState<number>(0)

  useEffect(() => {
    if (open) setStep(0)
  }, [open])

  const next = () => setStep((s) => Math.min(2, s + 1))
  const back = () => setStep((s) => Math.max(0, s - 1))

  return (
    <Dialog open={open} onClose={onClose} className="relative z-100">
      <div className="fixed inset-0 z-100">
        <DialogBackdrop className="fixed inset-0 bg-black/60" />

        <div className="fixed inset-0 flex items-center justify-center p-4">
          <DialogPanel className="relative z-101 w-full max-w-2xl rounded-2xl bg-verno-surface p-6 text-verno-dark shadow-xl">
            <div className="flex items-start justify-between gap-4">
              <div>
                <DialogTitle className="text-lg font-semibold">
                  Get Started
                </DialogTitle>
                <p className="mt-1 text-sm text-verno-darker/80">
                  Follow the steps to create your tenant.
                </p>
              </div>

              <div className="ml-auto flex items-center gap-2">
                <div className="flex items-center gap-1 p-2">
                  {[0, 1, 2].map((i) => (
                    <span
                      key={i}
                      className={`h-2 w-8 rounded-full transition-colors ${
                        i === step ? 'bg-verno-accent' : 'bg-verno-darker/30'
                      }`}
                      aria-hidden
                    />
                  ))}
                </div>
              </div>
            </div>

            <div className="mt-6 min-h-30">
              {step === 0 && <StepOne />}
              {step === 1 && <StepTwo />}
              {step === 2 && <StepThree />}
            </div>

            <div className="mt-6 flex items-center justify-between">
              <div>
                <Button variant="outline" onClick={back} disabled={step === 0}>
                  <ArrowLeftIcon className="h-5 w-5" /> Back
                </Button>
                <Button variant="outline" className="ml-2" onClick={onClose}>
                  <CircleSlash className="h-5 w-5" /> Cancel
                </Button>
              </div>

              <div className="flex items-center gap-2">
                {step < 2 ? (
                  <Button onClick={next}>
                    Continue <ArrowRightIcon className="h-5 w-5" />
                  </Button>
                ) : (
                  <Button
                    onClick={() => {
                      onClose()
                    }}
                  >
                    Finish <FlagIcon className="h-5 w-5" />
                  </Button>
                )}
              </div>
            </div>
          </DialogPanel>
        </div>
      </div>
    </Dialog>
  )
}
