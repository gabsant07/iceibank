$project = Split-Path -Parent $PSScriptRoot
$runtime = Join-Path $project ".runtime"

0..2 | ForEach-Object {
    $pidFile = Join-Path $runtime "agency-$_.pid"
    if (Test-Path $pidFile) {
        $processId = [int](Get-Content $pidFile)
        & taskkill /PID $processId /T /F 2>$null | Out-Null
        Remove-Item $pidFile -Force
    }
}

Write-Host "Agency processes stopped."
