# SpringAI Android App with Backend API

## Project Overview
This project consists of two parts:
1. **Android App**: Located at `D:\Android Project\SpringAI` - A Jetpack Compose UI that communicates with the backend
2. **Backend API**: Located at `D:\Belajar\Java\BelajarAI` - A Spring Boot application that provides AI chat functionality

## Issues Fixed

### Android Project Issues:
- ✅ Fixed duplicate dependencies in `build.gradle.kts`
- ✅ Enabled Compose properly with correct plugin configuration
- ✅ Fixed Kotlin compilation errors in Theme.kt
- ✅ Removed incorrect import for `androidx.wear.compose.foundation.weight`
- ✅ Added proper network libraries (Retrofit + OkHttp for better HTTP handling)
- ✅ Fixed Compose configuration issues

### Backend Project Issues:
- ✅ Added missing Jackson dependency for JSON processing
- ✅ Added CORS configuration to allow Android app access
- ✅ Improved security by supporting environment variables for API key
- ✅ Fixed compilation and dependency issues

## Prerequisites

### For Android Development:
- Android Studio (latest version)
- Android SDK 24+ 
- JDK 11+
- Gradle 8.13+

### For Backend Development:
- JDK 17+ (currently using Java 21)
- Maven 3.6+
- OpenRouter API Key (for AI functionality)

## Setup Instructions

### 1. Backend Setup

1. **Navigate to backend directory:**
   ```bash
   cd "D:\Belajar\Java\BelajarAI"
   ```

2. **Set environment variable (optional but recommended):**
   ```bash
   # For Windows PowerShell
   $env:OPENROUTER_API_KEY="your-actual-api-key-here"
   
   # For Windows Command Prompt
   set OPENROUTER_API_KEY=your-actual-api-key-here
   ```
   
   If you don't set this, the app will use the default key in `application.properties`.

3. **Build and run:**
   ```bash
   mvn clean compile
   mvn spring-boot:run
   ```

4. **Verify backend is running:**
   The server should start on `http://localhost:8081`
   You should see: "Started SpringAiApplication in X seconds"

### 2. Android App Setup

1. **Navigate to Android project directory:**
   ```bash
   cd "D:\Android Project\SpringAI"
   ```

2. **Clean and build:**
   ```bash
   ./gradlew clean build
   ```

3. **Run the app:**
   - Open the project in Android Studio
   - Connect an Android device or start an emulator
   - Click "Run" or use Ctrl+F10

## API Configuration

The Android app is configured to connect to the backend using:
- **For Emulator**: `http://10.0.2.2:8081/api/ai/ask`
- **For Physical Device**: You'll need to use your computer's IP address instead of `10.0.2.2`

To find your IP address:
```bash
ipconfig | findstr "IPv4"
```

Then update the `apiUrl` in `MainActivity.kt`:
```kotlin
private val apiUrl = "http://YOUR_IP_ADDRESS:8081/api/ai/ask"
```

## Project Structure

### Android App (D:\Android Project\SpringAI)
```
app/
├── src/main/
│   ├── java/com/michael/springai/
│   │   ├── MainActivity.kt          # Main activity with Compose UI
│   │   └── ui/theme/               # Theme configuration
│   ├── res/                        # Android resources
│   └── AndroidManifest.xml         # App configuration
├── build.gradle.kts                # App dependencies
└── README.md                       # This file
```

### Backend API (D:\Belajar\Java\BelajarAI)
```
src/main/
├── java/com/michael/SpringAI/
│   ├── SpringAiApplication.java    # Main Spring Boot application
│   ├── controller/
│   │   └── AiController.java       # REST API endpoints
│   └── service/
│       └── AiService.java          # AI integration service
└── resources/
    └── application.properties      # Configuration
```

## API Endpoints

### POST /api/ai/ask
Sends a message to the AI and returns a response.

**Request:**
```json
{
  "message": "Hello, how are you?"
}
```

**Response:** Plain text AI response

## Quick Start

**Easy Option:** Run the provided script:
```bash
start_projects.bat
```

This will automatically:
1. Start the backend server on port 8081
2. Build the Android APK
3. Give you next steps

## Troubleshooting

### ✅ Current Status:
- ✅ Android project builds successfully 
- ✅ Backend compiles and runs successfully
- ✅ All major compilation issues fixed
- ✅ Theme.kt simplified to avoid Kotlin compiler errors
- ✅ Dependencies properly configured

### Common Issues:

1. **Android lint errors (file locking):**
   ```bash
   # Build without lint (recommended)
   ./gradlew assembleDebug -x lint
   
   # Alternative: Close Android Studio first, then clean
   ./gradlew clean build
   ```

2. **Port 8081 already in use:**
   ```bash
   # Find process using port 8081
   netstat -ano | findstr :8081
   
   # Kill the process (replace PID with actual process ID)
   taskkill /PID <PID> /F
   ```

3. **Android app can't connect to backend:**
   - Make sure backend is running on port 8081
   - Check if using correct IP address (10.0.2.2 for emulator)
   - Verify `INTERNET` permission in AndroidManifest.xml (already added)

4. **Backend compilation errors:**
   ```bash
   mvn clean compile
   ```

5. **Android Studio issues:**
   - Close Android Studio completely
   - Delete `.idea` folder if needed
   - Reimport project
   - Sync Gradle files

6. **AI API not working:**
   - Verify your OpenRouter API key is valid
   - Check internet connectivity
   - Review backend logs for API errors

### Build Commands That Work:
```bash
# Backend
cd "D:\Belajar\Java\BelajarAI"
mvn spring-boot:run

# Android (skip lint to avoid file locking)
cd "D:\Android Project\SpringAI"
./gradlew assembleDebug -x lint
```

## Features

- **Android UI**: Clean Jetpack Compose interface with Material 3 design
- **Real-time Chat**: Send messages to AI and receive responses
- **Error Handling**: Comprehensive error handling for network issues
- **Responsive Design**: Adapts to different screen sizes
- **Secure API Key Management**: Environment variable support for API keys

## Development Notes

- The app uses Volley for HTTP requests (you can migrate to Retrofit for more advanced features)
- Backend uses WebFlux for reactive programming
- CORS is enabled for cross-origin requests
- The theme supports both light/dark modes and dynamic colors (Android 12+)

## Next Steps

1. **Security Improvements:**
   - Move API key to environment variable completely
   - Add proper authentication between Android and backend
   
2. **Features to Add:**
   - Chat history persistence
   - User authentication
   - Multiple AI models support
   - File upload support

3. **Performance:**
   - Add request caching
   - Implement proper error retry logic
   - Add loading states

## Support

If you encounter any issues:
1. Check the troubleshooting section above
2. Verify all prerequisites are installed
3. Make sure both projects build successfully before running
4. Check network connectivity between Android and backend
