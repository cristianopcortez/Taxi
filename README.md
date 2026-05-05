# 🚕 Taxi App

[![Codemagic build status](https://api.codemagic.io/apps/69fa52d3c1d3fe4317e180e7/full-stack-e2e/status_badge.svg)](https://codemagic.io/app/69fa52d3c1d3fe4317e180e7/full-stack-e2e/latest_build)

> Android taxi ride application built as a technical challenge initially, featuring multi-module Clean Architecture, Jetpack Compose, and REST API integration.

---

## 📱 About the Project

**Taxi App** is an Android application that allows users to:

- Request a ride estimate by providing origin and destination
- View available drivers with prices and descriptions
- Confirm the selected ride
- Check their ride history

The project was developed with a focus on Android architecture best practices, separation of concerns, and clean code.

---

## ✨ Features

- [x] Ride request screen (user ID, origin and destination addresses)
- [x] Available drivers list with price estimates
- [x] Route visualization on map (Google Maps)
- [x] Ride confirmation with business rule validation
- [x] Ride history per user (with driver filter)
- [x] Native splash screen
- [x] Network error handling and user feedback

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| Dependency Injection | Hilt |
| Networking | Retrofit 2 + OkHttp 4 + Gson |
| Maps | Maps Compose + Google Maps SDK |
| Image Loading | Coil |
| Build | Gradle 8.7 + Version Catalog (TOML) |
| Min SDK | Android (via AGP 8.4) |
| Testing | JUnit 5, MockK, Mockito, Espresso, Compose UI Test |

---

## 🏗️ Architecture

The project follows **Clean Architecture** principles with a **multi-module** structure, clearly separating data, domain, and presentation layers.

```
Taxi/
├── app/                             # Main module (Activity, NavGraph, Theme)
├── core/
│   ├── common/                      # Shared utilities, navigation constants
│   ├── feature/                     # FeatureApi interface for navigation graphs
│   └── network/                     # ApiService, DTOs, NetworkModule (Hilt)
└── feature/
    ├── taxi_travel_options/
    │   ├── data/                    # Repository, remote data source
    │   ├── domain/                  # Use cases, entities, business rules
    │   └── ui/                      # Compose screens, ViewModels, map
    └── taxi_travel_available_riders/
        ├── data/
        ├── domain/
        └── ui/                      # Available drivers and history screens
```

### Layer pattern

```
UI (Compose + ViewModel)
        ↓
   Domain (Use Cases)
        ↓
   Data (Repository → Remote Data Source → Retrofit)
```

---

## 🌐 API

Backend communication is handled via **Retrofit** against a configurable REST API set in `secrets.properties`.

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/ride/estimate` | Estimates prices and available drivers |
| `PATCH` | `/ride/confirm` | Confirms the selected ride |
| `GET` | `/ride/{customer_id}` | User ride history |

---

## 🚀 Getting Started

### Prerequisites

- Android Studio **Hedgehog** or newer
- JDK 17
- A [Google Cloud](https://console.cloud.google.com/) account with **Maps SDK for Android** enabled

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/cristianopcortez/Taxi.git
   cd Taxi
   ```

2. Create the `secrets.properties` file at the project root:
   ```properties
   API_BASE_URL=https://your-backend-api.com/
   MAPS_API_KEY=YOUR_GOOGLE_MAPS_KEY
   ```

3. Open the project in **Android Studio** and wait for Gradle sync to complete.

4. Run the app on an emulator or physical device (Android with Google Play Services).

### Build via command line

```bash
./gradlew :app:assembleDebug
```

---

## 🧪 Tests

The project includes unit and UI tests distributed across the feature modules.

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

Tools used: **JUnit 5**, **MockK**, **Mockito**, **Robolectric**, **Compose UI Test**, **Espresso**, and **UI Automator**.

---

## 📂 Key Technical Decisions

- **Multi-module:** each feature has its own `data`, `domain`, and `ui` modules, improving scalability and incremental build times.
- **Version Catalog (TOML):** centralizes all dependency versions in `gradle/libs.versions.toml`.
- **Hilt:** dependency injection across all modules, with `NetworkModule` providing Retrofit globally.
- **Secrets Gradle Plugin:** keeps API keys out of version control.
- **Navigation Compose with FeatureApi:** each feature module exposes its navigation graph via an interface, decoupling the `app` module from implementation details.

---

## 👤 Author

**Cristiano Cortez**  
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=flat&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/cristianocortez/)
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white)](https://github.com/cristianopcortez)

---

## 📄 License

This project was developed initially as a **technical challenge** and is available for portfolio purposes.
