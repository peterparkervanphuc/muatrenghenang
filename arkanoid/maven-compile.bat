@echo off
echo ================================================
echo Maven Test Compilation Script
echo ================================================

REM Check if Maven is available
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven not found in PATH
    echo.
    echo Please ensure Maven is installed and added to PATH.
    echo Or use IntelliJ's built-in Maven:
    echo   1. Open Maven tool window ^(View -^> Tool Windows -^> Maven^)
    echo   2. Click Reload All Maven Projects
    echo   3. Run: Lifecycle -^> compile
    pause
    exit /b 1
)

echo Maven found! Running compile...
echo.

cd /d "%~dp0"
call mvn clean compile

echo.
echo ================================================
echo Compilation complete!
echo ================================================
pause

