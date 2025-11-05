@echo off
REM =====================================
REM   ARKANOID GAME - RUN SCRIPT
REM   Java OOP Project - v2.1
REM   Updated: November 6, 2025
REM =====================================

cd /d "%~dp0"

echo.
echo =====================================
echo      ARKANOID GAME - JAVA OOP
echo =====================================
echo.
echo [INFO] Project Features:
echo   - 18 Levels
echo   - Combo System (x2, x3, x4, x5)
echo   - 14 Brick Types
echo   - 7 Power-ups
echo   - Save/Load Game
echo   - 60 FPS Gameplay
echo.
echo =====================================
echo.

REM Check if Java is installed
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java not found!
    echo.
    echo Please install Java JDK 17 or higher.
    echo Download: https://www.oracle.com/java/technologies/downloads/
    echo.
    pause
    exit /b 1
)

echo [1/3] Checking Java version...
java -version 2>&1 | findstr /i "version" >nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Could not determine Java version
    pause
    exit /b 1
)
echo [OK] Java found!
echo.

echo [2/3] Compiling Java files...

REM Create bin directory if not exists
if not exist "bin" (
    mkdir bin
    echo [INFO] Created bin/ directory
)

REM Copy config.properties to bin
copy /Y config.properties bin\ >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [WARNING] Could not copy config.properties
) else (
    echo [OK] Config copied to bin/
)

REM Compile all Java files with correct package structure
javac -d bin -sourcepath src -encoding UTF-8 -Xlint:none src\main\ArkanoidGame.java

if errorlevel 1 (
    echo.
    echo [ERROR] Compilation failed!
    echo.
    echo Possible solutions:
    echo   1. Check if all source files are present in src/
    echo   2. Ensure Java JDK (not JRE) is installed
    echo   3. Try using IntelliJ IDEA or Maven instead
    echo.
    pause
    exit /b 1
)

echo [OK] Compilation successful!
echo.

echo [3/3] Starting game...
echo.
echo =====================================
echo   CONTROLS:
echo   Arrow Keys / A D - Move Paddle
echo   Space            - Launch Ball / Fire Laser
echo   F5               - Quick Save
echo   F9               - Quick Load
echo   F6               - Save/Load Menu
echo   ESC              - Pause/Menu
echo =====================================
echo.

REM Run the game with assets in classpath
java -cp "bin;assets" main.ArkanoidGame

if errorlevel 1 (
    echo.
    echo [ERROR] Game crashed or closed unexpectedly
    echo.
    echo Check arkanoid.log for details
    echo.
    pause
    exit /b 1
)

echo.
echo =====================================
echo   Thanks for playing!
echo =====================================
echo.
pause

