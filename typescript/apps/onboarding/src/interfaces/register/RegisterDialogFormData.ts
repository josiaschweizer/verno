import { ComboBoxItem } from '@/type/ComboBoxItem'

export default interface RegisterDialogFormData {
  firstname: string
  lastname: string
  email: string
  password: string
  confirmPassword: string

  tenantName: string
  tenantSubdomain: string
}
