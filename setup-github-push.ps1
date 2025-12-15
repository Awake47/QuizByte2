# QuizByte - Push to GitHub (PowerShell)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "QuizByte - Push to GitHub" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Refresh PATH
$env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")

# Find Git
$gitPath = $null
$possiblePaths = @(
    "C:\Program Files\Git\bin\git.exe",
    "C:\Program Files (x86)\Git\bin\git.exe",
    "git"
)

foreach ($path in $possiblePaths) {
    if ($path -eq "git") {
        try {
            $null = Get-Command git -ErrorAction Stop
            $gitPath = "git"
            break
        } catch {
            continue
        }
    } else {
        if (Test-Path $path) {
            $gitPath = $path
            break
        }
    }
}

if (-not $gitPath) {
    Write-Host "ERROR: Git not found!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "Enter your GitHub repository URL:" -ForegroundColor Yellow
Write-Host "Example: https://github.com/YOUR_USERNAME/quizbyte.git" -ForegroundColor Gray
Write-Host ""
$repoUrl = Read-Host "Repository URL"

if ([string]::IsNullOrWhiteSpace($repoUrl)) {
    Write-Host "ERROR: Repository URL is required!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "[1/3] Adding remote repository..." -ForegroundColor Yellow
& $gitPath remote add origin $repoUrl 2>&1 | Out-Null
if ($LASTEXITCODE -ne 0) {
    Write-Host "Remote might already exist, updating..." -ForegroundColor Yellow
    & $gitPath remote set-url origin $repoUrl
}

Write-Host "[2/3] Setting branch to main..." -ForegroundColor Yellow
& $gitPath branch -M main

Write-Host "[3/3] Pushing to GitHub..." -ForegroundColor Yellow
Write-Host ""
Write-Host "NOTE: You will be asked for GitHub credentials." -ForegroundColor Cyan
Write-Host "Use your GitHub username and Personal Access Token (not password)." -ForegroundColor Cyan
Write-Host ""
& $gitPath push -u origin main

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "SUCCESS! Code pushed to GitHub!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "ERROR: Push failed!" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Common issues:" -ForegroundColor Yellow
    Write-Host "- Wrong repository URL" -ForegroundColor White
    Write-Host "- Need to authenticate (use Personal Access Token)" -ForegroundColor White
    Write-Host "- Repository doesn't exist on GitHub" -ForegroundColor White
}

Read-Host "Press Enter to exit"

