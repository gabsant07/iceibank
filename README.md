# ICEIBank

Projeto da Sprint 1 de Sistemas Distribuídos. Uma API REST + Relógio lógico de Lamport + Frontend + Auth JWT

## Tecnologias

- Java 21
- Spring Boot 3.5
- Maven
- Spring Web, Data JPA e Security
- H2
- JWT
- React 19 e Vite 8 no frontend separado

As instruções completas de execução e teste estão em `docs/EXECUCAO.md`, e o mapeamento da entrega está em `docs/CHECKLIST-SPRINT1.md`. O frontend separado está em `frontend/`.

## Arquitetura

Não existe servidor bancário central. O mesmo JAR roda nas portas 8080, 8081 e 8082, com um H2 próprio em cada processo. A agência dona da conta é calculada com `floorMod(accountNumber, 3)`. Transferências remotas são chamadas diretamente da agência de origem para a agência de destino.

## Principais rotas

| Método | Rota | Operação |
| --- | --- | --- |
| POST | `/api/auth/login` | Gerar JWT |
| POST | `/api/accounts` | Criar conta local |
| GET | `/api/accounts/{number}` | Consultar saldo |
| POST | `/api/accounts/{number}/deposits` | Depositar |
| POST | `/api/accounts/{number}/withdrawals` | Sacar |
| GET | `/api/accounts/{number}/history` | Histórico adicional |
| POST | `/api/transfers` | Transferência local ou remota |
| GET | `/api/timeline` | Linha do tempo de Lamport |

Todas essas rotas, exceto o login, exigem JWT. O console H2 continua público. O endpoint interno de crédito remoto é autenticado por uma chave entre agências e é idempotente pelo identificador da transação. A falha depois do débito e antes da confirmação do destino permanece registrada como inconsistente, propositalmente, para demonstrar a limitação tratada em uma sprint futura com 2PC ou Saga.

## Executar o projeto completo

Com Java 21, Maven e Node.js 20.19 ou superior instalados, execute no PowerShell:

```powershell
.\scripts\start-complete-project.ps1
```

O script gera o backend, abre as três agências e inicia o frontend em `http://localhost:5173`.

O projeto cria automaticamente duas contas por agência para testes. Consulte a tabela de contas e logins em `docs/EXECUCAO.md`.

Para validar automaticamente os itens técnicos e gerar as evidências reais em `evidencias/sprint1`, execute `scripts/validar-sprint1.ps1` depois de iniciar as agências.
