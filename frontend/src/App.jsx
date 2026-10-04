import { lazy, Suspense } from 'react'
import { Navigate, Route, Routes } from 'react-router-dom'
import ProtectedRoute from './components/ProtectedRoute.jsx'
import AppLayout from './layouts/AppLayout.jsx'
import { EmptyState, LoadingState } from './components/UI.jsx'
import { useAuth } from './context/AuthContext.jsx'

const LoginPage = lazy(() => import('./pages/LoginPage.jsx'))
const DashboardPage = lazy(() => import('./pages/DashboardPage.jsx'))
const MembersPage = lazy(() => import('./pages/MembersPage.jsx'))
const MembershipsPage = lazy(() => import('./pages/MembershipsPage.jsx'))
const PlansPage = lazy(() => import('./pages/PlansPage.jsx'))
const PaymentsPage = lazy(() => import('./pages/PaymentsPage.jsx'))
const AttendancePage = lazy(() => import('./pages/AttendancePage.jsx'))
const VerifyPage = lazy(() => import('./pages/VerifyPage.jsx'))
const TrainersPage = lazy(() => import('./pages/TrainersPage.jsx'))
const ReportsPage = lazy(() => import('./pages/ReportsPage.jsx'))
const UsersPage = lazy(() => import('./pages/UsersPage.jsx'))

function NotFound() {
  return <div className="not-found"><EmptyState title="Page not found" description="The address may have changed or the page does not exist." /><a className="button button-primary" href="/">Return to dashboard</a></div>
}

function UserRoute() {
  const { isAdmin } = useAuth()
  return isAdmin ? <UsersPage /> : <Navigate to="/" replace />
}

export default function App() {
  return <Suspense fallback={<LoadingState label="Opening workspace…" />}>
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/verify" element={<VerifyPage />} />
      <Route element={<ProtectedRoute />}>
        <Route element={<AppLayout />}>
          <Route index element={<DashboardPage />} />
          <Route path="members" element={<MembersPage />} />
          <Route path="memberships" element={<MembershipsPage />} />
          <Route path="plans" element={<PlansPage />} />
          <Route path="payments" element={<PaymentsPage />} />
          <Route path="attendance" element={<AttendancePage />} />
          <Route path="trainers" element={<TrainersPage />} />
          <Route path="reports" element={<ReportsPage />} />
          <Route path="users" element={<UserRoute />} />
        </Route>
      </Route>
      <Route path="*" element={<NotFound />} />
    </Routes>
  </Suspense>
}
