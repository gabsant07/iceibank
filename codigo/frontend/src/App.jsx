import { useMemo, useState } from "react";
import Sidebar from "./components/Sidebar";
import Toast from "./components/Toast";
import { useAuth } from "./context/AuthContext";
import LoginPage from "./pages/LoginPage";
import OverviewPage from "./pages/OverviewPage";
import AccountsPage from "./pages/AccountsPage";
import MovementPage from "./pages/MovementPage";
import TransferPage from "./pages/TransferPage";
import HistoryPage from "./pages/HistoryPage";
import TimelinePage from "./pages/TimelinePage";
import { createBankApi } from "./services/api";

export default function App() {
  const { session, logout } = useAuth();
  const [active, setActive] = useState("overview");
  const [toast, setToast] = useState(null);
  const api = useMemo(() => session ? createBankApi(session) : null, [session]);

  if (!session) return <LoginPage />;

  function notify(message, type = "success") {
    setToast({ message, type });
    window.setTimeout(() => setToast(null), 4500);
  }

  const pages = {
    overview: <OverviewPage api={api} session={session} onNavigate={setActive} />,
    accounts: <AccountsPage api={api} session={session} notify={notify} />,
    movement: <MovementPage api={api} notify={notify} />,
    transfer: <TransferPage api={api} session={session} notify={notify} />,
    history: <HistoryPage api={api} notify={notify} />,
    timeline: <TimelinePage api={api} notify={notify} />
  };

  return (
    <div className="app-shell">
      <Sidebar active={active} onChange={setActive} session={session} onLogout={logout} />
      <main className="main-area">{pages[active]}</main>
      <Toast toast={toast} onClose={() => setToast(null)} />
    </div>
  );
}
