import { useState } from 'react'
import { NavLink, Outlet, useLocation, useNavigate } from 'react-router-dom'
import {
  Activity, BadgeCheck, BarChart3, Bell, CalendarCheck, CreditCard, Dumbbell,
  LayoutDashboard, LogOut, Menu, ShieldCheck, Users, Wallet, X,
} from 'lucide-react'
import { useAuth } from '../context/AuthContext.jsx'
import brandSymbol from '../assets/brand-symbol.svg'

const links = [
  { to: '/', label: 'Overview', icon: LayoutDashboard },
  { to: '/members', label: 'Members', icon: Users },
  { to: '/memberships', label: 'Memberships', icon: BadgeCheck },
  { to: '/plans', label: 'Plans', icon: Dumbbell },
  { to: '/payments', label: 'Payments', icon: Wallet },
  { to: '/attendance', label: 'Attendance', icon: CalendarCheck },
  { to: '/verify', label: 'Verify member', icon: ShieldCheck },
  { to: '/trainers', label: 'Trainers', icon: Activity },
  { to: '/reports', label: 'Reports', icon: BarChart3 },
]

export default function AppLayout() {
  const [open, setOpen] = useState(false)
  const { user, isAdmin, logout } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const title = links.find((link) => link.to === location.pathname)?.label || 'Gym management'
  const leave = () => { logout(); navigate('/login', { replace: true }) }

  return <div className="app-shell">
    {open && <button aria-label="Close navigation" className="sidebar-scrim" onClick={() => setOpen(false)} />}
    <aside className={`sidebar${open ? ' sidebar-open' : ''}`}>
      <div className="brand"><img className="brand-mark brand-image" src={brandSymbol} alt="" /><div><strong>FORM<span> / </span>HOUSE</strong><small>CLUB MANAGEMENT</small></div><button className="mobile-close icon-button" onClick={() => setOpen(false)} aria-label="Close menu"><X size={18} /></button></div>
      <div className="club-label">WORKSPACE</div>
      <nav className="sidebar-nav">
        {links.map(({ to, label, icon: Icon }) => <NavLink end={to === '/'} to={to} key={to} onClick={() => setOpen(false)} className={({ isActive }) => `nav-link${isActive ? ' nav-active' : ''}`}><Icon size={18} /><span>{label}</span></NavLink>)}
      </nav>
      {isAdmin && <div className="sidebar-admin"><div className="club-label">ADMINISTRATION</div><NavLink to="/users" onClick={() => setOpen(false)} className={({ isActive }) => `nav-link${isActive ? ' nav-active' : ''}`}><ShieldCheck size={18} /><span>Staff accounts</span></NavLink></div>}
      <div className="sidebar-bottom"><div className="sidebar-note"><div className="note-icon"><Bell size={16} /></div><div><strong>Keep your club moving</strong><p>Monitor attendance and membership health from one place.</p></div></div><button className="profile-button" onClick={leave}><div className="avatar">{(user?.fullName || user?.username || 'U').slice(0, 1).toUpperCase()}</div><div className="profile-copy"><strong>{user?.fullName || user?.username}</strong><small>{user?.role === 'ADMIN' ? 'Administrator' : 'Staff'}</small></div><LogOut size={17} /></button></div>
    </aside>
    <main className="main-shell"><header className="topbar"><button className="mobile-menu icon-button" aria-label="Open menu" onClick={() => setOpen(true)}><Menu size={20} /></button><div className="breadcrumb">Workspace <span>/</span> <b>{title}</b></div><div className="topbar-right"><div className="today-label">{new Intl.DateTimeFormat(undefined, { weekday: 'short', month: 'short', day: 'numeric' }).format(new Date())}</div><div className="role-tag">{isAdmin ? 'ADMIN' : 'STAFF'}</div></div></header><div className="page-container"><Outlet /></div></main>
  </div>
}
