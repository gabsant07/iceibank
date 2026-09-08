# Checklist técnico da Sprint 1

| Item | Implementação | Como verificar |
| --- | --- | --- |
| Git incremental | Histórico com commits separados por estrutura, backend, frontend, dados e correções | `git log --oneline` |
| Três agências | Um JAR, três processos, `AGENCY_ID` 0–2, portas 8080–8082 e H2 separado | `scripts/start-agencies.ps1` |
| CRUD e movimentações | Criação, consulta, listagem, depósito e saque em controllers REST | Testes e `scripts/validar-sprint1.ps1` |
| Particionamento | `floorMod(accountNumber, 3)` e recusa de conta pertencente a outra agência | `AgencyRoutingServiceTest` |
| Lamport | Evento local, envio e recebimento implementados e persistidos em JSONL | `LamportClockServiceTest` e timeline |
| Transferências | Local transacional; remota via REST direto entre agências | Evidências 04 e 05 |
| Falha conhecida | Débito permanece e transação fica `INCONSISTENT` quando o destino cai | Evidência 06 |
| Mesclagem de logs | Ordenação por timestamp de Lamport, agência e relógio físico | `node scripts/mesclar-logs.js data` |
| JWT | Login público; APIs protegidas; testes sem token, válido e expirado | `JwtSecurityTest` |
| H2 | Console liberado em `/h2-console` | `JwtSecurityTest` e acesso pelo navegador |
| Frontend | Login, contas, saldo, depósito, saque, transferências, histórico e timeline | `frontend/` |
| Funcionalidade adicional | Histórico por conta | `/api/accounts/{number}/history` |
| Evidências | Script gera arquivos datados com respostas reais e saída dos testes | `evidencias/sprint1/` |

As respostas finais e o vídeo não fazem parte desta atualização, conforme solicitado.
