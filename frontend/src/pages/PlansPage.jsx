import EntityPage from '../components/EntityPage.jsx'
import { plansApi } from '../services/api.js'

const fields = [
  { name: 'name', label: 'Plan name', required: true },
  { name: 'durationValue', label: 'Duration', input: 'number', type: 'number', min: 1, required: true },
  { name: 'durationUnit', label: 'Duration unit', required: true, options: ['MONTH', 'QUARTER', 'HALF_YEAR', 'YEAR', 'CUSTOM'].map((value) => ({ value, label: value.replaceAll('_', ' ') })) },
  { name: 'price', label: 'Price (₹)', input: 'number', type: 'number', min: 0, step: '0.01', required: true },
  { name: 'status', label: 'Status', options: ['ACTIVE', 'INACTIVE'].map((value) => ({ value, label: value })) },
  { name: 'description', label: 'Description', multiline: true, wide: true },
  { name: 'benefits', label: 'Benefits', multiline: true, wide: true },
]

export default function PlansPage() {
  return <EntityPage eyebrow="OFFERINGS" title="Membership plans" description="Manage the pricing and durations available to your members."
    columns={[
      { key: 'name', label: 'Plan' }, { key: 'durationValue', label: 'Duration', render: (_value, record) => `${record.durationValue} ${String(record.durationUnit || '').replaceAll('_', ' ').toLowerCase()}` },
      { key: 'price', label: 'Price', format: 'currency' }, { key: 'description', label: 'Description' },
      { key: 'status', label: 'Status', format: 'status' },
    ]}
    fields={fields} load={plansApi.list} create={plansApi.create} update={plansApi.update} remove={plansApi.remove} adminOnly
    actionLabel="Create plan" initialValues={{ status: 'ACTIVE' }}
    filters={[{ label: 'All statuses', key: 'status', options: ['ACTIVE', 'INACTIVE'].map((value) => ({ value, label: value })) }]}
  />
}
