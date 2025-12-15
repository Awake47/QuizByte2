@echo off
echo ========================================
echo QuizByte - Push to GitHub
echo ========================================
echo.

REM Try to find Git
set GIT_PATH=
if exist "C:\Program Files\Git\bin\git.exe" (
    set GIT_PATH=C:\Program Files\Git\bin\git.exe
) else if exist "C:\Program Files (x86)\Git\bin\git.exe" (
    set GIT_PATH=C:\Program Files (x86)\Git\bin\git.exe
) else (
    where git >nul 2>&1
    if %ERRORLEVEL% == 0 (
        set GIT_PATH=git
    )
)

if "%GIT_PATH%"=="" (
    echo ERROR: Git not found!
    pause
    exit /b 1
)

echo Enter your GitHub repository URL:
echo Example: https://github.com/YOUR_USERNAME/quizbyte.git
echo.
set /p REPO_URL="Repository URL: "

if "%REPO_URL%"=="" (
    echo ERROR: Repository URL is required!
    pause
    exit /b 1
)

echo.
echo [1/3] Adding remote repository...
call "%GIT_PATH%" remote add origin "%REPO_URL%"
if %ERRORLEVEL% neq 0 (
    echo Warning: Remote might already exist. Continuing...
    call "%GIT_PATH%" remote set-url origin "%REPO_URL%"
)

echo [2/3] Setting branch to main...
call "%GIT_PATH%" branch -M main

echo [3/3] Pushing to GitHub...
echo.
echo NOTE: You will be asked for GitHub credentials.
echo Use your GitHub username and Personal Access Token (not password).
echo.
call "%GIT_PATH%" push -u origin main

if %ERRORLEVEL% == 0 (
    echo.
    echo ========================================
    echo SUCCESS! Code pushed to GitHub!
    echo ========================================
) else (
    echo.
    echo ========================================
    echo ERROR: Push failed!
    echo ========================================
    echo.
    echo Common issues:
    echo - Wrong repository URL
    echo - Need to authenticate (use Personal Access Token)
    echo - Repository doesn't exist on GitHub
    echo.
    echo To create Personal Access Token:
    echo 1. Go to GitHub.com
    echo 2. Settings ^> Developer settings ^> Personal access tokens
    echo 3. Generate new token (classic)
    echo 4. Select "repo" scope
    echo 5. Use token as password when pushing
)

pause

