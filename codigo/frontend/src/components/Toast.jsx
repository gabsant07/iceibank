export default function Toast({ toast, onClose }) {
  if (!toast) return null;
  return (
    <div className={`toast ${toast.type}`} role="status">
      <span>{toast.message}</span>
      <button onClick={onClose} aria-label="Fechar">×</button>
    </div>
  );
}
