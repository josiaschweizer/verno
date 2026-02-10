import { useEffect } from 'react'
import { useLocation } from 'react-router-dom'

export default function Product() {
  const location = useLocation()

  useEffect(() => {
    if (location.hash) {
      const id = location.hash.replace('#', '')
      const element = document.getElementById(id)

      if (element) {
        //timout to ensure that page is already rendered
        setTimeout(() => {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' })
        }, 100)
      }
    }
  }, [location])

  return (
    <div>
      <h1>product</h1>
    </div>
  )
}
