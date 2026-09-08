const items = [
  ["overview", "Visão geral"],
  ["accounts", "Contas"],
  ["movement", "Depósito e saque"],
  ["transfer", "Transferências"],
  ["history", "Histórico"],
  ["timeline", "Linha do tempo"]
];

export default function Sidebar({ active, onChange, session, onLogout }) {
  return (
    <aside className="sidebar">
      <div className="brand">
        <span className="brand-mark">I</span>
        <div><strong>ICEIBank</strong><small>Banco distribuído</small></div>
      </div>
      <nav>
        {items.map(([id, label]) => (
          <button key={id} className={active === id ? "active" : ""} onClick={() => onChange(id)}>
            {label}
          </button>
        ))}
      </nav>
      <div className="agency-card">
        <small>Conectado em</small>
        <strong>{session.label} · {session.name}</strong>
        <span>{session.baseUrl}</span>
      </div>
      <button className="logout" onClick={onLogout}>Sair</button>
    </aside>
  );
}
