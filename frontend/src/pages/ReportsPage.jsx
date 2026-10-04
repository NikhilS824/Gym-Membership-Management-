import { useEffect, useState } from 'react'
import { Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis, Area, AreaChart } from 'recharts'
import { BarChart3, CircleDollarSign, Users } from 'lucide-react'
import { reportsApi, apiErrorMessage } from '../services/api.js'
import { ErrorState, LoadingState, MetricCard, PageHeader, EmptyState } from '../components/UI.jsx'
import { currency } from '../utils/format.js'

function ReportChart({ title, eyebrow, data, dataKey, axisKey, color, formatter }) {
  return <section className="content-card chart-card report-chart"><div className="card-heading"><div><span className="eyebrow">{eyebrow}</span><h2>{title}</h2></div></div>
    {data?.length ? <ResponsiveContainer width="100%" height={250}><AreaChart data={data} margin={{ top: 12, right: 10, bottom: 0, left: -18 }}><defs><linearGradient id={`fill-${dataKey}`} x1="0" y1="0" x2="0" y2="1"><stop offset="0%" stopColor={color} stopOpacity={0.22} /><stop offset="100%" stopColor={color} stopOpacity={0.01} /></linearGradient></defs><CartesianGrid stroke="#eef0ec" vertical={false} /><XAxis dataKey={axisKey} axisLine={false} tickLine={false} tick={{ fill: '#939b91', fontSize: 11 }} /><YAxis axisLine={false} tickLine={false} tick={{ fill: '#939b91', fontSize: 11 }} /><Tooltip formatter={formatter} /><Area type="monotone" dataKey={dataKey} stroke={color} strokeWidth={2.5} fill={`url(#fill-${dataKey})`} /></AreaChart></ResponsiveContainer> : <EmptyState title="No report data" description="There is not enough recorded activity to display this chart." />}
  </section>
}

export default function ReportsPage() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const load = async () => {
    setLoading(true); setError('')
    try {
      const [members, revenue, memberships, attendance] = await Promise.all([
        reportsApi.members(), reportsApi.revenue(), reportsApi.memberships(), reportsApi.attendance(),
      ])
      setData({ members, revenue, memberships, attendance })
    } catch (err) { setError(apiErrorMessage(err)) }
    finally { setLoading(false) }
  }
  useEffect(() => { load() }, [])
  if (loading) return <LoadingState label="Preparing reports…" />
  if (error) return <ErrorState message={error} onRetry={load} />
  const monthly = (data.revenue.monthlyRevenueBreakdown || []).map((item) => ({ ...item, monthLabel: item.label || `${item.year}-${item.month}` }))
  const attendance = data.attendance.attendanceTrend30Days || []
  const plans = data.memberships.popularPlans || []
  return <>
    <PageHeader eyebrow="INSIGHTS" title="Reports & analytics" description="A view of member growth, recurring revenue and club activity." action={<button className="button button-secondary" onClick={load}>Refresh reports</button>} />
    <div className="metrics-grid report-metrics">
      <MetricCard icon={Users} label="Members" value={data.members.totalMembers ?? 0} note={`${data.members.newMembers30Days ?? 0} new in 30 days`} />
      <MetricCard icon={CircleDollarSign} label="Revenue this year" value={currency(data.revenue.yearlyRevenue)} note={`${currency(data.revenue.totalRevenue)} total`} tone="purple" />
      <MetricCard icon={BarChart3} label="Active memberships" value={data.memberships.activeMemberships ?? 0} note={`${data.memberships.expiringIn7Days ?? 0} expiring in 7 days`} tone="blue" />
      <MetricCard icon={BarChart3} label="Today's visits" value={data.attendance.todayAttendance ?? 0} note={`${data.memberships.expiredMemberships ?? 0} expired memberships`} tone="orange" />
    </div>
    <div className="reports-grid">
      <ReportChart title="Revenue by month" eyebrow="REVENUE" data={monthly} axisKey="monthLabel" dataKey="revenue" color="#789f31" formatter={(value) => currency(value)} />
      <ReportChart title="Attendance · last 30 days" eyebrow="ATTENDANCE" data={attendance} axisKey="date" dataKey="count" color="#6f9bd1" />
      <section className="content-card chart-card report-chart"><div className="card-heading"><div><span className="eyebrow">MEMBER MIX</span><h2>Member status</h2></div></div><div className="report-stats">{[['Active members', data.members.activeMembers], ['Inactive members', data.members.inactiveMembers], ['Suspended members', data.members.suspendedMembers]].map(([label, count]) => <div key={label}><span>{label}</span><strong>{count ?? 0}</strong></div>)}</div></section>
      <section className="content-card chart-card report-chart"><div className="card-heading"><div><span className="eyebrow">POPULARITY</span><h2>Popular plans</h2></div></div>{plans.length ? <ResponsiveContainer width="100%" height={250}><BarChart data={plans} margin={{ top: 10, right: 10, bottom: 0, left: -18 }}><CartesianGrid stroke="#eef0ec" vertical={false} /><XAxis dataKey="planName" axisLine={false} tickLine={false} tick={{ fill: '#939b91', fontSize: 11 }} /><YAxis axisLine={false} tickLine={false} tick={{ fill: '#939b91', fontSize: 11 }} /><Tooltip /><Bar dataKey="count" fill="#a6d94f" radius={[5, 5, 0, 0]} /></BarChart></ResponsiveContainer> : <EmptyState title="No plan activity" description="Plan popularity will appear when memberships are recorded." />}</section>
    </div>
  </>
}
