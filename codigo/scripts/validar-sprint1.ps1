$ErrorActionPreference = "Stop"
$project = Split-Path -Parent $PSScriptRoot
$evidence = Join-Path $project "evidencias\sprint1"
$runtime = Join-Path $project ".runtime"
New-Item -ItemType Directory -Force -Path $evidence | Out-Null

function Save-Evidence($name, $title, $value) {
    $path = Join-Path $evidence $name
    @(
        "ICEIBank - Sprint 1"
        $title
        "Generated at: $([DateTime]::UtcNow.ToString('yyyy-MM-ddTHH:mm:ssZ'))"
        ""
        ($value | ConvertTo-Json -Depth 12)
    ) | Set-Content -Path $path -Encoding UTF8
    Write-Host "OK: $title"
}

function Login($port) {
    Invoke-RestMethod -Method Post -Uri "http://localhost:$port/api/auth/login" `
        -ContentType "application/json" -Body '{"username":"admin","password":"admin123"}'
}

function Headers($token) {
    @{ Authorization = "Bearer $token" }
}

function Wait-Agency($port) {
    for ($attempt = 0; $attempt -lt 30; $attempt++) {
        try {
            Login $port | Out-Null
            return
        } catch {
            Start-Sleep -Seconds 1
        }
    }
    throw "Agency on port $port did not start"
}

8080..8082 | ForEach-Object { Wait-Agency $_ }
$login0 = Login 8080
$login1 = Login 8081
$login2 = Login 8082
$headers0 = Headers $login0.token
$headers1 = Headers $login1.token
$headers2 = Headers $login2.token

try {
    Invoke-RestMethod -Uri "http://localhost:8080/api/accounts" | Out-Null
    throw "Route accepted a request without JWT"
} catch {
    $status = [int]$_.Exception.Response.StatusCode
    if ($status -ne 401) { throw }
    Save-Evidence "01-jwt-sem-token.txt" "JWT without token returns 401" @{ status = $status }
}

$accounts = @{
    agency0 = Invoke-RestMethod -Uri "http://localhost:8080/api/accounts" -Headers $headers0
    agency1 = Invoke-RestMethod -Uri "http://localhost:8081/api/accounts" -Headers $headers1
    agency2 = Invoke-RestMethod -Uri "http://localhost:8082/api/accounts" -Headers $headers2
}
Save-Evidence "02-tres-agencias.txt" "Three agencies and their account partitions" $accounts

$deposit = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/accounts/3/deposits" `
    -Headers $headers0 -ContentType "application/json" -Body '{"amount":10}'
$withdrawal = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/accounts/3/withdrawals" `
    -Headers $headers0 -ContentType "application/json" -Body '{"amount":5}'
Save-Evidence "03-deposito-saque.txt" "Authenticated deposit and withdrawal" @{
    deposit = $deposit; withdrawal = $withdrawal
}

$local = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/transfers" `
    -Headers $headers0 -ContentType "application/json" `
    -Body '{"sourceAccount":3,"destinationAccount":6,"amount":2}'
Save-Evidence "04-transferencia-local.txt" "Local transfer within agency 0" $local

$remote = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/transfers" `
    -Headers $headers0 -ContentType "application/json" `
    -Body '{"sourceAccount":3,"destinationAccount":4,"amount":3}'
$destination = Invoke-RestMethod -Uri "http://localhost:8081/api/accounts/4" -Headers $headers1
Save-Evidence "05-transferencia-remota.txt" "Remote transfer from agency 0 to agency 1" @{
    transfer = $remote; destination = $destination
}

$agency2PidFile = Join-Path $runtime "agency-2.pid"
if (Test-Path $agency2PidFile) {
    $agency2Pid = [int](Get-Content $agency2PidFile)
    & taskkill /PID $agency2Pid /T /F 2>$null | Out-Null
    Remove-Item $agency2PidFile -Force
    Start-Sleep -Seconds 2
    try {
        Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/transfers" `
            -Headers $headers0 -ContentType "application/json" `
            -Body '{"sourceAccount":3,"destinationAccount":5,"amount":1}' | Out-Null
        throw "Known failure was not reproduced"
    } catch {
        $status = [int]$_.Exception.Response.StatusCode
        if ($status -ne 502) { throw }
        $history = Invoke-RestMethod -Uri "http://localhost:8080/api/accounts/3/history" -Headers $headers0
        Save-Evidence "06-falha-conhecida.txt" "Known failure: debit retained when agency 2 is down" @{
            status = $status; latestSourceHistory = $history[0]
        }
    } finally {
        $process = Start-Process powershell -PassThru -ArgumentList "-NoExit", "-File", (Join-Path $PSScriptRoot "run-agency.ps1"), "-AgencyId", 2, "-Port", 8082
        Set-Content -Path $agency2PidFile -Value $process.Id
        Wait-Agency 8082
    }
} else {
    Save-Evidence "06-falha-conhecida.txt" "Known failure not executed" @{
        instruction = "Start agencies with scripts/start-agencies.ps1 and run this validation again."
    }
}

$timeline = Invoke-RestMethod -Uri "http://localhost:8080/api/timeline" -Headers $headers0
Save-Evidence "07-lamport-timeline.txt" "Unified Lamport timeline" $timeline

node (Join-Path $PSScriptRoot "mesclar-logs.js") (Join-Path $project "data") |
    Set-Content -Path (Join-Path $evidence "08-logs-mesclados.txt") -Encoding UTF8

$maven = Join-Path $project "mvnw.cmd"
$testEvidence = Join-Path $evidence "09-testes-automatizados.txt"

& cmd.exe /d /c "`"$maven`" test > `"$testEvidence`" 2>&1"
$mavenExitCode = $LASTEXITCODE

if ($mavenExitCode -ne 0) {
    throw "Automated tests failed. Check $testEvidence"
}

Write-Host "OK: Automated tests"

Write-Host "Validation completed. Evidence is in evidencias/sprint1."
