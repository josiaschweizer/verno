import { useEffect, useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { billingTokensApi } from '@/lib/api/token.ts'

export default function EntryPage() {
  const [searchParams] = useSearchParams()
  const navigate = useNavigate()

  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const token = searchParams.get('token')

    if (!token) {
      setError('No token provided')
      return
    }

    async function resolveToken(token: string) {
      try {
        const result = await billingTokensApi.resolveEntryToken(token)

        sessionStorage.setItem('billingEntryToken', token)
        sessionStorage.setItem('billingEntryContext', JSON.stringify(result))

        navigate('/payment')
      } catch (e) {
        console.error(e)
        setError('Invalid or expired access link')
      }
    }

    resolveToken(token)
  }, [searchParams, navigate])

  if (error) {
    return <div>{error}</div>
  }

  return <div>Loading billing access…</div>
}
