# 🏘️ Grama Suvidha — ग्राम सुविधा

> **A transparency-focused rural infrastructure project tracker for village-level governance**

Grama Suvidha is an Android application that empowers citizens to monitor, track, and provide feedback on rural infrastructure development projects at the Gram Panchayat level. It supports both **English** and **Kannada (ಕನ್ನಡ)** languages and works fully **offline** using local caching.

---

## 📱 Screenshots & Features

### 🔑 Key Features

| Feature | Description |
|---|---|
| 📋 Project List | Scrollable list of all village infrastructure projects with progress bars |
| 🔍 Project Detail | Full detail view with budget, status, completion date, and before/after photos |
| 📊 Progress Visualization | Visual progress bar showing real-time completion percentage |
| 🌐 Multilingual Support | Switch between English and Kannada with a single tap |
| 💾 Offline Caching | All project data cached locally using Room database |
| ⭐ Citizen Feedback | Rate projects on a 5-star scale from within the app |
| 🚨 Report Issues | Citizens can flag problems with ongoing projects |
| 🖼️ Before/After Photos | Image comparison view for visual project progress |

---

## 🏗️ Project Structure

```
grama-suvidha/
├── app/
│   └── src/
│       └── main/
│           ├── assets/
│           │   └── projects.json           # Mock API data (4 sample projects)
│           ├── java/com/example/gramasuvidha/
│           │   ├── GramaSuvidhaApp.kt      # Application class
│           │   ├── MainActivity.kt         # Entry point + language switching logic
│           │   ├── data/
│           │   │   ├── model/
│           │   │   │   └── Project.kt      # Room Entity & data class
│           │   │   ├── local/
│           │   │   │   ├── AppDatabase.kt  # Room Database
│           │   │   │   └── ProjectDao.kt   # DAO (Data Access Object)
│           │   │   └── repository/
│           │   │       └── ProjectRepository.kt  # Data layer (Room + JSON)
│           │   └── ui/
│           │       ├── screens/
│           │       │   ├── ProjectListScreen.kt   # Main list screen
│           │       │   └── ProjectDetailScreen.kt # Detail + feedback screen
│           │       ├── viewmodel/
│           │       │   └── ProjectViewModel.kt    # ViewModel (StateFlow)
│           │       └── theme/
│           │           ├── Color.kt        # App color palette
│           │           └── Theme.kt        # Material3 theme config
│           └── res/
│               ├── values/strings.xml      # English strings
│               └── values-kn/strings.xml   # Kannada strings
└── build.gradle.kts                        # Project-level Gradle config
```

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| **Kotlin** | 1.9.x | Primary programming language |
| **Jetpack Compose** | BOM `2023.10.01` | Modern declarative UI toolkit |
| **Material 3** | Latest | Design system & UI components |
| **Room Database** | `2.6.1` | Local SQLite abstraction for offline caching |
| **KSP** | Latest | Kotlin Symbol Processing for Room annotation processing |
| **Coil Compose** | `2.5.0` | Async image loading for before/after photos |
| **Gson** | `2.10.1` | JSON parsing for Mock API (`projects.json`) |
| **Kotlin Coroutines** | `1.7.x` | Async/background operations |
| **Kotlin Flow / StateFlow** | Built-in | Reactive data streams between Repository and ViewModel |
| **AndroidX Lifecycle** | `2.7.0` | ViewModel, LiveData, Lifecycle-aware components |
| **AndroidX Activity Compose** | `1.8.2` | Compose integration with Android Activity |

---

## 📐 Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture with a clean data layer:

```
┌─────────────────────────────────────────────────┐
│                     UI Layer                     │
│  ProjectListScreen  ◄──►  ProjectDetailScreen   │
└───────────────────────┬─────────────────────────┘
                        │ observes StateFlow
┌───────────────────────▼─────────────────────────┐
│                  ViewModel Layer                 │
│              ProjectViewModel                    │
│   (AndroidViewModel + viewModelScope)            │
└───────────────────────┬─────────────────────────┘
                        │ calls
┌───────────────────────▼─────────────────────────┐
│                 Repository Layer                 │
│              ProjectRepository                   │
│   - Reads from Room DB (Flow<List<Project>>)     │
│   - Loads JSON from assets on first launch       │
└──────────┬────────────────────────┬─────────────┘
           │                        │
┌──────────▼──────┐      ┌─────────▼───────────┐
│   Room Database  │      │   projects.json      │
│   (AppDatabase)  │      │   (Mock API / Assets)│
│   ProjectDao     │      │                     │
└─────────────────┘      └─────────────────────┘
```

