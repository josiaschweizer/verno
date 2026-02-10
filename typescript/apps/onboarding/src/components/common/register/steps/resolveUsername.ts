import RegisterDialogFormData from '@/interfaces/register/RegisterDialogFormData'

export default function resolveUsername(
  values: RegisterDialogFormData,
): string {
  const username = values.username?.trim()
  if (username) return username

  const firstname = values.firstname?.trim()
  const lastname = values.lastname?.trim()

  if (firstname || lastname) {
    return [firstname, lastname].filter(Boolean).join('-')
  }

  return values.email?.trim()
}
