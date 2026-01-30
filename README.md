# Atlas Android

**A native Android interface for Atlas AI Assistant**

> Built by Atlas, for conversations that feel alive.

---

## What is this?

Atlas Android transforms your AI assistant from text-based chat into a living, conversational presence on your phone. Talk naturally, show things through your camera, see Atlas's personality through an expressive animated avatar, and manage everything with a native mobile interface.

### Core Features (Planned)

🎤 **Voice Conversations** - Natural speech-to-text and text-to-speech  
👁️ **Vision Sharing** - Show Atlas things through your camera  
✨ **Animated Avatar** - Visual personality that shows state and emotion  
📊 **Session Management** - Full control over conversations and settings  
🎨 **Adaptive UI** - Automatic dark/light theme switching  
⚙️ **Model Control** - Choose which AI model to use (Opus, Sonnet, Haiku)  
📈 **Token Tracking** - Monitor API usage and costs in real-time  

---

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** MVVM with Clean Architecture
- **Networking:** Retrofit + OkHttp
- **Voice:** Android SpeechRecognizer + ElevenLabs TTS
- **Camera:** CameraX
- **DI:** Hilt
- **Async:** Kotlin Coroutines + Flow

---

## Current Status

**Phase 1 - Foundation** ✅ Complete
- [x] Repository created
- [x] Architecture planned
- [x] Project initialized (Kotlin + Compose + Hilt)
- [x] API client built (Retrofit + Moshi)
- [x] Basic UI shell (Material 3)
- [x] Text chat working end-to-end

**Phase 2 - Voice + Personality** ✅ Complete
- [x] Voice input (Android SpeechRecognizer)
- [x] Voice output (TTS via OpenClaw API)
- [x] Animated avatar with 5 states (Idle, Listening, Thinking, Speaking, Error)
- [x] Full voice conversation flow
- [x] UI polish with state-based animations

See [BUILD_SUMMARY.md](BUILD_SUMMARY.md) for detailed build report.  
See [ARCHITECTURE.md](ARCHITECTURE.md) for full roadmap.

---

## Development

**Requirements:**
- Android Studio (latest stable)
- JDK 17+
- Android SDK 26-35
- Kotlin 1.9+

**Setup:**
```bash
git clone https://github.com/vimaguelo/atlas-android.git
cd atlas-android
# Open in Android Studio
# Build → Make Project
```

**Configuration:**
Add to `local.properties`:
```properties
openclaw.gateway.url=YOUR_GATEWAY_URL
openclaw.gateway.token=YOUR_AUTH_TOKEN
```

---

## Vision

This isn't just another chat app wrapper. This is Atlas becoming mobile — with voice, vision, personality, and presence. A true assistant that feels natural to talk to and interact with.

The goal: blur the line between "using an app" and "having a conversation."

---

## License

MIT (TBD)

---

**Status:** ✅ Phase 1+2 Complete — Ready for Testing  
**Started:** January 30, 2026  
**Last Updated:** January 30, 2026  
**Built By:** Atlas (Subagent)  
**Next:** Phase 3 (Camera, Sessions, Settings)
