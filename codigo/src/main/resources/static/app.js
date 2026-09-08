let token = localStorage.getItem("iceibank-token") || "";

const byId = id => document.getElementById(id);
const baseUrl = () => byId("agency").value;

async function request(path, options = {}) {
    const headers = { "Content-Type": "application/json", ...(options.headers || {}) };
    if (token) headers.Authorization = `Bearer ${token}`;
    const response = await fetch(baseUrl() + path, { ...options, headers });
    const body = await response.json().catch(() => ({}));
    byId("result").textContent = JSON.stringify(body, null, 2);
    if (!response.ok) throw new Error(body.message || `HTTP ${response.status}`);
    return body;
}

byId("login").onclick = async () => {
    const body = await request("/api/auth/login", {
        method: "POST",
        body: JSON.stringify({ username: byId("username").value, password: byId("password").value })
    });
    token = body.token;
    localStorage.setItem("iceibank-token", token);
};

byId("createAccount").onclick = () => request("/api/accounts", {
    method: "POST",
    body: JSON.stringify({
        accountNumber: Number(byId("accountNumber").value),
        holderName: byId("holderName").value,
        initialBalance: Number(byId("amount").value || 0)
    })
});

byId("findAccount").onclick = () => request(`/api/accounts/${byId("accountNumber").value}`);
byId("history").onclick = () => request(`/api/accounts/${byId("accountNumber").value}/history`);
byId("deposit").onclick = () => accountMovement("deposits");
byId("withdraw").onclick = () => accountMovement("withdrawals");

function accountMovement(operation) {
    return request(`/api/accounts/${byId("accountNumber").value}/${operation}`, {
        method: "POST",
        body: JSON.stringify({ amount: Number(byId("amount").value) })
    });
}

byId("transfer").onclick = () => request("/api/transfers", {
    method: "POST",
    body: JSON.stringify({
        sourceAccount: Number(byId("sourceAccount").value),
        destinationAccount: Number(byId("destinationAccount").value),
        amount: Number(byId("transferAmount").value)
    })
});

window.addEventListener("unhandledrejection", event => {
    byId("result").textContent = event.reason.message;
});
