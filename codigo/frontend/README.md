# ICEIBank Frontend

Frontend React que consome as APIs das três instâncias do backend Spring Boot.

## Executar

```powershell
npm install
npm run dev
```

Acesse `http://localhost:5173`.

Antes de abrir o frontend, inicie as três agências do backend. As URLs padrão são:

- Agência 0 - Vila Mariana: `http://localhost:8080`
- Agência 1 - Paulista: `http://localhost:8081`
- Agência 2 - Perdizes: `http://localhost:8082`

Login inicial: `admin` / `admin123`.

Usuários de teste: `cliente0`, `cliente1` e `cliente2`, todos com a senha `123456`. Use cada usuário na agência de mesmo número.

## Funcionalidades

- seleção e login em uma das três agências;
- armazenamento e envio do JWT;
- painel com contas, saldo total e relógio de Lamport;
- criação e consulta de contas;
- depósito e saque;
- transferência local ou entre agências;
- histórico de transações;
- linha do tempo unificada de Lamport;
- mensagens claras de sucesso e erro.
