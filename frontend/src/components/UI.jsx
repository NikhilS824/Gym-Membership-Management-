import { AlertCircle, LoaderCircle, Search, X } from 'lucide-react'

export function PageHeader({ eyebrow, title, description, action }) {
  return <div className="page-header"><div><p className="eyebrow">{eyebrow}</p><h1>{title}</h1>{description && <p className="page-description">{description}</p>}</div>{action}</div>
}

export function LoadingState({ label = 'Loading…' }) {
  return <div className="state-panel"><LoaderCircle className="spin" size={25} /><span>{label}</span></div>
}

export function ErrorState({ message, onRetry }) {
  return <div className="state-panel state-error"><AlertCircle size={22} /><span>{message}</span>{onRetry && <button className="button button-secondary button-sm" onClick={onRetry}>Try again</button>}</div>
}

export function EmptyState({ title = 'Nothing here yet', description = 'When records become available, they will appear here.' }) {
  return <div className="empty-state"><div className="empty-mark">—</div><strong>{title}</strong><p>{description}</p></div>
}

export function SearchBox({ value, onChange, placeholder = 'Search…' }) {
  return <label className="search-box"><Search size={17} /><input value={value} onChange={(event) => onChange(event.target.value)} placeholder={placeholder} aria-label={placeholder} />{value && <button type="button" aria-label="Clear search" onClick={() => onChange('')}><X size={15} /></button>}</label>
}

export function StatusPill({ value }) {
  const key = String(value || 'unknown').toLowerCase().replaceAll('_', '-')
  return <span className={`status-pill status-${key}`}>{String(value || 'Unknown').replaceAll('_', ' ')}</span>
}

export function Modal({ title, children, onClose, wide = false }) {
  return <div className="modal-backdrop" onMouseDown={(event) => { if (event.target === event.currentTarget) onClose() }}>
    <section className={`modal-card${wide ? ' modal-wide' : ''}`} role="dialog" aria-modal="true" aria-label={title}>
      <div className="modal-heading"><h2>{title}</h2><button className="icon-button" onClick={onClose} aria-label="Close dialog"><X size={19} /></button></div>
      {children}
    </section>
  </div>
}

export function ConfirmDialog({ title, message, onCancel, onConfirm, busy = false }) {
  return <Modal title={title} onClose={onCancel}><p className="confirm-copy">{message}</p><div className="form-actions"><button className="button button-secondary" onClick={onCancel} disabled={busy}>Cancel</button><button className="button button-danger" onClick={onConfirm} disabled={busy}>{busy ? 'Working…' : 'Confirm'}</button></div></Modal>
}

export function MetricCard({ icon: Icon, label, value, note, tone = 'lime' }) {
  return <article className="metric-card"><div className={`metric-icon metric-${tone}`}><Icon size={19} /></div><div className="metric-label">{label}</div><strong className="metric-value">{value}</strong>{note && <div className="metric-note">{note}</div>}</article>
}
