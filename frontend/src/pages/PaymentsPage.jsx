import EntityPage from '../components/EntityPage.jsx'
import { paymentsApi } from '../services/api.js'

const fields = [
  { name: 'memberId', label: 'Member ID', required: true, placeholder: 'Enter the exact Member ID' },
  { name: 'membershipId', label: 'Membership database ID', input: 'number', type: 'number', min: 1 },
  { name: 'amount', label: 'Amount (₹)', input: 'number', type: 'number', min: 0.01, step: '0.01', required: true },
  { name: 'discount', label: 'Discount (₹)', input: 'number', type: 'number', min: 0, step: '0.01' },
  { name: 'paymentMethod', label: 'Payment method', required: true, options: ['CASH', 'UPI', 'CARD', 'BANK_TRANSFER', 'OTHER'].map((value) => ({ value, label: value.replaceAll('_', ' ') })) },
  { name: 'paymentStatus', label: 'Payment status', options: ['PAID', 'PENDING', 'FAILED', 'REFUNDED'].map((value) => ({ value, label: value })) },
  { name: 'transactionReference', label: 'Transaction reference' },
  { name: 'notes', label: 'Notes', multiline: true, wide: true },
]

export default function PaymentsPage() {
  return <EntityPage eyebrow="FINANCE" title="Payments" description="Review payment activity and record member transactions."
    columns={[
      { key: 'paymentDate', label: 'Date', format: 'datetime' }, { key: 'memberId', label: 'Member ID' },
      { key: 'memberName', label: 'Member' }, { key: 'planName', label: 'Plan' },
      { key: 'amount', label: 'Amount', format: 'currency' }, { key: 'discount', label: 'Discount', format: 'currency' },
      { key: 'finalAmount', label: 'Paid', format: 'currency' }, { key: 'paymentMethod', label: 'Method', format: 'enum' },
      { key: 'paymentStatus', label: 'Status', format: 'status' },
    ]}
    fields={fields} load={paymentsApi.list} create={paymentsApi.create} actionLabel="Record payment"
    filters={[{ label: 'All statuses', key: 'paymentStatus', options: ['PAID', 'PENDING', 'FAILED', 'REFUNDED'].map((value) => ({ value, label: value })) }]}
  />
}
