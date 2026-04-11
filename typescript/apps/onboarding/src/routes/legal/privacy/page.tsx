import LegalPage from '@/components/common/legal/LegalPage'
import LegalSection from '@/components/common/legal/LegalSection'

export default function Privacy() {
  return (
    <LegalPage title="Privacy Policy">
      <p>
        This Privacy Policy describes how Verno collects, uses and processes
        personal data when using our website and services.
      </p>

      <LegalSection title="1. Data we collect">
        <p>
          We may collect personal information such as your name, email address,
          and account-related data when you register or use our service.
        </p>
      </LegalSection>

      <LegalSection title="2. Purpose of processing">
        <p>
          We process your data to provide and improve our services, manage user
          accounts, and ensure security and stability.
        </p>
      </LegalSection>

      <LegalSection title="3. Hosting and infrastructure">
        <p>
          Our services are hosted in Switzerland (Google Cloud, europe-west6).
          Data is stored and processed within Switzerland.
        </p>
      </LegalSection>

      <LegalSection title="4. Third-party services">
        <p>
          We may use third-party services such as hosting providers and
          infrastructure services. These providers process data on our behalf.
        </p>
      </LegalSection>

      <LegalSection title="5. Data security">
        <p>
          We implement appropriate technical and organizational measures to
          protect your data.
        </p>
      </LegalSection>

      <LegalSection title="6. Your rights">
        <p>
          You have the right to request access, correction, or deletion of your
          personal data.
        </p>
      </LegalSection>

      <LegalSection title="7. Contact">
        <p>
          For any privacy-related requests, please contact:
          <br />
          josia.schweizer@gmail.com
        </p>
      </LegalSection>
    </LegalPage>
  )
}
