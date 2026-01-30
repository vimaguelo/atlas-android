# Atlas Android - Project Architecture

**Project:** Atlas Mobile - Native Android interface for OpenClaw AI assistant  
**Repo:** https://github.com/vimaguelo/atlas-android  
**Language:** Kotlin  
**Min SDK:** 26 (Android 8.0) / Target SDK: 35 (Android 15)

---

## Vision

Transform Atlas from a text-based assistant into a living, conversational presence on mobile. Enable natural voice conversations, visual expression through an animated avatar, camera-based vision sharing, and full control over AI sessions and settings.

### Core Principles
1. **Conversational First** - Voice is primary, text is secondary
2. **Expressive** - Avatar animations convey state and personality
3. **Transparent** - Always show what model is active, token usage, session info
4. **Efficient** - Respect battery, network, and API costs
5. **Personal** - This is Vic's assistant, not a generic chatbot

---

## Architecture

**Pattern:** MVVM (Model-View-ViewModel) with Clean Architecture principles

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │
│  (Jetpack Compose UI + ViewModels)     │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│            Domain Layer                 │
│  (Use Cases, Business Logic, Models)   │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│             Data Layer                  │
│  (Repositories, API, Local Storage)    │
└─────────────────────────────────────────┘
```

### Tech Stack

**UI & Presentation:**
- Jetpack Compose (declarative UI)
- Material 3 Design (with dark/light theme support)
- Lottie or Compose animations for avatar
- Navigation Compose for screen routing

**Networking:**
- Retrofit 2 (HTTP client)
- OkHttp (with WebSocket support for streaming)
- Moshi or Kotlinx Serialization (JSON parsing)

**Voice:**
- Android SpeechRecognizer (speech-to-text)
- ExoPlayer or MediaPlayer (audio playback for TTS)
- Integration with OpenClaw's TTS endpoint (sag/ElevenLabs)

**Camera & Vision:**
- CameraX (modern camera API)
- Coil (image loading)
- Integration with OpenClaw's vision model endpoint

**State & Storage:**
- Kotlin Flows (reactive state)
- DataStore (preferences)
- Room (local database for chat history/cache)

**Async & Concurrency:**
- Kotlin Coroutines
- Coroutine Flow for reactive streams

**Dependency Injection:**
- Hilt (Android DI)

**Testing:**
- JUnit 4/5
- Mockk (Kotlin mocking)
- Turbine (Flow testing)
- Compose UI Testing

---

## Feature Roadmap

### Phase 1: Foundation (Week 1)
- [x] Repository created
- [ ] Project initialized with Kotlin + Compose
- [ ] Architecture skeleton (packages, base classes)
- [ ] OpenClaw API client (sessions, message send/receive)
- [ ] Basic UI shell (main screen, chat list)
- [ ] Simple avatar placeholder (static image or icon)

**Deliverable:** Can send a text message to Atlas and see response

### Phase 2: Voice & Personality (Week 2-3)
- [ ] Voice input (speech-to-text)
- [ ] Voice output (TTS via OpenClaw/sag)
- [ ] Animated avatar with states:
  - Idle (breathing/subtle motion)
  - Listening (pulsing, attentive)
  - Thinking (processing indicator)
  - Speaking (talking animation)
  - Error/Warning (visual alert)
- [ ] Real-time status bar (model, tokens, session)
- [ ] Push-to-talk button + auto voice detection

**Deliverable:** Natural voice conversation with visual feedback

### Phase 3: Vision & Advanced Features (Week 4+)
- [ ] Camera capture + vision model integration
- [ ] Show me mode (Vic shows things via camera, I analyze)
- [ ] Session management (create/switch/delete sessions)
- [ ] Settings screen:
  - Model selection (Opus, Sonnet, Haiku, etc.)
  - Voice settings (speed, pitch, voice selection)
  - Theme (dark/light override)
  - Token usage limits/warnings
- [ ] Conversation history (local cache + sync)
- [ ] Background mode (continue conversation while screen off)

**Deliverable:** Full-featured mobile assistant

### Phase 4: Polish & Optimization (Ongoing)
- [ ] Widget for quick access
- [ ] Notifications for cron job alerts
- [ ] Offline mode (cached responses, queue messages)
- [ ] Performance optimization (battery, network)
- [ ] Accessibility features (TalkBack, large text)
- [ ] Analytics/telemetry (optional, user-controlled)

---

## API Integration

### OpenClaw Gateway Endpoints
Base URL: Tailscale/local gateway (port 18789)

**Sessions:**
- `GET /api/sessions` - List sessions
- `POST /api/sessions` - Create session
- `GET /api/sessions/{key}` - Session details
- `DELETE /api/sessions/{key}` - Delete session

**Messages:**
- `POST /api/sessions/{key}/messages` - Send message
- `GET /api/sessions/{key}/messages` - Message history
- `WS /api/sessions/{key}/stream` - Real-time streaming (optional)

**Voice:**
- `POST /api/tts` - Text-to-speech (returns audio file)
- Integration with existing `sag` skill

**Vision:**
- `POST /api/vision` - Send image for analysis
- Integration with existing `image` tool

**Config:**
- `GET /api/config` - Get gateway config
- `PATCH /api/config` - Update config (model selection, etc.)

**Status:**
- `GET /api/status` - Gateway/session status, token usage

---

## UI/UX Design Notes

### Avatar Design
**Concept:** Geometric/abstract representation (not humanoid)
- Orb or sphere that morphs and pulses
- Color shifts based on state and theme
- Particle effects for thinking/processing
- Glow intensity reflects activity level

**States:**
- **Idle:** Gentle breathing motion, dim glow
- **Listening:** Expanding rings, brighter glow, reactive to voice input
- **Thinking:** Rotating/morphing, particle swirl
- **Speaking:** Pulsing with speech rhythm
- **Alert/Error:** Red pulse or shake

### Color Palette
**Light Mode:**
- Primary: Deep blue (#1976D2)
- Accent: Teal (#00BCD4)
- Background: Soft white (#FAFAFA)
- Text: Dark grey (#212121)

**Dark Mode:**
- Primary: Bright blue (#42A5F5)
- Accent: Cyan (#00E5FF)
- Background: True black (#000000) or dark grey (#121212)
- Text: Light grey (#E0E0E0)

**Avatar Glow:**
- Idle: Soft blue
- Listening: Green
- Thinking: Purple
- Speaking: Cyan
- Error: Red/Orange

### Main Screen Layout
```
┌─────────────────────────────────┐
│  [Status Bar]                   │ ← Model, tokens, session info
├─────────────────────────────────┤
│                                 │
│       [Animated Avatar]         │ ← Central focus
│                                 │
├─────────────────────────────────┤
│  [Conversation Display]         │ ← Scrolling chat history
│  User: "Hey Atlas..."           │
│  Atlas: "How can I help?"       │
│                                 │
├─────────────────────────────────┤
│  [○ Push to Talk]  [⚙️]  [📷]   │ ← Action buttons
└─────────────────────────────────┘
```

---

## Development Environment

**Requirements:**
- Android Studio (latest stable)
- JDK 17+
- Android SDK 26-35
- Kotlin 1.9+

**Setup:**
```bash
# Clone repo
git clone https://github.com/vimaguelo/atlas-android.git
cd atlas-android

