import { useState } from 'react'
import { ArrowRight, Eye, EyeOff, LockKeyhole, UserRound } from 'lucide-react'
import { Navigate, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'
import { useToast } from '../context/ToastContext.jsx'
import { apiErrorMessage } from '../services/api.js'
import brandSymbol from '../assets/brand-symbol.svg'

export default function LoginPage() {
  const { user, login } = useAuth()
  const { notify } = useToast()
  const navigate = useNavigate()
  const [form, setForm] = useState({ usernameOrEmail: '', password: '' })
  const [visible, setVisible] = useState(false)
  const [busy, setBusy] = useState(false)
  if (user) return <Navigate to="/" replace />
  const submit = async (event) => {
    event.preventDefault()
    setBusy(true)
    try { await login(form); navigate('/', { replace: true }) }
    catch (error) { notify(apiErrorMessage(error), 'error') }
    finally { setBusy(false) }
  }
  return <main className="login-page">
    <section className="login-art"><div className="login-brand"><img className="brand-mark brand-image" src={brandSymbol} alt="" /><span>FORM / HOUSE</span></div><div className="login-art-copy"><span className="eyebrow">YOUR CLUB. IN SYNC.</span><h1>Stronger<br />operations.<br /><em>Better training.</em></h1><p>The essential workspace for managing members, memberships and the daily rhythm of your gym.</p></div><div className="login-art-bottom"><span>MEMBER EXPERIENCE, BUILT EVERY DAY</span><span>01 — 04</span></div></section>
    <section className="login-panel"><div className="login-form-wrap"><div className="login-mobile-brand"><img className="brand-mark brand-image" src={brandSymbol} alt="" /><strong>FORM / HOUSE</strong></div><div className="login-heading"><div className="login-icon"><LockKeyhole size={21} /></div><p className="eyebrow">WELCOME BACK</p><h2>Sign in to your workspace</h2><p>Use your club account to access the management dashboard.</p></div>
      <form onSubmit={submit} className="login-form"><label className="field"><span>Username or email</span><div className="input-icon"><UserRound size={17} /><input autoComplete="username" required value={form.usernameOrEmail} onChange={(e) => setForm({ ...form, usernameOrEmail: e.target.value })} placeholder="Enter your username or email" /></div></label><label className="field"><span>Password</span><div className="input-icon"><LockKeyhole size={17} /><input autoComplete="current-password" required type={visible ? 'text' : 'password'} value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} placeholder="Enter your password" /><button type="button" onClick={() => setVisible(!visible)} aria-label={visible ? 'Hide password' : 'Show password'}>{visible ? <EyeOff size={17} /> : <Eye size={17} />}</button></div></label><button className="button button-primary login-submit" disabled={busy}>{busy ? 'Signing in…' : <>Sign in <ArrowRight size={17} /></>}</button></form><div className="login-foot">Your login is secured with token-based authentication.</div></div></section>
  </main>
}
