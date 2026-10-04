import EntityPage from '../components/EntityPage.jsx'
import { membersApi } from '../services/api.js'
import { today } from '../utils/format.js'

const fields = [
  { name: 'fullName', label: 'Full name', required: true },
  { name: 'phone', label: 'Phone', required: true, input: 'tel' },
  { name: 'email', label: 'Email', input: 'email' },
  { name: 'dateOfBirth', label: 'Date of birth', input: 'date' },
  { name: 'age', label: 'Age', input: 'number', type: 'number', min: 0 },
  { name: 'gender', label: 'Gender' },
  { name: 'address', label: 'Address', wide: true },
  { name: 'emergencyContactName', label: 'Emergency contact name' },
  { name: 'emergencyContactPhone', label: 'Emergency contact phone', input: 'tel' },
  { name: 'joiningDate', label: 'Joining date', input: 'date' },
  { name: 'trainerId', label: 'Trainer database ID', input: 'number', type: 'number', min: 1 },
  { name: 'status', label: 'Member status', options: ['ACTIVE', 'INACTIVE', 'SUSPENDED'].map((value) => ({ value, label: value })) },
]

export default function MembersPage() {
  return <EntityPage
    eyebrow="PEOPLE" title="Members" description="Member records and contact details, organized by your club."
    columns={[
      { key: 'memberId', label: 'Member ID' },
      { key: 'fullName', label: 'Member' },
      { key: 'phone', label: 'Phone' },
      { key: 'email', label: 'Email' },
      { key: 'trainerName', label: 'Trainer' },
      { key: 'joiningDate', label: 'Joined', format: 'date' },
      { key: 'status', label: 'Status', format: 'status' },
    ]}
    fields={fields} load={membersApi.list} create={membersApi.create} update={membersApi.update} remove={membersApi.remove}
    pagination deleteAdminOnly searchPlaceholder="Search name, phone or Member ID…" actionLabel="Add member"
    filters={[{ label: 'All statuses', key: 'status', options: ['ACTIVE', 'INACTIVE', 'SUSPENDED'].map((value) => ({ value, label: value })) }]}
    initialValues={{ joiningDate: today(), status: 'ACTIVE' }}
  />
}
