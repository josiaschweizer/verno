import LegalPage from '@/components/common/legal/LegalPage'
import LegalSection from '@/components/common/legal/LegalSection'

export function Terms() {
  return (
    <LegalPage title="Terms of Service">
      <p>These Terms of Service govern your use of Verno and its services.</p>
      <LegalSection title="1. Scope">
        <p>
          Verno provides software for managing clubs, teams, and related
          operations.
        </p>
      </LegalSection>
      <LegalSection title="2. User accounts">
        <p>
          Users are responsible for maintaining the confidentiality of their
          account credentials.
        </p>
      </LegalSection>
      di
      <LegalSection title="3. Acceptable use">
        <p>
          You agree not to misuse the service or use it for unlawful purposes.
        </p>
      </LegalSection>
      <LegalSection title="4. Availability">
        <p>
          We strive to provide a reliable service but do not guarantee
          uninterrupted availability.
        </p>
      </LegalSection>
      <LegalSection title="5. Limitation of liability">
        <p>
          Verno is provided &quot;as is&quot;. We are not liable for indirect or
          consequential damages.
        </p>
      </LegalSection>
      <LegalSection title="6. Changes">
        <p>We may update these terms at any time.</p>
      </LegalSection>
      <LegalSection title="7. Contact">
        <p>josia.schweizer@gmail.com</p>
      </LegalSection>
    </LegalPage>
  )
}
