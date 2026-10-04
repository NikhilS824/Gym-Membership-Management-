import { useState } from 'react'
import { ArrowLeft, BadgeCheck, Search, ShieldAlert, ShieldCheck } from 'lucide-react'
import { Link } from 'react-router-dom'
import { apiErrorMessage, membershipsApi } from '../services/api.js'
import { ErrorState, LoadingState, StatusPill } from '../components/UI.jsx'
import { date } from '../utils/format.js'
import { playExpiryAlert } from '../utils/alertSound.js'

export default function VerifyPage() {
  const [memberId, setMemberId] = useState('')
  const [result, setResult] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const verify = async (event) => {
    event.preventDefault()
    setLoading(true); setError(''); setResult(null)
    try {
      const record = await membershipsApi.verify(memberId.trim())
      setResult(record)
      if (!record.allowAccess) playExpiryAlert()
    } catch (err) { setError(apiErrorMessage(err)) }
    finally { setLoading(false) }
  }
  return <main className="verify-page"><header className="verify-top"><Link to="/" className="verify-brand"><span className="brand-mark"><ShieldCheck size={18} /></span> CLUB ACCESS</Link><span>MEMBERSHIP VERIFICATION</span></header><section className="verify-content"><Link className="back-link" to="/"><ArrowLeft size={16} />Back to workspace</Link><div className="verify-intro"><div className="verify-icon"><ShieldCheck size={24} /></div><p className="eyebrow">MEMBER ACCESS</p><h1>Verify membership</h1><p>Confirm a member’s current access status using their unique Member ID.</p></div><form className="verify-search" onSubmit={verify}><label className="field"><span>Member ID</span><input value={memberId} onChange={(event) => setMemberId(event.target.value)} required placeholder="Enter Member ID" autoFocus /></label><button className="button button-primary" disabled={loading}><Search size={17} />{loading ? 'Checking…' : 'Verify member'}</button></form>{loading && <LoadingState label="Checking membership status…" />}{error && <ErrorState message={error} />}{result && <section className={`verify-result ${result.allowAccess ? 'verify-allowed' : 'verify-denied'}`}><div className="verify-result-head"><div className="verify-result-symbol">{result.allowAccess ? <BadgeCheck size={25} /> : <ShieldAlert size={25} />}</div><div><span className="eyebrow">{result.allowAccess ? 'ACCESS APPROVED' : 'ACCESS DENIED'}</span><h2>{result.memberName || 'Member record'}</h2><p>{result.memberId}</p></div><StatusPill value={result.verificationStatus} /></div><div className="verify-info-grid"><div><span>Membership plan</span><strong>{result.planName || '—'}</strong></div><div><span>Membership period</span><strong>{date(result.startDate)} — {date(result.endDate)}</strong></div><div><span>Days remaining</span><strong>{result.daysRemaining}</strong></div><div><span>Contact phone</span><strong>{result.phone || '—'}</strong></div></div><p className="verify-message">{result.message}</p></section>}</section><footer className="verify-footer">Member ID is the sole identifier used for membership verification.</footer></main>
}