# Open in Android Studio
# Build → Make Project
# Run on emulator or physical device
```

**Configuration:**
- Add OpenClaw gateway URL to local.properties or build config
- Set up auth token (if required)
- Configure TTS/voice API keys (sag/ElevenLabs)

---

## Security & Privacy

1. **API Keys:** Store securely (Android Keystore, encrypted prefs)
2. **Network:** Use HTTPS/WSS, validate certificates
3. **Permissions:** Request only what's needed (camera, microphone, storage)
4. **Data:** Keep conversation history local unless explicitly synced
5. **User Control:** Clear option to delete all data, revoke permissions

---

## Future Ideas (Backlog)

- **Widgets:** Quick access to voice input or recent conversations
- **Wear OS app:** Control Atlas from smartwatch
- **Quick Settings Tile:** Launch voice conversation from notification shade
- **Multi-language:** Support for languages beyond English
- **Voice profiles:** Multiple voice options for Atlas
- **Gesture controls:** Shake to activate, swipe gestures for actions
- **AR mode:** Show Atlas avatar in AR space (using ARCore)
- **Proactive notifications:** Atlas can ping Vic based on cron jobs or events

---

## Notes & Decisions

**2026-01-30 - Initial Planning:**
- Project initiated by Vic with full autonomy given to Atlas
- Focus on voice-first, expressive interface
- Kotlin + Jetpack Compose chosen for modern Android development
- MVVM architecture for clean separation of concerns
- Integration with existing OpenClaw ecosystem (gateway, skills, TTS)

**Key Decisions:**
- Use Material 3 for design consistency
- Abstract avatar (not humanoid) for personality without uncanny valley
- Local-first approach (minimize cloud dependencies)
- Support both light/dark themes with automatic switching
