import { useState } from "react";
import Field from "../components/Field";

export default function MovementPage({ api, notify }) {
  const [operation, setOperation] = useState("deposit");
  const [accountNumber, setAccountNumber] = useState("");
  const [amount, setAmount] = useState("");
  const [result, setResult] = useState(null);

  async function submit(event) {
    event.preventDefault();
    try {
      const response = operation === "deposit"
        ? await api.deposit(accountNumber, Number(amount))
        : await api.withdraw(accountNumber, Number(amount));
      setResult(response);
      notify(operation === "deposit" ? "Depósito realizado." : "Saque realizado.");
    } catch (error) { notify(error.message, "error"); }
  }

  return <div className="page-content narrow">
    <div className="page-heading"><div><small>MOVIMENTAÇÃO</small><h1>Depósito e saque</h1><p>Atualize o saldo de uma conta local.</p></div></div>
    <section className="card operation-card">
      <div className="segmented"><button className={operation === "deposit" ? "active" : ""} onClick={() => setOperation("deposit")}>Depósito</button><button className={operation === "withdraw" ? "active" : ""} onClick={() => setOperation("withdraw")}>Saque</button></div>
      <form onSubmit={submit} className="form-stack"><Field label="Número da conta" type="number" required value={accountNumber} onChange={e => setAccountNumber(e.target.value)} /><Field label="Valor" type="number" min="0.01" step="0.01" required value={amount} onChange={e => setAmount(e.target.value)} /><button className="primary wide">Confirmar {operation === "deposit" ? "depósito" : "saque"}</button></form>
      {result && <div className="result-card"><small>Novo saldo</small><strong>{new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" }).format(result.balance)}</strong></div>}
    </section>
  </div>;
}
