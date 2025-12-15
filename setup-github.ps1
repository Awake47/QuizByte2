# QuizByte - GitHub Setup Script (PowerShell)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "QuizByte - GitHub Setup Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Try to refresh PATH
$env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")

# Try to find Git
$gitPath = $null
$possiblePaths = @(
    "C:\Program Files\Git\bin\git.exe",
    "C:\Program Files (x86)\Git\bin\git.exe",
    "git"  # Try from PATH
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
    Write-Host "Please restart Android Studio or terminal after installing Git." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Or run these commands manually in Git Bash:" -ForegroundColor Yellow
    Write-Host "  git init" -ForegroundColor White
    Write-Host "  git add ." -ForegroundColor White
    Write-Host "  git commit -m 'Initial commit: QuizByte app'" -ForegroundColor White
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "Found Git!" -ForegroundColor Green
Write-Host ""

# Initialize git repository
Write-Host "[1/3] Initializing Git repository..." -ForegroundColor Yellow
& $gitPath init
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Failed to initialize Git repository" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Add all files
Write-Host "[2/3] Adding files to Git..." -ForegroundColor Yellow
& $gitPath add .
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Failed to add files" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Create initial commit
Write-Host "[3/3] Creating initial commit..." -ForegroundColor Yellow
& $gitPath commit -m "Initial commit: QuizByte app"
if ($LASTEXITCODE -ne 0) {
    Write-Host "WARNING: Commit might have failed (maybe no changes or already committed)" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "SUCCESS! Git repository initialized." -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "1. Go to https://github.com and create a new repository" -ForegroundColor White
Write-Host "2. Name it 'quizbyte' (or any name you like)" -ForegroundColor White
Write-Host "3. DO NOT initialize with README" -ForegroundColor White
Write-Host "4. Copy the repository URL" -ForegroundColor White
Write-Host "5. Run: .\setup-github-push.ps1" -ForegroundColor White
Write-Host ""
Read-Host "Press Enter to exit"

