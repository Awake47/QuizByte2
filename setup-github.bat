@echo off
echo ========================================
echo QuizByte - GitHub Setup Script
echo ========================================
echo.

REM Try to find Git in common locations
set GIT_PATH=
if exist "C:\Program Files\Git\bin\git.exe" (
    set GIT_PATH=C:\Program Files\Git\bin\git.exe
) else if exist "C:\Program Files (x86)\Git\bin\git.exe" (
    set GIT_PATH=C:\Program Files (x86)\Git\bin\git.exe
) else (
    REM Try using git from PATH (might work after restart)
    where git >nul 2>&1
    if %ERRORLEVEL% == 0 (
        set GIT_PATH=git
    )
)

if "%GIT_PATH%"=="" (
    echo ERROR: Git not found!
    echo Please make sure Git is installed and restart this terminal.
    echo Or restart Android Studio after installing Git.
    pause
    exit /b 1
)

echo Found Git at: %GIT_PATH%
echo.

REM Initialize git repository
echo [1/5] Initializing Git repository...
call "%GIT_PATH%" init
if %ERRORLEVEL% neq 0 (
    echo ERROR: Failed to initialize Git repository
    pause
    exit /b 1
)

REM Add all files
echo [2/5] Adding files to Git...
call "%GIT_PATH%" add .
if %ERRORLEVEL% neq 0 (
    echo ERROR: Failed to add files
    pause
    exit /b 1
)

REM Create initial commit
echo [3/5] Creating initial commit...
call "%GIT_PATH%" commit -m "Initial commit: QuizByte app"
if %ERRORLEVEL% neq 0 (
    echo ERROR: Failed to create commit
    pause
    exit /b 1
)

echo.
echo ========================================
echo SUCCESS! Git repository initialized.
echo ========================================
echo.
echo Next steps:
echo 1. Go to https://github.com and create a new repository
echo 2. Name it "quizbyte" (or any name you like)
echo 3. DO NOT initialize with README
echo 4. Copy the repository URL (e.g., https://github.com/YOUR_USERNAME/quizbyte.git)
echo 5. Run these commands in Git Bash or Android Studio Terminal:
echo.
echo    git remote add origin YOUR_REPO_URL
echo    git branch -M main
echo    git push -u origin main
echo.
echo Or run: setup-github-push.bat after creating the repository.
echo.
pause

