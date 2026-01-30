# Project Structure

```
atlas-android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/atlas/android/
│   │   │   │   ├── AtlasApplication.kt
│   │   │   │   ├── MainActivity.kt
│   │   │   │   │
│   │   │   │   ├── di/                    # Dependency Injection (Hilt modules)
│   │   │   │   │   ├── AppModule.kt
│   │   │   │   │   ├── NetworkModule.kt
│   │   │   │   │   └── RepositoryModule.kt
│   │   │   │   │
│   │   │   │   ├── data/                  # Data Layer
│   │   │   │   │   ├── api/              # API clients
│   │   │   │   │   │   ├── OpenClawApi.kt
│   │   │   │   │   │   ├── models/       # API DTOs
│   │   │   │   │   │   └── interceptors/ # Auth, logging
│   │   │   │   │   ├── local/            # Local storage
│   │   │   │   │   │   ├── dao/          # Room DAOs
│   │   │   │   │   │   ├── entities/     # DB entities
│   │   │   │   │   │   └── prefs/        # DataStore
│   │   │   │   │   └── repository/       # Repository implementations
│   │   │   │   │       ├── SessionRepository.kt
│   │   │   │   │       ├── MessageRepository.kt
│   │   │   │   │       └── ConfigRepository.kt
│   │   │   │   │
│   │   │   │   ├── domain/               # Domain Layer
│   │   │   │   │   ├── model/            # Domain models
│   │   │   │   │   │   ├── Session.kt
│   │   │   │   │   │   ├── Message.kt
│   │   │   │   │   │   └── AtlasState.kt
│   │   │   │   │   ├── usecase/          # Use cases
│   │   │   │   │   │   ├── SendMessageUseCase.kt
│   │   │   │   │   │   ├── StartVoiceConversationUseCase.kt
│   │   │   │   │   │   └── CaptureImageUseCase.kt
│   │   │   │   │   └── repository/       # Repository interfaces
│   │   │   │   │
│   │   │   │   ├── ui/                   # Presentation Layer
│   │   │   │   │   ├── theme/            # Compose theme
│   │   │   │   │   │   ├── Theme.kt
│   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   └── Type.kt
│   │   │   │   │   │
│   │   │   │   │   ├── components/       # Reusable UI components
│   │   │   │   │   │   ├── AtlasAvatar.kt
│   │   │   │   │   │   ├── VoiceButton.kt
│   │   │   │   │   │   └── MessageBubble.kt
│   │   │   │   │   │
│   │   │   │   │   ├── screens/          # Feature screens
│   │   │   │   │   │   ├── main/
│   │   │   │   │   │   │   ├── MainScreen.kt
│   │   │   │   │   │   │   ├── MainViewModel.kt
│   │   │   │   │   │   │   └── MainState.kt
│   │   │   │   │   │   ├── settings/
│   │   │   │   │   │   │   ├── SettingsScreen.kt
│   │   │   │   │   │   │   └── SettingsViewModel.kt
│   │   │   │   │   │   └── sessions/
│   │   │   │   │   │       ├── SessionListScreen.kt
│   │   │   │   │   │       └── SessionListViewModel.kt
│   │   │   │   │   │
│   │   │   │   │   └── navigation/       # Navigation graph
│   │   │   │   │       └── AtlasNavGraph.kt
│   │   │   │   │
│   │   │   │   └── util/                 # Utilities
│   │   │   │       ├── VoiceManager.kt   # Speech-to-text / TTS
│   │   │   │       ├── CameraManager.kt  # Camera utilities
│   │   │   │       └── Extensions.kt     # Kotlin extensions
│   │   │   │
│   │   │   ├── res/                      # Resources
│   │   │   │   ├── drawable/            # Icons, images
│   │   │   │   ├── values/              # Strings, colors, themes
│   │   │   │   └── raw/                 # Audio files, animations
│   │   │   │
│   │   │   └── AndroidManifest.xml
│   │   │
│   │   └── test/                         # Unit tests
│   │       └── java/com/atlas/android/
│   │           ├── data/
│   │           ├── domain/
│   │           └── ui/
│   │
│   └── build.gradle.kts
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── local.properties                      # Git-ignored (gateway URL, tokens)
│
├── docs/                                 # Additional documentation
│   ├── API.md                           # API endpoints reference
│   ├── DESIGN.md                        # UI/UX design specs
│   └── CONTRIBUTING.md                  # Contribution guidelines
│
├── .gitignore
├── README.md
├── ARCHITECTURE.md
├── PROJECT_STRUCTURE.md                 # This file
└── LICENSE
```

## Package Naming Convention

`com.atlas.android`

**Rationale:** Simple, clear, reflects the project identity.

## Key Principles

1. **Layer Separation:** Data, Domain, and Presentation layers are strictly separated
2. **Dependency Rule:** Dependencies flow inward (UI → Domain ← Data)
3. **Single Responsibility:** Each class has one clear purpose
4. **Testability:** Every layer is testable in isolation
5. **Scalability:** Easy to add new features without affecting existing code

## Next Steps

1. Initialize Android project with Gradle
2. Set up Hilt for DI
3. Create base package structure
4. Implement API client
5. Build basic UI shell
