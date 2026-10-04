import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'
import { LoadingState } from './UI.jsx'

export default function ProtectedRoute({ admin = false }) {
  const { user, checking, isAdmin } = useAuth()
  if (checking) return <LoadingState label="Verifying your session…" />
  if (!user) return <Navigate to="/login" replace />
  if (admin && !isAdmin) return <Navigate to="/" replace />
  return <Outlet />
}
