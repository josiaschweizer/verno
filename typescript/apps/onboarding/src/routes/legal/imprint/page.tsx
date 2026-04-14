import LegalPage from '@/components/common/legal/LegalPage'
import LegalSection from '@/components/common/legal/LegalSection'

export default function Imprint() {
  return (
    <LegalPage title="Imprint">
      <LegalSection title="Provider">
        <p>
          Verno
          <br />
          Josia Schweizer
          <br />
          Schwarzenbach 2178
          <br />
          9200 Gossau
          <br />
          Switzerland
        </p>
      </LegalSection>

      <LegalSection title="Contact">
        <p>
          <a
            href="mailto:josia.schweizer@gmail.com"
            className="text-primary underline"
          >
            josia.schweizer@gmail.com
          </a>
        </p>
      </LegalSection>
    </LegalPage>
  )
}
