import { useState } from "react";
import { agencies } from "../config/agencies";
import { useAuth } from "../context/AuthContext";

export default function LoginPage() {
  const { login } = useAuth();
  const [agencyId, setAgencyId] = useState(0);
  const [username, setUsername] = useState("admin");
  const [password, setPassword] = useState("admin123");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function submit(event) {
    event.preventDefault();
    setError("");
    setLoading(true);
    try {
      await login(agencies[agencyId], username, password);
    } catch (exception) {
      setError(exception.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="login-page">
      <section className="login-panel">
        <div className="login-copy">
          <div className="brand large"><span className="brand-mark">I</span><strong>ICEIBank</strong></div>
          <h1>Suas operações bancárias em uma rede distribuída.</h1>
          <p>Escolha a agência responsável pelo atendimento e acesse o ambiente seguro do banco.</p>
          <div className="network-preview">
            {agencies.map(agency => <span key={agency.id}>A{agency.id}</span>)}
            <div className="network-line" />
          </div>
        </div>
        <form className="login-form" onSubmit={submit}>
          <div><small>ACESSO AO SISTEMA</small><h2>Entre na sua conta</h2></div>
          <label className="field">
            <span>Agência</span>
            <select value={agencyId} onChange={event => setAgencyId(Number(event.target.value))}>
              {agencies.map(agency => (
                <option key={agency.id} value={agency.id}>{agency.label} · {agency.name}</option>
              ))}
            </select>
          </label>
          <label className="field"><span>Usuário</span><input value={username} onChange={e => setUsername(e.target.value)} /></label>
          <label className="field"><span>Senha</span><input type="password" value={password} onChange={e => setPassword(e.target.value)} /></label>
          {error && <div className="form-error">{error}</div>}
          <button className="primary wide" disabled={loading}>{loading ? "Conectando..." : "Entrar"}</button>
          <p className="login-help">Acesso inicial: <strong>admin</strong> / <strong>admin123</strong></p>
        </form>
      </section>
    </main>
  );
}
