@echo off
REM ================================
REM   ARKANOID GAME - RUN SCRIPT
REM ================================

cd /d "%~dp0"

echo ================================
echo   ARKANOID GAME
echo ================================
echo.
echo [1/2] Compiling Java files...

REM Create bin directory if not exists
if not exist "bin" mkdir bin

REM Copy config.properties to bin
copy /Y config.properties bin\ >nul 2>&1

REM Compile all Java files with correct package structure
javac -d bin -sourcepath src -encoding UTF-8 src\main\ArkanoidGame.java

if errorlevel 1 (
    echo [ERROR] Compilation failed!
    echo.
    pause
    exit /b 1
)

echo [OK] Compilation successful!
echo.
echo [2/2] Starting Arkanoid Game...
echo ================================
echo.

REM Run the game (assets folder is in current directory)
java -cp bin;assets main.ArkanoidGame

REM Pause only if there was an error
if errorlevel 1 (
    echo.
    echo [ERROR] Game crashed or failed to run!
    echo Check arkanoid.log for details
    pause
)

