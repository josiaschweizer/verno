import { TeamMember } from '@/types/company/team/TeamMember'

interface Props {
  member: TeamMember
  featured?: boolean
}

export default function TeamMemberUser({ member, featured = false }: Props) {
  return (
    <div
      className={`rounded-2xl bg-verno-surface shadow flex flex-col sm:flex-row items-start ${
        featured ? 'p-8 gap-6' : 'p-6 gap-4'
      }`}
    >
      <img
        src={member.image}
        alt={member.name}
        loading="lazy"
        className={`shrink-0 rounded-full object-cover ring-1 ring-verno-accent/30 ${
          featured ? 'h-24 w-24 sm:h-28 sm:w-28' : 'h-16 w-16'
        }`}
      />
      <div className="min-w-0">
        <div
          className={`font-semibold text-verno-darker ${
            featured ? 'text-base sm:text-lg' : 'text-sm'
          }`}
        >
          {member.name}
        </div>
        <div
          className={`text-verno-accent ${featured ? 'text-sm' : 'text-xs'}`}
        >
          {member.role}
        </div>
        <p
          className={`mt-2 text-muted-foreground ${
            featured ? 'text-sm sm:text-base max-w-2xl' : 'text-sm'
          }`}
        >
          {member.text}
        </p>
      </div>
    </div>
  )
}
