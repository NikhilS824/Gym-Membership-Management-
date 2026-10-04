import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  headers: { 'Content-Type': 'application/json' },
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('gym_token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('gym_token')
      localStorage.removeItem('gym_user')
      window.dispatchEvent(new Event('gym:unauthorized'))
    }
    return Promise.reject(error)
  },
)

export const apiErrorMessage = (error) => {
  const data = error?.response?.data
  if (data?.errors && Object.keys(data.errors).length) return Object.values(data.errors).join(' · ')
  return data?.message || error?.message || 'The request could not be completed.'
}

export const authApi = {
  login: (payload) => api.post('/api/auth/login', payload).then((r) => r.data),
  me: () => api.get('/api/auth/me').then((r) => r.data),
}

export const membersApi = {
  list: (params) => api.get('/api/members', { params }).then((r) => r.data),
  all: () => api.get('/api/members/all').then((r) => r.data),
  get: (id) => api.get(`/api/members/${id}`).then((r) => r.data),
  create: (body) => api.post('/api/members', body).then((r) => r.data),
  update: (id, body) => api.put(`/api/members/${id}`, body).then((r) => r.data),
  status: (id, status) => api.patch(`/api/members/${id}/status`, null, { params: { status } }).then((r) => r.data),
  remove: (id) => api.delete(`/api/members/${id}`),
  byMemberId: (memberId) => api.get(`/api/members/code/${encodeURIComponent(memberId)}`).then((r) => r.data),
}

export const plansApi = {
  list: () => api.get('/api/plans').then((r) => r.data),
  active: () => api.get('/api/plans/active').then((r) => r.data),
  create: (body) => api.post('/api/plans', body).then((r) => r.data),
  update: (id, body) => api.put(`/api/plans/${id}`, body).then((r) => r.data),
  remove: (id) => api.delete(`/api/plans/${id}`),
}

export const membershipsApi = {
  list: () => api.get('/api/memberships').then((r) => r.data),
  byMemberId: (memberId) => api.get(`/api/memberships/member/${encodeURIComponent(memberId)}`).then((r) => r.data),
  create: (body) => api.post('/api/memberships', body).then((r) => r.data),
  renew: (id, body) => api.post(`/api/memberships/${id}/renew`, body).then((r) => r.data),
  verify: (memberId) => api.get(`/api/membership/verify/${encodeURIComponent(memberId)}`).then((r) => r.data),
}

export const paymentsApi = {
  list: () => api.get('/api/payments').then((r) => r.data),
  byMemberId: (memberId) => api.get(`/api/payments/member/${encodeURIComponent(memberId)}`).then((r) => r.data),
  create: (body) => api.post('/api/payments', body).then((r) => r.data),
}

export const attendanceApi = {
  list: (date) => api.get('/api/attendance', { params: date ? { date } : {} }).then((r) => r.data),
  byMemberId: (memberId) => api.get(`/api/attendance/member/${encodeURIComponent(memberId)}`).then((r) => r.data),
  checkIn: (memberId) => api.post('/api/attendance/check-in', { memberId }).then((r) => r.data),
  checkOut: (id) => api.post(`/api/attendance/check-out/${id}`).then((r) => r.data),
}

export const trainersApi = {
  list: () => api.get('/api/trainers').then((r) => r.data),
  active: () => api.get('/api/trainers/active').then((r) => r.data),
  create: (body) => api.post('/api/trainers', body).then((r) => r.data),
  update: (id, body) => api.put(`/api/trainers/${id}`, body).then((r) => r.data),
  remove: (id) => api.delete(`/api/trainers/${id}`),
}

export const dashboardApi = { summary: () => api.get('/api/dashboard/summary').then((r) => r.data) }
export const reportsApi = {
  members: () => api.get('/api/reports/member').then((r) => r.data),
  revenue: () => api.get('/api/reports/revenue').then((r) => r.data),
  memberships: () => api.get('/api/reports/membership').then((r) => r.data),
  attendance: () => api.get('/api/reports/attendance').then((r) => r.data),
}
export const usersApi = {
  list: () => api.get('/api/users').then((r) => r.data),
  create: (body) => api.post('/api/users', body).then((r) => r.data),
  remove: (id) => api.delete(`/api/users/${id}`),
}

export default api
