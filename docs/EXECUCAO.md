# Execução do ICEIBank

## Requisitos

- Java 21
- Maven 3.9+ ou o Maven Wrapper incluído
- Node.js 20.19 ou superior para o frontend React

## Iniciar as três agências no PowerShell

Abra três terminais na raiz do projeto.

```powershell
$env:AGENCY_ID="0"; $env:SERVER_PORT="8080"; .\mvnw.cmd spring-boot:run
```

```powershell
$env:AGENCY_ID="1"; $env:SERVER_PORT="8081"; .\mvnw.cmd spring-boot:run
```

```powershell
$env:AGENCY_ID="2"; $env:SERVER_PORT="8082"; .\mvnw.cmd spring-boot:run
```

O frontend estará disponível em qualquer instância, por exemplo `http://localhost:8080`.

Como alternativa, gere o JAR uma vez e inicie as três agências automaticamente:

```powershell
.\scripts\start-agencies.ps1
```

## Iniciar o frontend React

Com as três agências abertas, use outro terminal:

```powershell
cd frontend
npm install
npm run dev
```

Acesse `http://localhost:5173`. Para iniciar backend e frontend com um único comando, use:

```powershell
.\scripts\start-complete-project.ps1
```

## Login inicial

- Usuário: `admin`
- Senha: `admin123`

Também existe um usuário de teste em cada agência:

| Agência | Usuário | Senha | Conta vinculada |
| --- | --- | --- | --- |
| 0 | `cliente0` | `123456` | 3 |
| 1 | `cliente1` | `123456` | 4 |
| 2 | `cliente2` | `123456` | 5 |

## Contas criadas automaticamente

| Agência | Conta | Titular | Saldo inicial |
| --- | ---: | --- | ---: |
| 0 | 3 | Ana Souza | R$ 1.500,00 |
| 0 | 6 | Bruno Lima | R$ 800,00 |
| 1 | 4 | Carla Mendes | R$ 1.250,00 |
| 1 | 7 | Daniel Rocha | R$ 600,00 |
| 2 | 5 | Elisa Martins | R$ 2.000,00 |
| 2 | 8 | Felipe Costa | R$ 450,00 |

As contas são cadastradas pelo `DataInitializer`, pois ele identifica o `AGENCY_ID` da instância e insere somente as contas pertencentes à agência correta.

## Console H2

- URL: `http://localhost:8080/h2-console`
- JDBC URL da agência 0: `jdbc:h2:file:./data/iceibank-agency-0`
- Usuário: `sa`
- Senha: vazia

Troque a porta e o final da JDBC URL para acessar as demais agências.

## Exemplos

Uma conta pertence à agência calculada por `accountNumber % 3`.

```http
POST /api/accounts
Content-Type: application/json

{"accountNumber":30,"holderName":"Nova Conta","initialBalance":500}
```

```http
POST /api/transfers
Content-Type: application/json

{"sourceAccount":3,"destinationAccount":4,"amount":100}
```

O histórico adicional pode ser consultado em `GET /api/accounts/3/history`, e a linha do tempo unificada em `GET /api/timeline`.

## Mesclar os logs

Depois de gerar operações nas três agências, execute:

```powershell
node scripts/mesclar-logs.js data
```

O utilitário Java equivalente (`scripts/MergeLogs.java`) foi mantido como alternativa.

## Validar o checklist técnico

Com as três agências iniciadas por `start-agencies.ps1`, execute:

```powershell
.\scripts\validar-sprint1.ps1
```

O script comprova o particionamento, JWT sem token e com token válido, depósito, saque, transferência local, transferência remota, relógio de Lamport e a falha conhecida. Ele interrompe e reinicia a Agência 2 para reproduzir a inconsistência e também executa os testes automatizados, incluindo o cenário de token expirado. Os resultados reais e datados são gravados em `evidencias/sprint1`.

Para encerrar os processos iniciados pelo script:

```powershell
.\scripts\stop-agencies.ps1
```

## Segurança

O login e o console H2 são públicos. As rotas bancárias exigem `Authorization: Bearer <token>` e retornam 401 quando o token está ausente, inválido, expirado ou pertence a outra agência. O endpoint de crédito entre agências usa `X-Agency-Key`, preenchido automaticamente pelo backend a partir de `INTERNAL_API_KEY`.
