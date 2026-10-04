import EntityPage from '../components/EntityPage.jsx'
import { trainersApi } from '../services/api.js'
import { today } from '../utils/format.js'

const fields = [
  { name: 'name', label: 'Trainer name', required: true },
  { name: 'phone', label: 'Phone', input: 'tel', required: true },
  { name: 'email', label: 'Email', input: 'email' },
  { name: 'specialization', label: 'Specialization' },
  { name: 'experience', label: 'Experience (years)', input: 'number', type: 'number', min: 0 },
  { name: 'joiningDate', label: 'Joining date', input: 'date' },
  { name: 'status', label: 'Status', options: ['ACTIVE', 'INACTIVE'].map((value) => ({ value, label: value })) },
]

export default function TrainersPage() {
  return <EntityPage eyebrow="COACHING TEAM" title="Trainers" description="Manage trainer profiles, specialties and member assignments."
    columns={[
      { key: 'name', label: 'Trainer' }, { key: 'specialization', label: 'Specialization' },
      { key: 'experience', label: 'Experience', render: (value) => value == null ? '—' : `${value} years` },
      { key: 'phone', label: 'Phone' }, { key: 'assignedMembersCount', label: 'Assigned members' },
      { key: 'status', label: 'Status', format: 'status' },
    ]}
    fields={fields} load={trainersApi.list} create={trainersApi.create} update={trainersApi.update} remove={trainersApi.remove} adminOnly
    actionLabel="Add trainer" initialValues={{ joiningDate: today(), status: 'ACTIVE' }}
    filters={[{ label: 'All statuses', key: 'status', options: ['ACTIVE', 'INACTIVE'].map((value) => ({ value, label: value })) }]}
  />
}
