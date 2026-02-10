import { ComboBoxItem } from '@/type/ComboBoxItem'

export default interface RegisterDialogFormData {
  firstname: string
  lastname: string
  username: string
  email: string
  phone: string
  preferredLanguage?: ComboBoxItem
  password: string
  confirmPassword: string

  tenantName: string
  tenantSubdomain: string
  tenantKey?: string
}
