import { createContext, useCallback, useContext, useMemo, useState } from 'react'
import { CheckCircle2, CircleAlert, Info, X } from 'lucide-react'

const ToastContext = createContext(null)
const icons = { success: CheckCircle2, error: CircleAlert, info: Info }

export function ToastProvider({ children }) {
  const [toasts, setToasts] = useState([])
  const dismiss = useCallback((id) => setToasts((items) => items.filter((item) => item.id !== id)), [])
  const notify = useCallback((message, type = 'success') => {
    const id = `${Date.now()}-${Math.random()}`
    setToasts((items) => [...items, { id, message, type }])
    window.setTimeout(() => dismiss(id), 4500)
  }, [dismiss])
  const value = useMemo(() => ({ notify }), [notify])
  return (
    <ToastContext.Provider value={value}>
      {children}
      <div className="toast-stack" aria-live="polite">
        {toasts.map(({ id, message, type }) => {
          const Icon = icons[type] || Info
          return <div className={`toast toast-${type}`} key={id}><Icon size={18} /><span>{message}</span><button aria-label="Dismiss notification" onClick={() => dismiss(id)}><X size={16} /></button></div>
        })}
      </div>
    </ToastContext.Provider>
  )
}

export const useToast = () => {
  const value = useContext(ToastContext)
  if (!value) throw new Error('useToast must be used within ToastProvider.')
  return value
}
