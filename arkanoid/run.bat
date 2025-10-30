@echo off
REM ================================
REM   ARKANOID GAME - RUN SCRIPT
REM ================================
REM Automatically compiles and runs the Arkanoid game
REM Works without IDE for quick testing, CI/CD, or distribution

cd /d "%~dp0"

echo ================================
echo   ARKANOID GAME
echo ================================
echo.
echo [1/2] Compiling Java files...

REM Create bin directory if not exists
if not exist "bin" mkdir bin

REM Compile all Java files except GameTest.java (requires JUnit)
cd src
for %%f in (*.java) do (
    if not "%%f"=="GameTest.java" (
        javac -d ..\bin -encoding UTF-8 "%%f" 2>nul
        if errorlevel 1 (
            echo [ERROR] Failed to compile %%f
            cd ..
            pause
            exit /b 1
        )
    )
)
cd ..

echo [OK] Compilation successful!
echo.
echo [2/2] Starting Arkanoid Game...
echo ================================
echo.

REM Run the game
java -cp bin ArkanoidGame

REM Pause only if there was an error
if errorlevel 1 (
    echo.
    echo [ERROR] Game crashed or failed to run!
    pause
)

