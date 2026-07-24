# ResQAI

Android app for disaster and emergency response — SOS alerts, shelter finder, and safe-route navigation, built with Kotlin and Jetpack Compose.

## Overview

ResQAI helps users stay safe during emergencies by providing quick access to SOS requests, real-time alerts, nearby shelter information, and safe navigation routes. The app is designed with an MVVM architecture for a clean separation between UI, business logic, and data.

> **Status:** Frontend UI and architecture are built. Live weather-based risk scoring (ML) and backend integration are in progress.

## Features

- **SOS button** — quick emergency request with live status tracking
- **Alerts** — view active alerts with severity levels and details
- **Shelter finder** — locate nearby shelters
- **Safe route navigation** — map-based safe routing during emergencies
- **User profiles & history** — track past requests and account details
- **Weather-based risk alerts** *(in progress)* — ML model ingesting live weather data to predict and surface risk levels

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose, Material 3
- **Architecture:** MVVM (ViewModel + Repository pattern)
- **Navigation:** Navigation Compose
- **Build system:** Gradle (Kotlin DSL), version catalogs (`libs.versions.toml`)
- **Planned backend:** Firebase (Auth, Firestore, Cloud Messaging) / Spring Boot API
- **Planned ML serving:** FastAPI service consuming live weather data

## Project Structure

```
app/src/main/java/com/example/resqai/
├── components/     # Reusable Compose UI components (buttons, cards, dialogs, etc.)
├── model/           # Data models (Alert, SOSRequest, Shelter, User, etc.)
├── navigation/      # Navigation graph and routes
├── repository/      # Data layer — will connect to backend/Firebase
├── screens/         # App screens (Home, SOS, Alerts, Shelter, Profile, etc.)
├── ui/              # Theme, typography, colors
├── viewmodel/       # ViewModels per feature
└── MainActivity.kt
```

## Getting Started

### Prerequisites
- Android Studio (latest stable)
- JDK 11+
- Android SDK, minSdk 24 / targetSdk 37

### Setup
```bash
git clone https://github.com/<your-username>/ResQAI.git
```
1. Open the project in Android Studio.
2. Let Gradle sync (it will auto-generate your local `local.properties` with your SDK path).
3. Run on an emulator or physical device (API 24+).

> Note: `local.properties` is machine-specific and is not committed — Android Studio generates it automatically on first sync.

## Roadmap

- [x] Core UI screens and navigation (Compose)
- [x] MVVM structure with ViewModels and Repository interfaces
- [ ] Firebase Auth integration
- [ ] Real backend for Alerts, Shelters, SOS requests
- [ ] ML-based weather risk scoring integration
- [ ] Push notifications for live alerts
- [ ] Play Store release build

## Team

- **Android/Frontend:** [Your name]
- **ML:** [Friend's name]
- **QA / Data / Store Listing:** [Teammate's name]

## License

Not yet decided.
