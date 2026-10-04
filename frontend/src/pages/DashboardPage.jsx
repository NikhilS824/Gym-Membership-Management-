import { useEffect, useState } from 'react'
import { Activity, ArrowUpRight, BadgeAlert, CalendarDays, CircleDollarSign, Users } from 'lucide-react'
import { Area, AreaChart, CartesianGrid, Cell, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'
import { dashboardApi, apiErrorMessage } from '../services/api.js'
import { useToast } from '../context/ToastContext.jsx'
import { EmptyState, ErrorState, LoadingState, MetricCard, PageHeader, StatusPill } from '../components/UI.jsx'
import { currency, date } from '../utils/format.js'
import { playExpiryAlert } from '../utils/alertSound.js'

export default function DashboardPage() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const { notify } = useToast()
  const load = async () => {
    setLoading(true); setError('')
    try { setData(await dashboardApi.summary()) } catch (err) { setError(apiErrorMessage(err)) } finally { setLoading(false) }
  }
  useEffect(() => { load() }, [])
  useEffect(() => {
    if (data?.expiredMembers > 0) {
      playExpiryAlert()
      notify(`${data.expiredMembers} membership${data.expiredMembers === 1 ? '' : 's'} expired. Review member access.`, 'error')
    }
  }, [data, notify])
  if (loading) return <LoadingState label="Loading your club overview…" />
  if (error) return <ErrorState message={error} onRetry={load} />
  const revenue = data.revenueChart || []
  const membership = data.membershipStatusChart || []
  const attendance = data.attendanceChart || []
  return <>
    <PageHeader eyebrow="CLUB OVERVIEW" title="Good to see you." description="Here’s what’s happening across your club today." action={<button className="button button-secondary" onClick={load}><CalendarDays size={17} />Refresh overview</button>} />
    {data.expiredMembers > 0 && <div className="alert-banner"><BadgeAlert size={20} /><div><strong>Memberships need attention</strong><p>{data.expiredMembers} expired membership record{data.expiredMembers === 1 ? ' requires' : 's require'} review. Verify membership by Member ID before granting access.</p></div><button className="text-button" onClick={() => playExpiryAlert()}>Play alert</button></div>}
    <div className="metrics-grid">
      <MetricCard icon={Users} label="Total members" value={data.totalMembers ?? 0} note={`${data.newMembers ?? 0} joined in the last 30 days`} />
      <MetricCard icon={Activity} label="Active members" value={data.activeMembers ?? 0} note={`${data.expiredMembers ?? 0} expired memberships`} tone="blue" />
      <MetricCard icon={CircleDollarSign} label="Revenue this month" value={currency(data.monthlyRevenue)} note={`${currency(data.totalRevenue)} all time`} tone="purple" />
      <MetricCard icon={CalendarDays} label="Today's attendance" value={data.todayAttendance ?? 0} note={`${data.expiringSoon ?? 0} memberships expiring soon`} tone="orange" />
    </div>
    <div className="dashboard-grid">
      <section className="content-card chart-card chart-span"><div className="card-heading"><div><span className="eyebrow">PERFORMANCE</span><h2>Revenue trend</h2></div><span className="chart-label"><i className="legend-dot lime-dot" />Daily revenue</span></div>
        {revenue.length ? <ResponsiveContainer width="100%" height={250}><AreaChart data={revenue} margin={{ top: 12, right: 8, bottom: 0, left: -18 }}><defs><linearGradient id="revFill" x1="0" y1="0" x2="0" y2="1"><stop offset="0%" stopColor="#a6d94f" stopOpacity={0.22} /><stop offset="100%" stopColor="#a6d94f" stopOpacity={0.01} /></linearGradient></defs><CartesianGrid stroke="#eef0ec" vertical={false} /><XAxis dataKey="date" axisLine={false} tickLine={false} tick={{ fill: '#939b91', fontSize: 11 }} /><YAxis axisLine={false} tickLine={false} tick={{ fill: '#939b91', fontSize: 11 }} /><Tooltip formatter={(value) => currency(value)} /><Area type="monotone" dataKey="revenue" stroke="#789f31" strokeWidth={2.5} fill="url(#revFill)" /></AreaChart></ResponsiveContainer> : <EmptyState title="No revenue chart data" />}
      </section>
      <section className="content-card chart-card"><div className="card-heading"><div><span className="eyebrow">MEMBERSHIPS</span><h2>Membership health</h2></div></div>
        {membership.length ? <div className="donut-wrap"><ResponsiveContainer width="100%" height={210}><PieChart><Pie data={membership} dataKey="value" nameKey="name" innerRadius={62} outerRadius={88} paddingAngle={4} stroke="none">{membership.map((item, index) => <Cell key={`${item.name}-${index}`} fill={item.color || ['#a6d94f', '#efb55a', '#e66f61'][index % 3]} />)}</Pie><Tooltip /></PieChart></ResponsiveContainer><div className="chart-legend">{membership.map((item, index) => <div key={item.name}><span><i className="legend-dot" style={{ background: item.color || ['#a6d94f', '#efb55a', '#e66f61'][index % 3] }} />{item.name}</span><strong>{item.value}</strong></div>)}</div></div> : <EmptyState title="No membership data" />}
      </section>
      <section className="content-card chart-card"><div className="card-heading"><div><span className="eyebrow">DAILY PULSE</span><h2>Attendance this week</h2></div></div>
        {attendance.length ? <ResponsiveContainer width="100%" height={220}><AreaChart data={attendance} margin={{ top: 10, right: 8, bottom: 0, left: -18 }}><defs><linearGradient id="attFill" x1="0" y1="0" x2="0" y2="1"><stop offset="0%" stopColor="#78a9e6" stopOpacity={0.23} /><stop offset="100%" stopColor="#78a9e6" stopOpacity={0.01} /></linearGradient></defs><CartesianGrid stroke="#eef0ec" vertical={false} /><XAxis dataKey="day" axisLine={false} tickLine={false} tick={{ fill: '#939b91', fontSize: 10 }} /><YAxis axisLine={false} tickLine={false} tick={{ fill: '#939b91', fontSize: 11 }} /><Tooltip /><Area type="monotone" dataKey="attendance" stroke="#6f9bd1" strokeWidth={2.5} fill="url(#attFill)" /></AreaChart></ResponsiveContainer> : <EmptyState title="No attendance data" />}
      </section>
      <section className="content-card chart-card expiring-card"><div className="card-heading"><div><span className="eyebrow">RETENTION</span><h2>Expiring soon</h2></div><span className="subtle-count">{(data.expiringMemberships || []).length} records</span></div>
        {(data.expiringMemberships || []).length ? <div className="expiring-list">{data.expiringMemberships.map((item) => <div className="expiring-item" key={item.id}><div className="avatar avatar-soft">{(item.memberName || 'M').slice(0, 1)}</div><div className="expiring-copy"><strong>{item.memberName}</strong><small>{item.memberId} · {item.planName}</small></div><div className="expiring-end"><StatusPill value={item.daysRemaining <= 0 ? 'EXPIRED' : `${item.daysRemaining} days`} /><small>{date(item.endDate)}</small></div><ArrowUpRight size={16} className="muted-icon" /></div>)}</div> : <EmptyState title="All clear" description="No memberships are expiring within the next 7 days." />}
      </section>
    </div>
  </>
}
