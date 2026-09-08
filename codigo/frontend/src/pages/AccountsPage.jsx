import { useEffect, useState } from "react";
import Field from "../components/Field";
import Modal from "../components/Modal";
import { agencyForAccount } from "../config/agencies";

const money = new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" });

export default function AccountsPage({ api, session, notify }) {
  const [accounts, setAccounts] = useState([]);
  const [showCreate, setShowCreate] = useState(false);
  const [form, setForm] = useState({ accountNumber: "", holderName: "", initialBalance: "0" });

  const load = async () => {
    try {
      setAccounts(await api.listAccounts());
    } catch (error) {
      notify(error.message, "error");
    }
  };

  useEffect(() => {
    let active = true;

    async function loadAccounts() {
      try {
        const data = await api.listAccounts();
        if (active) setAccounts(data);
      } catch (error) {
        if (active) notify(error.message, "error");
      }
    }

    loadAccounts();
    return () => {
      active = false;
    };
  }, [api]);

  async function create(event) {
    event.preventDefault();
    try {
      await api.createAccount({ ...form, accountNumber: Number(form.accountNumber), initialBalance: Number(form.initialBalance) });
      notify("Conta criada com sucesso.");
      setShowCreate(false);
      setForm({ accountNumber: "", holderName: "", initialBalance: "0" });
      load();
    } catch (error) {
      notify(error.message, "error");
    }
  }

  const owner = form.accountNumber ? agencyForAccount(form.accountNumber) : null;
  return (
    <div className="page-content">
      <div className="page-heading"><div><small>GESTÃO DE CONTAS</small><h1>Contas da agência</h1><p>Contas sob responsabilidade da {session.label}.</p></div><button className="primary" onClick={() => setShowCreate(true)}>+ Nova conta</button></div>
      <section className="card table-card">
        <table>
          <thead><tr><th>Conta</th><th>Titular</th><th>Agência</th><th>Saldo</th><th>Criada em</th></tr></thead>
          <tbody>
            {accounts.map(account => <tr key={account.accountNumber}><td><strong>#{account.accountNumber}</strong></td><td>{account.holderName}</td><td>Agência {account.agencyId}</td><td className="money">{money.format(account.balance)}</td><td>{new Date(account.createdAt).toLocaleDateString("pt-BR")}</td></tr>)}
            {!accounts.length && <tr><td colSpan="5" className="empty">Nenhuma conta cadastrada nesta agência.</td></tr>}
          </tbody>
        </table>
      </section>
      {showCreate && <Modal title="Cadastrar nova conta" onClose={() => setShowCreate(false)}><form onSubmit={create} className="form-stack">
        <Field label="Número da conta" type="number" min="0" required value={form.accountNumber} onChange={e => setForm({ ...form, accountNumber: e.target.value })} hint={owner ? `Esta conta pertence à ${owner.label}` : "A agência é calculada pelo número da conta módulo 3."} />
        {owner && owner.id !== session.id && <div className="form-warning">Escolha um número pertencente à Agência {session.id}.</div>}
        <Field label="Nome do titular" required value={form.holderName} onChange={e => setForm({ ...form, holderName: e.target.value })} />
        <Field label="Saldo inicial" type="number" min="0" step="0.01" required value={form.initialBalance} onChange={e => setForm({ ...form, initialBalance: e.target.value })} />
        <div className="modal-actions"><button type="button" className="secondary" onClick={() => setShowCreate(false)}>Cancelar</button><button className="primary" disabled={owner?.id !== session.id}>Criar conta</button></div>
      </form></Modal>}
    </div>
  );
}
