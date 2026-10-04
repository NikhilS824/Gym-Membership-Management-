import { useEffect, useMemo, useState } from 'react'
import { RotateCcw } from 'lucide-react'
import EntityPage from '../components/EntityPage.jsx'
import { Modal } from '../components/UI.jsx'
import { apiErrorMessage, membershipsApi, plansApi } from '../services/api.js'
import { useToast } from '../context/ToastContext.jsx'
import { today } from '../utils/format.js'

const methods = ['CASH', 'UPI', 'CARD', 'BANK_TRANSFER', 'OTHER'].map((value) => ({ value, label: value.replaceAll('_', ' ') }))
const createFields = [
  { name: 'memberId', label: 'Member ID', required: true, placeholder: 'Enter the exact Member ID' },
  { name: 'planId', label: 'Plan', required: true, input: 'number', type: 'number', min: 1 },
  { name: 'startDate', label: 'Start date', input: 'date' },
  { name: 'discount', label: 'Discount (₹)', input: 'number', type: 'number', min: 0, step: '0.01' },
  { name: 'paymentMethod', label: 'Payment method', options: methods },
  { name: 'transactionReference', label: 'Transaction reference' },
  { name: 'notes', label: 'Notes', multiline: true, wide: true },
]

function RenewDialog({ membership, plans, onClose, onSubmit, busy }) {
  const [form, setForm] = useState({ planId: '', discount: '', paymentMethod: '', transactionReference: '', notes: '' })
  const submit = (event) => {
    event.preventDefault()
    onSubmit({
      planId: Number(form.planId),
      discount: form.discount === '' ? null : Number(form.discount),
      paymentMethod: form.paymentMethod || null,
      transactionReference: form.transactionReference || null,
      notes: form.notes || null,
    })
  }
  return <Modal title={`Renew ${membership.memberName}`} onClose={onClose} wide><p className="modal-hint">Member ID <strong>{membership.memberId}</strong> · choose the next plan.</p><form onSubmit={submit} className="form-grid">
    <label className="field field-wide"><span>Plan *</span><select required value={form.planId} onChange={(e) => setForm({ ...form, planId: e.target.value })}><option value="">Select a plan</option>{plans.map((plan) => <option value={plan.id} key={plan.id}>{plan.name} · ₹{Number(plan.price).toLocaleString('en-IN')}</option>)}</select></label>
    <label className="field"><span>Discount (₹)</span><input min="0" step="0.01" type="number" value={form.discount} onChange={(e) => setForm({ ...form, discount: e.target.value })} /></label>
    <label className="field"><span>Payment method</span><select value={form.paymentMethod} onChange={(e) => setForm({ ...form, paymentMethod: e.target.value })}><option value="">Not specified</option>{methods.map((method) => <option value={method.value} key={method.value}>{method.label}</option>)}</select></label>
    <label className="field"><span>Transaction reference</span><input value={form.transactionReference} onChange={(e) => setForm({ ...form, transactionReference: e.target.value })} /></label>
    <label className="field field-wide"><span>Notes</span><textarea rows="3" value={form.notes} onChange={(e) => setForm({ ...form, notes: e.target.value })} /></label>
    <div className="form-actions field-wide"><button type="button" className="button button-secondary" onClick={onClose}>Cancel</button><button className="button button-primary" disabled={busy}>{busy ? 'Renewing…' : 'Renew membership'}</button></div>
  </form></Modal>
}

export default function MembershipsPage() {
  const [plans, setPlans] = useState([])
  const [renewing, setRenewing] = useState(null)
  const [busy, setBusy] = useState(false)
  const [refreshKey, setRefreshKey] = useState(0)
  const { notify } = useToast()
  useEffect(() => { plansApi.active().then(setPlans).catch((error) => notify(apiErrorMessage(error), 'error')) }, [notify])
  const membershipFields = useMemo(() => createFields.map((field) => field.name === 'planId'
    ? { ...field, options: plans.map((plan) => ({ value: plan.id, label: `${plan.name} · ₹${Number(plan.price).toLocaleString('en-IN')}` })) }
    : field), [plans])
  const renew = async (payload) => {
    setBusy(true)
    try {
      await membershipsApi.renew(renewing.id, payload)
      notify('Membership renewed.')
      setRenewing(null)
      setRefreshKey((key) => key + 1)
    } catch (error) { notify(apiErrorMessage(error), 'error') }
    finally { setBusy(false) }
  }
  return <>
    <EntityPage eyebrow="MEMBERSHIPS" title="Memberships" description="Track plan coverage, expiration dates and renewals."
      columns={[
        { key: 'memberId', label: 'Member ID' }, { key: 'memberName', label: 'Member' },
        { key: 'planName', label: 'Plan' }, { key: 'startDate', label: 'Start date', format: 'date' },
        { key: 'endDate', label: 'End date', format: 'date' }, { key: 'daysRemaining', label: 'Days remaining' },
        { key: 'status', label: 'Status', format: 'status' },
      ]}
      fields={membershipFields} load={membershipsApi.list} create={membershipsApi.create}
      actionLabel="New membership" initialValues={{ startDate: today() }} refreshKey={refreshKey}
      filters={[{ label: 'All statuses', key: 'status', options: ['ACTIVE', 'EXPIRED', 'SUSPENDED', 'CANCELLED'].map((value) => ({ value, label: value })) }]}
      actions={(record) => <button className="icon-button renew-button" title="Renew membership" aria-label="Renew membership" onClick={() => setRenewing(record)}><RotateCcw size={16} /></button>}
    />
    {renewing && <RenewDialog membership={renewing} plans={plans} onClose={() => setRenewing(null)} onSubmit={renew} busy={busy} />}
  </>
}
