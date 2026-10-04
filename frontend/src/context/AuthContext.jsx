import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react'
import { authApi } from '../services/api.js'

const AuthContext = createContext(null)
const USER_KEY = 'gym_user'

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try { return JSON.parse(localStorage.getItem(USER_KEY) || 'null') } catch { return null }
  })
  const [checking, setChecking] = useState(Boolean(localStorage.getItem('gym_token')))

  const logout = useCallback(() => {
    localStorage.removeItem('gym_token')
    localStorage.removeItem(USER_KEY)
    setUser(null)
    setChecking(false)
  }, [])

  useEffect(() => {
    const onUnauthorized = () => logout()
    window.addEventListener('gym:unauthorized', onUnauthorized)
    const token = localStorage.getItem('gym_token')
    if (!token) {
      setChecking(false)
      return () => window.removeEventListener('gym:unauthorized', onUnauthorized)
    }
    authApi.me()
      .then((current) => {
        setUser(current)
        localStorage.setItem(USER_KEY, JSON.stringify(current))
      })
      .catch((error) => {
        if (error.response?.status === 401) logout()
        else console.error('Unable to verify the saved session with the backend.', error)
      })
      .finally(() => setChecking(false))
    return () => window.removeEventListener('gym:unauthorized', onUnauthorized)
  }, [logout])

  const login = async (credentials) => {
    const result = await authApi.login(credentials)
    if (!result.token) throw new Error('The server did not return an authentication token.')
    localStorage.setItem('gym_token', result.token)
    const { token: _token, tokenType: _tokenType, ...profile } = result
    localStorage.setItem(USER_KEY, JSON.stringify(profile))
    setUser(profile)
    return profile
  }

  const value = useMemo(() => ({ user, checking, login, logout, isAdmin: user?.role === 'ADMIN' }), [user, checking, logout])
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export const useAuth = () => {
  const value = useContext(AuthContext)
  if (!value) throw new Error('useAuth must be used within AuthProvider.')
  return value
}
