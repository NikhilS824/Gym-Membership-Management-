import EntityPage from '../components/EntityPage.jsx'
import { usersApi } from '../services/api.js'

const fields = [
  { name: 'fullName', label: 'Full name', required: true },
  { name: 'username', label: 'Username', required: true },
  { name: 'email', label: 'Email', required: true, input: 'email' },
  { name: 'password', label: 'Temporary password', required: true, input: 'password' },
  { name: 'role', label: 'Role', required: true, options: [{ value: 'ADMIN', label: 'Administrator' }, { value: 'STAFF', label: 'Staff' }] },
]

export default function UsersPage() {
  return <EntityPage eyebrow="ACCESS CONTROL" title="Staff accounts" description="Create and remove accounts that can access this workspace."
    columns={[
      { key: 'fullName', label: 'Name' }, { key: 'username', label: 'Username' },
      { key: 'email', label: 'Email' }, { key: 'role', label: 'Role', format: 'status' },
    ]}
    fields={fields} load={usersApi.list} create={usersApi.create} remove={usersApi.remove}
    actionLabel="Create account" searchable={true}
  />
}
