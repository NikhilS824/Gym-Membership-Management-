import { useCallback, useEffect, useMemo, useState } from 'react'
import { ChevronLeft, ChevronRight, Pencil, Plus, Trash2 } from 'lucide-react'
import { apiErrorMessage } from '../services/api.js'
import { useToast } from '../context/ToastContext.jsx'
import { useAuth } from '../context/AuthContext.jsx'
import { useDebouncedValue } from '../hooks/useDebouncedValue.js'
import { ConfirmDialog, EmptyState, ErrorState, LoadingState, Modal, PageHeader, SearchBox, StatusPill } from './UI.jsx'
import { date, dateTime, titleCase } from '../utils/format.js'

const PAGE_SIZE = 10

const formatCell = (value, column, record) => {
  if (column.render) return column.render(value, record)
  if (column.format === 'date') return date(value)
  if (column.format === 'datetime') return dateTime(value)
  if (column.format === 'currency') return value == null ? '—' : `₹${Number(value).toLocaleString('en-IN')}`
  if (column.format === 'status') return <StatusPill value={value} />
  return value === null || value === undefined || value === '' ? '—' : column.format === 'enum' ? titleCase(value) : String(value)
}

function RecordForm({ title, fields, value = {}, onCancel, onSubmit, busy }) {
  const [data, setData] = useState(() => ({ ...value }))
  const set = (key, val) => setData((current) => ({ ...current, [key]: val }))
  const handleSubmit = (event) => {
    event.preventDefault()
    const payload = {}
    fields.forEach((field) => {
      const raw = data[field.name]
      if (raw === '' || raw === undefined) { if (!field.omitEmpty) payload[field.name] = null; return }
      payload[field.name] = field.type === 'number' ? Number(raw) : raw
    })
    onSubmit(payload)
  }
  return <Modal title={title} onClose={onCancel} wide>
    <form className="form-grid" onSubmit={handleSubmit}>
      {fields.map((field) => <label className={field.wide ? 'field field-wide' : 'field'} key={field.name}>
        <span>{field.label}{field.required && <i> *</i>}</span>
        {field.options ? <select required={field.required} value={data[field.name] ?? ''} onChange={(e) => set(field.name, e.target.value)}><option value="">Select {field.label.toLowerCase()}</option>{field.options.map((item) => <option key={item.value} value={item.value}>{item.label}</option>)}</select>
          : field.multiline ? <textarea required={field.required} rows={3} value={data[field.name] ?? ''} onChange={(e) => set(field.name, e.target.value)} placeholder={field.placeholder || ''} />
            : <input required={field.required} type={field.input || 'text'} min={field.min} max={field.max} step={field.step} value={data[field.name] ?? ''} onChange={(e) => set(field.name, e.target.value)} placeholder={field.placeholder || ''} />}
      </label>)}
      <div className="form-actions field-wide"><button className="button button-secondary" type="button" onClick={onCancel}>Cancel</button><button className="button button-primary" type="submit" disabled={busy}>{busy ? 'Saving…' : 'Save record'}</button></div>
    </form>
  </Modal>
}