### Data Flow
1. On first launch, `ProjectRepository` checks if the Room DB is empty.
2. If empty, it reads `projects.json` from the `assets/` folder using **Gson** and seeds the database.
3. `ProjectDao` exposes all projects as a `Flow<List<Project>>`.
4. `ProjectViewModel` converts this into a `StateFlow` (using `stateIn`) for Compose to observe.
5. Compose screens recompose reactively whenever the data changes.

---

## 🗄️ Data Model

### `Project` (Room Entity)

```kotlin
@Entity(tableName = "projects")
data class Project(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val budget: String,           // e.g., "₹5,00,000"
    val completionDate: String,   // e.g., "2026-08-15"
    val status: String,           // "Planning" | "In Progress" | "Completed"
    val progressPercentage: Int,  // 0–100
    val beforeImageUrl: String,   // URL to before photo
    val afterImageUrl: String     // URL to after/current photo
)
```

### Mock API (`projects.json`) — 4 Sample Projects

| # | Project | Budget | Status | Progress |
|---|---|---|---|---|
| 1 | Main Road Repair | ₹5,00,000 | In Progress | 50% |
| 2 | Borewell Installation | ₹1,50,000 | Completed | 100% |
| 3 | Community Hall Construction | ₹12,00,000 | Planning | 10% |
| 4 | Pond Rejuvenation | ₹3,00,000 | In Progress | 75% |

---

## 🌍 Multilingual Support

The app supports **English** and **Kannada** via Android's resource localization system:

| String Key | English | Kannada (ಕನ್ನಡ) |
|---|---|---|
| `app_name` | Grama Suvidha | ಗ್ರಾಮ ಸುವಿಧಾ |
| `projects_list_title` | Village Projects | ಗ್ರಾಮ ಯೋಜನೆಗಳು |
| `citizen_feedback` | Citizen Feedback | ನಾಗರಿಕ ಪ್ರತಿಕ್ರಿಯೆ |
| `switch_language` | ಕನ್ನಡ | English |

Language switching is handled at runtime by restarting the activity with a locale override — no app reinstall needed.

---

## ⚙️ Build Configuration

| Setting | Value |
|---|---|
| `compileSdk` | 34 (Android 14) |
| `minSdk` | 24 (Android 7.0 Nougat) |
| `targetSdk` | 34 |
| `versionCode` | 1 |
| `versionName` | 1.0 |
| JVM Target | Java 17 |
| Kotlin Compiler Extension | `1.5.8` |

---

## 🚀 Getting Started

### Prerequisites
- Android Studio **Hedgehog** (2023.1.1) or later
- JDK 17
- Android SDK with API Level 34

### Setup & Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/ashishvid18/grama-suvidha.git
   cd grama-suvidha
   ```

2. **Open in Android Studio**
   - Open Android Studio → `File > Open` → select the `grama-suvidha` folder

3. **Let Gradle sync**
   - Wait for Gradle to download all dependencies automatically

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the **▶ Run** button or press `Shift + F10`

> **Note:** No API keys or external services are required. All data is bundled in `assets/projects.json`.

---

## 🎨 UI & Theme

- Built with **Material 3 (Material You)** design system
- Custom `ProgressGreen` color (`#4CAF50`) for progress indicators
- Responsive `LazyColumn` list with `Card` components and 4dp elevation
- `AsyncImage` (Coil) for efficient image loading with crop scaling
- `TopAppBar` with language switch action button

---

## 🧩 Dependencies (Full List)

```kotlin
// Core
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
implementation("androidx.activity:activity-compose:1.8.2")

// Jetpack Compose (BOM)
implementation(platform("androidx.compose:compose-bom:2023.10.01"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.compose.material3:material3")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// Image Loading
implementation("io.coil-kt:coil-compose:2.5.0")

// JSON Parsing
implementation("com.google.code.gson:gson:2.10.1")
```

---

## 👨‍💻 Developer

**Ashish Vidyarthi**
- GitHub: [@ashishvid18](https://github.com/ashishvid18)

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).

---

> *Grama Suvidha — Bringing transparency to rural development, one project at a time.* 🌾
