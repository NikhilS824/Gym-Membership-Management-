import { useCallback, useEffect, useState } from 'react'
import { Check, ChevronLeft, ChevronRight, LogIn } from 'lucide-react'
import { apiErrorMessage, attendanceApi, membershipsApi } from '../services/api.js'
import { useToast } from '../context/ToastContext.jsx'
import { ConfirmDialog, EmptyState, ErrorState, LoadingState, Modal, PageHeader, SearchBox, StatusPill } from '../components/UI.jsx'
import { dateTime, today } from '../utils/format.js'
import { playExpiryAlert } from '../utils/alertSound.js'

export default function AttendancePage() {
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [dateValue, setDateValue] = useState(today())
  const [query, setQuery] = useState('')
  const [page, setPage] = useState(0)
  const [checkinOpen, setCheckinOpen] = useState(false)
  const [memberId, setMemberId] = useState('')
  const [busy, setBusy] = useState(false)
  const [checkoutTarget, setCheckoutTarget] = useState(null)
  const { notify } = useToast()

  const load = useCallback(async () => {
    setLoading(true); setError('')
    try { setItems(await attendanceApi.list(dateValue)) }
    catch (err) { setError(apiErrorMessage(err)) }
    finally { setLoading(false) }
  }, [dateValue])
  useEffect(() => { load() }, [load])
  const visible = items.filter((item) => [item.memberId, item.memberName, item.memberPhone].some((value) => String(value || '').toLowerCase().includes(query.toLowerCase())))
  const pageCount = Math.max(Math.ceil(visible.length / 10), 1)
  const pageItems = visible.slice(page * 10, (page + 1) * 10)

  const checkIn = async (event) => {
    event.preventDefault()
    setBusy(true)
    try {
      const result = await membershipsApi.verify(memberId.trim())
      if (!result.allowAccess) {
        playExpiryAlert()
        notify(result.message || 'Membership is not active. Access denied.', 'error')
        return
      }
      await attendanceApi.checkIn(memberId.trim())
      notify('Member checked in.')
      setMemberId('')
      setCheckinOpen(false)
      await load()
    } catch (err) { notify(apiErrorMessage(err), 'error') }
    finally { setBusy(false) }
  }
  const checkOut = async () => {
    setBusy(true)
    try { await attendanceApi.checkOut(checkoutTarget.id); notify('Member checked out.'); setCheckoutTarget(null); await load() }
    catch (err) { notify(apiErrorMessage(err), 'error') }
    finally { setBusy(false) }
  }
  return <>
    <PageHeader eyebrow="DAILY OPERATIONS" title="Attendance" description="Check members in using their Member ID and review daily visits."
      action={<button className="button button-primary" onClick={() => setCheckinOpen(true)}><LogIn size={17} />Check in member</button>} />
    <section className="content-card">
      <div className="table-toolbar"><SearchBox value={query} onChange={(value) => { setQuery(value); setPage(0) }} placeholder="Search Member ID, name or phone…" /><label className="date-filter"><span>Attendance date</span><input type="date" value={dateValue} onChange={(event) => { setDateValue(event.target.value); setPage(0) }} /></label></div>
      {loading ? <LoadingState label="Loading attendance…" /> : error ? <ErrorState message={error} onRetry={load} /> : !visible.length ? <EmptyState title="No attendance records" description="No visits were recorded for this date or search." /> : <div className="table-scroll"><table><thead><tr><th>Member ID</th><th>Member</th><th>Phone</th><th>Check-in</th><th>Check-out</th><th>Status</th><th>Actions</th></tr></thead><tbody>{pageItems.map((item) => <tr key={item.id}><td><span className="mono-text">{item.memberId}</span></td><td className="strong-cell">{item.memberName}</td><td>{item.memberPhone || '—'}</td><td>{dateTime(item.checkInTime)}</td><td>{dateTime(item.checkOutTime)}</td><td><StatusPill value={item.status} /></td><td>{!item.checkOutTime && <button className="button button-secondary button-sm" onClick={() => setCheckoutTarget(item)}><Check size={15} />Check out</button>}</td></tr>)}</tbody></table></div>}
      {!loading && !error && visible.length > 0 && <div className="pagination"><span>{visible.length} visits · page {page + 1} of {pageCount}</span><span className="muted-caption">Membership verified by Member ID before check-in</span><div><button className="icon-button" disabled={page <= 0} onClick={() => setPage((current) => current - 1)} aria-label="Previous page"><ChevronLeft size={18} /></button><button className="icon-button" disabled={page + 1 >= pageCount} onClick={() => setPage((current) => current + 1)} aria-label="Next page"><ChevronRight size={18} /></button></div></div>}
    </section>
    {checkinOpen && <Modal title="Check in a member" onClose={() => setCheckinOpen(false)}><p className="modal-hint">Enter the Member ID to verify access before recording attendance.</p><form className="single-form" onSubmit={checkIn}><label className="field"><span>Member ID *</span><input required autoFocus value={memberId} onChange={(event) => setMemberId(event.target.value)} placeholder="e.g. Member ID" /></label><div className="form-actions"><button type="button" className="button button-secondary" onClick={() => setCheckinOpen(false)}>Cancel</button><button className="button button-primary" disabled={busy}>{busy ? 'Verifying…' : 'Verify and check in'}</button></div></form></Modal>}
    {checkoutTarget && <ConfirmDialog title="Check out member?" message={`Record check-out for ${checkoutTarget.memberName} (${checkoutTarget.memberId})?`} onCancel={() => setCheckoutTarget(null)} onConfirm={checkOut} busy={busy} />}
  </>
}
