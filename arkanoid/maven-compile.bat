@echo off
REM =====================================
REM   ARKANOID GAME - MAVEN BUILD
REM   Updated: November 6, 2025
REM =====================================

cd /d "%~dp0"

echo.
echo =====================================
echo   MAVEN BUILD SCRIPT
echo =====================================
echo.

REM Check if Maven is available
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven not found in PATH
    echo.
    echo Solutions:
    echo   1. Install Maven: https://maven.apache.org/download.cgi
    echo   2. Add Maven to system PATH
    echo.
    echo OR use IntelliJ IDEA's built-in Maven:
    echo   1. Open Maven tool window (View -^> Tool Windows -^> Maven)
    echo   2. Click "Reload All Maven Projects"
    echo   3. Run: Lifecycle -^> compile
    echo.
    pause
    exit /b 1
)

echo [OK] Maven found!
echo.

REM Display Maven version
echo [INFO] Maven version:
call mvn -v
echo.

echo =====================================
echo   BUILDING PROJECT...
echo =====================================
echo.

REM Clean and compile
echo [1/2] Cleaning old builds...
call mvn clean

if errorlevel 1 (
    echo.
    echo [ERROR] Clean failed!
    pause
    exit /b 1
)

echo.
echo [2/2] Compiling source files...
call mvn compile

if errorlevel 1 (
    echo.
    echo [ERROR] Compilation failed!
    echo.
    echo Check for:
    echo   - Syntax errors in source code
    echo   - Missing dependencies in pom.xml
    echo   - Java version compatibility
    echo.
    pause
    exit /b 1
)

echo.
echo =====================================
echo   BUILD SUCCESSFUL!
echo =====================================
echo.
echo Compiled classes are in: target/classes/
echo.
echo To run the game:
echo   1. Use run.bat
echo   2. Or: mvn exec:java -Dexec.mainClass="main.ArkanoidGame"
echo.
pause