export default function EntityPage({ title, eyebrow, description, columns, fields, load, create, update, remove, searchPlaceholder, searchable = true, filters = [], adminOnly = false, deleteAdminOnly = false, actionLabel = 'Add record', initialValues = {}, actions, pagination = false, refreshKey = 0 }) {
  const { notify } = useToast()
  const { isAdmin } = useAuth()
  const canCreate = Boolean(create) && (!adminOnly || isAdmin)
  const canUpdate = Boolean(update) && (!adminOnly || isAdmin)
  const canRemove = Boolean(remove) && ((!adminOnly && !deleteAdminOnly) || isAdmin)
  const [records, setRecords] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [query, setQuery] = useState('')
  const [filter, setFilter] = useState('')
  const [page, setPage] = useState(0)
  const debouncedQuery = useDebouncedValue(query)
  const [pageInfo, setPageInfo] = useState(null)
  const [modal, setModal] = useState(null)
  const [busy, setBusy] = useState(false)
  const [removeTarget, setRemoveTarget] = useState(null)
  const requestPage = pagination ? page : 0

  const reload = useCallback(async () => {
    setLoading(true)
    setError('')
    try {
      const result = await load({ query: debouncedQuery || undefined, status: filter || undefined, page: requestPage, size: PAGE_SIZE, sortBy: 'id', sortDir: 'DESC' })
      if (pagination && result && !Array.isArray(result)) {
        setRecords(result.content || [])
        setPageInfo({ totalPages: result.totalPages || 0, totalElements: result.totalElements || 0 })
      } else {
        setRecords(Array.isArray(result) ? result : [])
        setPageInfo(null)
      }
    } catch (err) { setError(apiErrorMessage(err)) }
    finally { setLoading(false) }
  }, [debouncedQuery, filter, load, pagination, refreshKey, requestPage])

  useEffect(() => { reload() }, [reload])
  const shown = useMemo(() => {
    const needle = query.toLowerCase()
    const statusKey = filters[0]?.key
    return records.filter((record) => {
      const matchesQuery = pagination || !needle || Object.values(record).some((item) => String(item ?? '').toLowerCase().includes(needle))
      const matchesFilter = pagination || !filter || !statusKey || String(record[statusKey]) === filter
      return matchesQuery && matchesFilter
    })
  }, [filter, filters, pagination, query, records])
  const pageCount = pagination ? Math.max(pageInfo?.totalPages || 0, 1) : Math.max(Math.ceil(shown.length / PAGE_SIZE), 1)
  const pageRecords = pagination ? shown : shown.slice(page * PAGE_SIZE, (page + 1) * PAGE_SIZE)
  useEffect(() => {
    if (!pagination && page >= pageCount && page > 0) setPage(pageCount - 1)
  }, [page, pageCount, pagination])

  const save = async (payload) => {
    setBusy(true)
    try {
      if (modal?.record) await update(modal.record.id, payload)
      else await create(payload)
      notify(modal?.record ? 'Record updated.' : 'Record created.')
      setModal(null)
      await reload()
    } catch (err) { notify(apiErrorMessage(err), 'error') }
    finally { setBusy(false) }
  }

  const deleteRecord = async () => {
    setBusy(true)
    try {
      await remove(removeTarget.id)
      notify('Record deleted.')
      setRemoveTarget(null)
      await reload()
    } catch (err) { notify(apiErrorMessage(err), 'error') }
    finally { setBusy(false) }
  }

  return <>
    <PageHeader eyebrow={eyebrow} title={title} description={description} action={canCreate && <button className="button button-primary" onClick={() => setModal({ record: null })}><Plus size={17} />{actionLabel}</button>} />
    <section className="content-card">
      <div className="table-toolbar">
        {searchable ? <SearchBox value={query} onChange={(value) => { setQuery(value); setPage(0) }} placeholder={searchPlaceholder || 'Search records…'} /> : <div />}
        <div className="toolbar-filters">{filters.map((item) => <select aria-label={item.label} key={item.key} value={filter} onChange={(event) => { setFilter(event.target.value); setPage(0) }}><option value="">{item.label}</option>{item.options.map((option) => <option value={option.value} key={option.value}>{option.label}</option>)}</select>)}</div>
      </div>
      {loading ? <LoadingState label={`Loading ${title.toLowerCase()}…`} /> : error ? <ErrorState message={error} onRetry={reload} /> : shown.length === 0 ? <EmptyState title={query || filter ? 'No matching records' : `No ${title.toLowerCase()} yet`} description={query || filter ? 'Try adjusting your search or filters.' : 'Create your first record to get started.'} /> : <>
        <div className="table-scroll"><table><thead><tr>{columns.map((column) => <th key={column.key}>{column.label}</th>)}{(canUpdate || canRemove || actions) && <th className="actions-head">Actions</th>}</tr></thead><tbody>
          {pageRecords.map((record) => <tr key={record.id ?? record.memberId}>{columns.map((column) => <td key={column.key}>{formatCell(record[column.key], column, record)}</td>)}{(canUpdate || canRemove || actions) && <td><div className="row-actions">{actions?.(record)}{canUpdate && <button className="icon-button" aria-label="Edit record" onClick={() => setModal({ record })}><Pencil size={16} /></button>}{canRemove && <button className="icon-button danger-hover" aria-label="Delete record" onClick={() => setRemoveTarget(record)}><Trash2 size={16} /></button>}</div></td>}</tr>)}
        </tbody></table></div>
        {(pagination || shown.length > PAGE_SIZE) && <div className="pagination"><span>{pagination ? pageInfo?.totalElements || 0 : shown.length} records · page {page + 1} of {pageCount}</span><div><button className="icon-button" disabled={page <= 0} onClick={() => setPage((current) => current - 1)} aria-label="Previous page"><ChevronLeft size={18} /></button><button className="icon-button" disabled={page + 1 >= pageCount} onClick={() => setPage((current) => current + 1)} aria-label="Next page"><ChevronRight size={18} /></button></div></div>}
      </>}
    </section>
    {modal && <RecordForm title={modal.record ? `Edit ${title.toLowerCase().replace(/s$/, '')}` : actionLabel} fields={fields} value={modal.record || initialValues} onCancel={() => setModal(null)} onSubmit={save} busy={busy} />}
    {removeTarget && <ConfirmDialog title="Delete this record?" message="This action cannot be undone. Confirm only if you are sure." onCancel={() => setRemoveTarget(null)} onConfirm={deleteRecord} busy={busy} />}
  </>
}
