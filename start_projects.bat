@echo off
echo ===============================================
echo       SpringAI Project Startup Script
echo ===============================================

echo.
echo 1. Starting Backend Server...
echo.
cd /d "D:\Belajar\Java\BelajarAI"

REM Check if port 8081 is in use and kill process if necessary
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8081') do (
    echo Killing process on port 8081: %%a
    taskkill /PID %%a /F >nul 2>&1
)

echo Starting Spring Boot backend...
start "Backend Server" cmd /k "mvn spring-boot:run"

echo Waiting for backend to start...
timeout /t 10

echo.
echo 2. Backend should be starting on http://localhost:8081
echo.

echo 3. Now you can run the Android app:
echo    - Open Android Studio
echo    - Open project: D:\Android Project\SpringAI  
echo    - Connect device/emulator
echo    - Click Run
echo.

echo 4. Or build APK manually:
cd /d "D:\Android Project\SpringAI"
echo Building Android APK...
call gradlew assembleDebug -x lint

echo.
echo ===============================================
echo Setup complete! 
echo Backend: http://localhost:8081
echo Android APK: app\build\outputs\apk\debug\app-debug.apk
echo ===============================================
pause
